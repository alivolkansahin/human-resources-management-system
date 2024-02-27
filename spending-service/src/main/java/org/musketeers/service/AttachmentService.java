package org.musketeers.service;

import org.musketeers.entity.Attachment;
import org.musketeers.repository.AttachmentRepository;
import org.musketeers.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class AttachmentService extends ServiceManager<Attachment, String> {

    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        super(attachmentRepository);
        this.attachmentRepository = attachmentRepository;
    }


}
