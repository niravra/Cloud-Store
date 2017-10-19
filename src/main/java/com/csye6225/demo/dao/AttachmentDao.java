package com.csye6225.demo.dao;

import com.csye6225.demo.entity.AttachmentData;
import com.csye6225.demo.entity.Task;
import com.sun.mail.imap.protocol.ID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.core.CrudMethods;

import java.util.List;
import java.util.UUID;

public interface AttachmentDao extends CrudRepository<AttachmentData,Long> {

      List<AttachmentData> findByTask(Task task);

      List<AttachmentData> findByAttachId(UUID attachId);

      List<AttachmentData> findAttachmentDataByTask(Task task);
      //@Override
      //void delete(AttachmentData deleted);

      //AttachmentData save(AttachmentData persisted);
}
