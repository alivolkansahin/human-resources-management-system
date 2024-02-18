package org.musketeers.repository;

import org.musketeers.entity.Comment;
import org.musketeers.entity.enums.EActivationStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment,String> {

    List<Comment> findAllByCompanyIdOrderByCreatedAtDesc(String companyId);
    List<Comment> findAllByCompanyIdAndActivationStatusOrderByCreatedAtDesc(String companyId, EActivationStatus activationStatus);
    List<Comment> findAllByActivationStatusOrderByCreatedAt(EActivationStatus eActivationStatus);
}
