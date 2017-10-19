package com.csye6225.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "attachmentdata")
public class AttachmentData {


    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(name = "attachid")
    private UUID attachId;

    @ManyToOne
    @Cascade({CascadeType.REMOVE})
    @org.hibernate.annotations.ForeignKey(name="none")
    @PrimaryKeyJoinColumn
    private Task task;

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
