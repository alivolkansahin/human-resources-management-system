package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.exception.CommentServiceException;
import org.musketeers.rabbitmq.model.GetAllPendingCommentsResponseModel;
import org.musketeers.service.CommentService;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllPendingCommentsRequestConsumer {

    private final CommentService commentService;

    @RabbitListener(queues = "${comment-service-config.rabbitmq.admin-get-all-pending-comments-comment-queue}")
    public List<GetAllPendingCommentsResponseModel> getAllPendingComments() {
        try {
            return commentService.getAllPendingComments();
        } catch (CommentServiceException ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }

}
