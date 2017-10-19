package com.csye6225.demo.dao;

/**
 * <Niravra Kar>, <001252638>, <kar.n@husky.neu.edu>
 * <Sumedh Saraf>, <001267030>, <saraf.s@husky.neu.edu>
 * <Parakh Mahajan>, <001236045>, <mahajan.pa@husky.neu.edu>
 * <Ashwini Thaokar>, <001282202>, <thaokar.a@husky.neu.edu>
 **/

import com.csye6225.demo.entity.AttachmentData;
import com.csye6225.demo.entity.Person;
import org.springframework.data.repository.CrudRepository;
import com.csye6225.demo.entity.Task;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

public interface TaskDao extends CrudRepository<Task, Long>, Repository<Task,Long> {

//    List<Task> findByuser(int userId);
    List<Task> findByPerson(Person person);

    List<Task> findByTaskId(UUID taskId);


    @Override
    void delete(Task deleted);

    Task save(Task persisted);
}
