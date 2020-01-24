package nl.ou.se.rest.fuzzer.task;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import nl.ou.se.rest.fuzzer.data.task.dao.TaskService;
import nl.ou.se.rest.fuzzer.data.task.domain.Task;

@Service
public class TasksExecutor {

    @Autowired
    private TaskService taskService;

    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Async("TaskExecutor")
    public void run(Task task, TaskExecution execution) {

        task.setStartedAt(LocalDateTime.now());
        taskService.save(task);

        try {
            execution.execute();

            task.setFinishedAt(LocalDateTime.now());
            taskService.save(task);
        } catch (Exception e) {
            logger.error(e.getMessage());

            task.setCrashedAt(LocalDateTime.now());
            taskService.save(task);
        }
    }
}