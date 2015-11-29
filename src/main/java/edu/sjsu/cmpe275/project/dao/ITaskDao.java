package edu.sjsu.cmpe275.project.dao;

import edu.sjsu.cmpe275.project.model.Task;

import java.util.List;

/**
 * @author Naks
 * Task Dao interface to perform database operations
 */
public interface ITaskDao {
    long addTask(Task task);
    boolean updateTask (Task Task);
    boolean deleteTaskById (long taskId);
    List<Task> getTaskByProjectId (long projectId);
    Task getTaskById (long taskId);
}