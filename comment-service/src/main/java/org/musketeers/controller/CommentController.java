package org.musketeers.controller;

import lombok.RequiredArgsConstructor;
import org.musketeers.dto.request.AddCommentRequestDto;
import org.musketeers.dto.request.RateCommentRequestDto;
import org.musketeers.dto.response.GetAllCommentsByCompanyResponseDto;
import org.musketeers.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.musketeers.constant.Endpoint.*;

@RestController
@RequestMapping(ROOT + COMMENT)
@RequiredArgsConstructor
@CrossOrigin
public class CommentController {

    private final CommentService commentService;

    @PostMapping(ADD)
    public ResponseEntity<Boolean> addComment(@RequestBody AddCommentRequestDto dto) {
        return ResponseEntity.ok(commentService.addComment(dto));
    }

    @GetMapping(GET_ALL_BY_COMPANY + "/{companyId}")
    public ResponseEntity<List<GetAllCommentsByCompanyResponseDto>> getAllComments(@PathVariable String companyId) {
        return ResponseEntity.ok(commentService.getAllCommentsByCompany(companyId));
    }

    @DeleteMapping(DELETE + "/{commentId}") // for admin, security implementation required
    public ResponseEntity<Boolean> softDeleteComment(@PathVariable String commentId) {
        return ResponseEntity.ok(commentService.softDeleteCommentById(commentId));
    }

}
