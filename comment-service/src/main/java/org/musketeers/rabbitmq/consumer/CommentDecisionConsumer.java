package org.musketeers.rabbitmq.consumer;

import lombok.RequiredArgsConstructor;
import org.musketeers.rabbitmq.model.CommentDecisionModel;
import org.musketeers.service.CommentService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDecisionConsumer {

    private final CommentService commentService;

    @RabbitListener(queues = "${comment-service-config.rabbitmq.admin-handle-comment-decision-queue}")
    public void handleCommentDecision(CommentDecisionModel model){
        commentService.handleCommentDecision(model);
    }

}
