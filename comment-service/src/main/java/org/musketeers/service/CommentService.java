package org.musketeers.service;

import org.musketeers.dto.request.AddCommentRequestDto;
import org.musketeers.dto.request.RateCommentRequestDto;
import org.musketeers.dto.response.GetAllCommentsByCompanyResponseDto;
import org.musketeers.entity.Comment;
import org.musketeers.entity.enums.EActivationStatus;
import org.musketeers.mapper.ICommentMapper;
import org.musketeers.rabbitmq.model.*;
import org.musketeers.rabbitmq.producer.GetCompanyDetailsByCommentRequestProducer;
import org.musketeers.rabbitmq.producer.GetPersonnelDetailsByCommentRequestProducer;
import org.musketeers.rabbitmq.producer.GetPersonnelIdAndCompanyIdByTokenRequestProducer;
import org.musketeers.repository.CommentRepository;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CommentService extends ServiceManager<Comment, String> {

    private final CommentRepository commentRepository;

    private final GetPersonnelIdAndCompanyIdByTokenRequestProducer getPersonnelIdAndCompanyIdByTokenRequestProducer;

    private final GetPersonnelDetailsByCommentRequestProducer getPersonnelDetailsByCommentRequestProducer;

    private final GetCompanyDetailsByCommentRequestProducer getCompanyDetailsByCommentRequestProducer;

    public CommentService(CommentRepository commentRepository, GetPersonnelIdAndCompanyIdByTokenRequestProducer getPersonnelIdAndCompanyIdByTokenRequestProducer, GetPersonnelDetailsByCommentRequestProducer getPersonnelDetailsByCommentRequestProducer, GetCompanyDetailsByCommentRequestProducer getCompanyDetailsByCommentRequestProducer) {
        super(commentRepository);
        this.commentRepository = commentRepository;
        this.getPersonnelIdAndCompanyIdByTokenRequestProducer = getPersonnelIdAndCompanyIdByTokenRequestProducer;
        this.getPersonnelDetailsByCommentRequestProducer = getPersonnelDetailsByCommentRequestProducer;
        this.getCompanyDetailsByCommentRequestProducer = getCompanyDetailsByCommentRequestProducer;
    }

    public Boolean addComment(AddCommentRequestDto dto) {
        GetPersonnelIdAndCompanyIdByTokenRequestModel requestModel = GetPersonnelIdAndCompanyIdByTokenRequestModel.builder().token(dto.getToken()).build();
        GetPersonnelIdAndCompanyIdByTokenResponseModel responseModel = getPersonnelIdAndCompanyIdByTokenRequestProducer.getPersonnelIdAndCompanyIdFromPersonnelService(requestModel);
        Comment comment = Comment.builder()
                .companyId(responseModel.getCompanyId())
                .personnelId(responseModel.getPersonnelId())
                .header(dto.getHeader())
                .content(dto.getContent())
                .rating(dto.getRating())
                .build();
        save(comment);
        return true;
    }

    public List<GetAllCommentsByCompanyResponseDto> getAllCommentsByCompany(String companyId) {
//        List<Comment> activeCommentsByCompany = commentRepository.findAllByCompanyIdOrderByCreatedAtDesc(companyId).stream().filter(comment -> comment.getActivationStatus().equals(EActivationStatus.ACTIVATED)).toList();
        List<Comment> activeCommentsByCompany = commentRepository.findAllByCompanyIdAndActivationStatusOrderByCreatedAtDesc(companyId, EActivationStatus.ACTIVATED);
        if(activeCommentsByCompany.isEmpty()) return Collections.emptyList();
        List<GetPersonnelDetailsByCommentResponseModel> personnelDetailsResponseModel = getPersonnelDetailsFromPersonnelService(activeCommentsByCompany);
        return prepareResponseDtoForCommentsByCompanyFromModel(activeCommentsByCompany, personnelDetailsResponseModel);
    }

    private List<GetPersonnelDetailsByCommentResponseModel> getPersonnelDetailsFromPersonnelService(List<Comment> comments) {
        List<String> personnelList = comments.stream().map(Comment::getPersonnelId).toList();
        return getPersonnelDetailsByCommentRequestProducer.getPersonnelInfo(personnelList);
    }

    private List<GetAllCommentsByCompanyResponseDto> prepareResponseDtoForCommentsByCompanyFromModel(List<Comment> comments, List<GetPersonnelDetailsByCommentResponseModel> personnelDetailsResponseModel) {
        List<GetAllCommentsByCompanyResponseDto> responseDto = new ArrayList<>();
        for (int i = 0; i < personnelDetailsResponseModel.size(); i++) {
            responseDto.add(GetAllCommentsByCompanyResponseDto.builder()
                    .commentId(comments.get(i).getId())
                    .personnel(ICommentMapper.INSTANCE.personnelDetailsModelToDto(personnelDetailsResponseModel.get(i)))
                    .header(comments.get(i).getHeader())
                    .content(comments.get(i).getContent())
                    .creationDate(Instant.ofEpochMilli(comments.get(i).getCreatedAt()).atZone(ZoneId.systemDefault()).toLocalDate().toString())
                    .rating(comments.get(i).getRating())
                    .build());
        }
        return responseDto;
    }

    public List<GetAllPendingCommentsResponseModel> getAllPendingComments() {
        List<Comment> pendingComments = commentRepository.findAllByActivationStatusOrderByCreatedAt(EActivationStatus.PENDING);
        if(pendingComments.isEmpty()) return Collections.emptyList();
        List<GetPersonnelDetailsByCommentResponseModel> personnelDetailsResponseModel = getPersonnelDetailsFromPersonnelService(pendingComments);
        List<GetCompanyDetailsByCommentResponseModel> companyDetailsResponseModel = getCompanyDetailsFromCompanyService(pendingComments);
        return prepareResponseModelForPendingCommentsByCompanyFromModel(pendingComments, personnelDetailsResponseModel, companyDetailsResponseModel);
    }

    private List<GetCompanyDetailsByCommentResponseModel> getCompanyDetailsFromCompanyService(List<Comment> comments) {
        List<String> companies = comments.stream().map(Comment::getCompanyId).toList();
        return getCompanyDetailsByCommentRequestProducer.getCompanyInfo(companies);
    }

    private List<GetAllPendingCommentsResponseModel> prepareResponseModelForPendingCommentsByCompanyFromModel(List<Comment> comments, List<GetPersonnelDetailsByCommentResponseModel> personnelDetailsResponseModel, List<GetCompanyDetailsByCommentResponseModel> companyDetailsResponseModel) {
        List<GetAllPendingCommentsResponseModel> responseModel = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            responseModel.add(GetAllPendingCommentsResponseModel.builder()
                    .commentId(comments.get(i).getId())
                    .companyName(companyDetailsResponseModel.get(i).getCompanyName())
                    .companyLogo(companyDetailsResponseModel.get(i).getCompanyLogo())
                    .personnelName(personnelDetailsResponseModel.get(i).getName())
                    .personnelLastName(personnelDetailsResponseModel.get(i).getLastName())
                    .personnelGender(personnelDetailsResponseModel.get(i).getGender())
                    .personnelImage(personnelDetailsResponseModel.get(i).getImage())
//                    .personnelDateOfEmployment(personnelDetailsResponseModel.get(i).getDateOfEmployment())
                    .personnelDateOfEmployment(LocalDate.now().toString()) //// YORUMU GERİ AÇACAKSIN !!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                    .header(comments.get(i).getHeader())
                    .content(comments.get(i).getContent())
                    .rating(comments.get(i).getRating())
                    .creationDate(Instant.ofEpochMilli(comments.get(i).getCreatedAt()).atZone(ZoneId.systemDefault()).toLocalDate().toString())
                    .build());
        }
        return responseModel;
    }

    public void handleCommentDecision(CommentDecisionModel model) {
        Comment comment = findById(model.getCommentId());
        if(model.getDecision()) {
            activate(comment);
        } else {
            hardDelete(comment);
        }
    }

    private void activate(Comment comment) {
        comment.setActivationStatus(EActivationStatus.ACTIVATED);
        update(comment);
    }

    public Boolean softDeleteCommentById(String commentId) {
        Comment comment = findById(commentId);
        comment.setStatus(false);
        update(comment);
        return true;
    }
}
