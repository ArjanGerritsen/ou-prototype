package nl.ou.se.rest.fuzzer.components.task;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.ou.se.rest.fuzzer.components.extractor.ExtractorTask;
import nl.ou.se.rest.fuzzer.components.fuzzer.executor.ExecutorTask;
import nl.ou.se.rest.fuzzer.components.fuzzer.generator.GeneratorTask;
import nl.ou.se.rest.fuzzer.components.shared.Constants;

@Service
public class TaskExecutionFactory {

	// variables
	private Map<String, Object> metaDataTuples = new HashMap<>();

	@Autowired
	private ExtractorTask extractorTask;

	@Autowired
	private GeneratorTask generatorTask;

	@Autowired
	private ExecutorTask executorTask;

	private String taskExecutionCanonicalName;

	private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	public TaskExecutionFactory create(String taskExecutionCanonicalName) {
		this.taskExecutionCanonicalName = taskExecutionCanonicalName;
		return this;
	}

	public TaskExecutionFactory setMetaDataTuples(Map<String, Object> metaDataTuples) {
		this.metaDataTuples = metaDataTuples;
		return this;
	}

	public TaskExecution build() {
		TaskExecution taskExecution = null;

		if (ExtractorTask.class.getCanonicalName().equals(taskExecutionCanonicalName)) {
			taskExecution = extractorTask;
		} else if (GeneratorTask.class.getCanonicalName().equals(taskExecutionCanonicalName)) {
			taskExecution = generatorTask;
		} else if (ExecutorTask.class.getCanonicalName().equals(taskExecutionCanonicalName)) {
			taskExecution = executorTask;
		} else {
			logger.error(String.format(Constants.ERROR_TASK_EXECUTION_FACTORY_UNKNOWN, taskExecutionCanonicalName));
			return null;
		}

		taskExecution.setMetaDataTuples(this.metaDataTuples);

		return taskExecution;
	}
}