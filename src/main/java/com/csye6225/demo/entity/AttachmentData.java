package com.csye6225.demo.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "attachmentdata")
public class AttachmentData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "attachid")
    private int attachId;

    @ManyToOne
    @PrimaryKeyJoinColumn
    private Task task;

    @Column(name = "attachments")
    @Lob
    private Byte[] attachName;

    @Column(name = "content")
    private String content;

    public int getAttachId() {
        return attachId;
    }

    public void setAttachId(int attachId) {
        this.attachId = attachId;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Byte[] getAttachName() {
        return attachName;
    }

    public void setAttachName(Byte[] attachName) {
        this.attachName = attachName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
