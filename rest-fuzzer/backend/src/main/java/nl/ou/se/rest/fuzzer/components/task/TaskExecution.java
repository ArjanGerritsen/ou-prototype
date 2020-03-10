package nl.ou.se.rest.fuzzer.components.task;

import java.util.Map;

public interface TaskExecution {

	public void execute();

	public void setMetaDataTuples(Map<String, Object> metaDataTuples);

	public Object getMetaDataValue(String key);

}