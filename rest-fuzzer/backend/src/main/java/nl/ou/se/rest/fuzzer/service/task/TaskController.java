package nl.ou.se.rest.fuzzer.service.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nl.ou.se.rest.fuzzer.data.task.dao.TaskService;
import nl.ou.se.rest.fuzzer.data.task.domain.Task;
import nl.ou.se.rest.fuzzer.extractor.ExtractorTask;

@RestController()
@RequestMapping("/rest/tasks")
public class TaskController {

    private static final String EXTRACTOR = "extractor";
    private static final int MAX_TASKS_ENDED = 10;

    @Autowired
	TaskService taskSerivce;

    @RequestMapping(path = "progress", method = RequestMethod.GET)
    public @ResponseBody List<TaskDto> findAllProgress() {
        List<Task> tasks = new ArrayList<>();

        tasks = taskSerivce.findQueued();
        tasks.addAll(taskSerivce.findRunning());
        tasks.addAll(taskSerivce.findEnded(PageRequest.of(0, MAX_TASKS_ENDED)));

        return TaskMapper.toDtos(tasks);
    }

    @RequestMapping(path = "/{name}/start", method = RequestMethod.POST)
    public @ResponseBody TaskDto addExtractorTask(@PathVariable(value = "name") String name, @RequestBody Map<String, Object> metaDataTuples) {
        Task task = null;

        switch (name) {
        case EXTRACTOR:
            task = new Task(ExtractorTask.class.getCanonicalName());           
            break;
        default:
            break;
        }

        task.setMetaDataTuples(metaDataTuples);
        taskSerivce.save(task);
        return TaskMapper.toDto(task);
    }
}