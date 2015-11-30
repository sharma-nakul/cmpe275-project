package edu.sjsu.cmpe275.project.dao;

import edu.sjsu.cmpe275.project.model.Task;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Naks
 *         Implementation class for Task DAO interface
 *         Repository annotation to mark the class as repository entity for a task.
 */
@Repository
public class TaskDaoImpl extends AbstractDao implements ITaskDao {
    /**
     * Variable of type logger to print data on console
     */
    private static final Logger logger = LoggerFactory.getLogger(TaskDaoImpl.class);
    /**
     * Object to hold the current session for hibernate connection
     */
    private Session session;

    @Override
    public long addTask(Task task) {
        session = getSession();
        session.save(task);
        session.flush();
        logger.info(task.getTitle() + " added successfully");
        return task.getId();
    }

    @Override
    public boolean updateTask(Task task) {
        try {
            Task currentTaskInfo = getTaskById(task.getId());
            Hibernate.initialize(currentTaskInfo.getAssignee());
            if (!task.getTitle().isEmpty())
                currentTaskInfo.setTitle(task.getTitle());
            if (!task.getDescription().isEmpty())
                currentTaskInfo.setDescription(task.getDescription());
            if (task.getActual() != currentTaskInfo.getActual())
                currentTaskInfo.setActual(task.getActual());
            if (task.getEstimate() != currentTaskInfo.getEstimate())
                currentTaskInfo.setEstimate(task.getEstimate());
            if (task.getAssignee().getId() != currentTaskInfo.getAssignee().getId())
                currentTaskInfo.setAssignee(task.getAssignee());
            session = getSession();
            session.update(currentTaskInfo);
            session.flush();
            logger.info(task.getTitle() + " updated successfully");
            return true;
        } catch (HibernateException e) {
            logger.info("Hibernate Exception: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteTaskById(long taskId) {
        try {
            session = getSession();
            Task task = getTaskById(taskId);
            session.delete(task);
            session.flush();
            logger.info(task.getTitle() + " deleted successfully");
            return true;
        } catch (HibernateException e) {
            logger.info("Hibernate Exception: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Task> getTaskByProjectId(long projectId) {
        session = getSession();
        Criteria taskByProjectId = session.createCriteria(Task.class).add(Restrictions.eq("project.id", projectId));
        List<Task> taskListOfProject = (ArrayList<Task>) taskByProjectId.list();
        if (taskListOfProject.size() > 0)
            logger.info("There is at least one task available for project id " + projectId);
        else
            logger.info("Task list is empty for project id " + projectId);
        return taskListOfProject;
    }

    @Override
    public Task getTaskById(long taskId) {
        session = getSession();
        Task currentTask = (Task) session.get(Task.class, taskId);
        if (currentTask != null)
            logger.info("Task exist of id " + taskId);
        else
            logger.info("There is no task of id " + taskId);
        return currentTask;
    }
}
