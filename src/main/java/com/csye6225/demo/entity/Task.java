package com.csye6225.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.action.internal.OrphanRemovalAction;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.awt.*;
import java.util.UUID;

@Entity
@Table(name = "task")
@OnDelete(action=OnDeleteAction.CASCADE)
public class Task {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Type(type = "uuid-char")
    @Column(name = "taskid")
    private UUID taskId;

    @Column(length = 4096, name = "description")
    private String desc;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @org.hibernate.annotations.ForeignKey(name="none")
    private Person person;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
