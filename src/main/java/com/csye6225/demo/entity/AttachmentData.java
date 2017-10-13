package com.csye6225.demo.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "attachmentdata")
public class AttachmentData {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "attachid")
    private UUID attachId;

    @ManyToOne(cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn
    private Task task;


    public byte[] getAttachName() {
        return attachName;
    }

    public void setAttachName(byte[] attachName) {
        this.attachName = attachName;
    }

    @Column(name = "attachments")
    @Lob
    private byte[] attachName;

    @Column(name = "content")
    private String content;

    public UUID getAttachId() {
        return attachId;
    }

    public void setAttachId(UUID attachId) {
        this.attachId = attachId;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }



    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
