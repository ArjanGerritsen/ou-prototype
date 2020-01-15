package nl.ou.se.fuzz.rest.service.fuzzing.jobs;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class JobsExecutor {

	@Async("JobExecutor")
	public void execute(Job job) {
		job.run();
	}
}