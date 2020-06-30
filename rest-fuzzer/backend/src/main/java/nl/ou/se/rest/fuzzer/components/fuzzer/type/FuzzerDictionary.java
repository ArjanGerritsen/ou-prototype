package nl.ou.se.rest.fuzzer.components.fuzzer.type;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import nl.ou.se.rest.fuzzer.components.data.fuz.dao.FuzDictionaryService;
import nl.ou.se.rest.fuzzer.components.data.fuz.dao.FuzRequestService;
import nl.ou.se.rest.fuzzer.components.data.fuz.dao.FuzResponseService;
import nl.ou.se.rest.fuzzer.components.data.fuz.domain.FuzDictionary;
import nl.ou.se.rest.fuzzer.components.data.fuz.domain.FuzProject;
import nl.ou.se.rest.fuzzer.components.data.fuz.domain.FuzRequest;
import nl.ou.se.rest.fuzzer.components.data.fuz.domain.FuzResponse;
import nl.ou.se.rest.fuzzer.components.data.rmd.dao.RmdActionService;
import nl.ou.se.rest.fuzzer.components.data.rmd.domain.RmdAction;
import nl.ou.se.rest.fuzzer.components.data.task.domain.Task;
import nl.ou.se.rest.fuzzer.components.fuzzer.executor.ExecutorUtil;
import nl.ou.se.rest.fuzzer.components.fuzzer.metadata.MetaDataUtil;
import nl.ou.se.rest.fuzzer.components.fuzzer.metadata.MetaDataUtil.Meta;
import nl.ou.se.rest.fuzzer.components.fuzzer.util.RequestUtil;

@Service
public class FuzzerDictionary extends FuzzerBase implements Fuzzer {

    // variable(s)
    private FuzProject project = null;
    private MetaDataUtil metaDataUtil = null;

    @Autowired
    private RmdActionService actionService;

    @Autowired
    private FuzRequestService requestService;

    @Autowired
    private FuzResponseService responseService;

    @Autowired
    private FuzDictionaryService dictionaryService;

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private ExecutorUtil executorUtil;

    // method(s)
    public void start(FuzProject project, Task task) {
        this.project = project;

        // authentication
        executorUtil.setAuthentication(metaDataUtil.getAuthentication());

        // get meta
        Integer repetitions = metaDataUtil.getIntegerValue(Meta.REPITITIONS);
        List<Integer> dictionaryIds = metaDataUtil.getIntegerArrayValues(Meta.DICTIONARIES);
        
        List<FuzDictionary> dictionaries = dictionaryService.findByIds(dictionaryIds);

        List<RmdAction> actions = actionService.findBySutId(this.project.getSut().getId());
        actions = metaDataUtil.getFilteredActions(actions);

        int count = 0;
        int total = repetitions * actions.size();

        // init requestUtil
        requestUtil.init(project, metaDataUtil.getDefaults(), dictionaries);

        for (int i = 0; i < repetitions; i++) {
            for (RmdAction a : actions) {
                FuzRequest request = requestUtil.getRequestFromAction(a, null, null);
                requestService.save(request);

                FuzResponse response = executorUtil.processRequest(request);
                responseService.save(response);

                count++;
                saveTaskProgress(task, count, total);
            }
        }
    }

    public Boolean isMetaDataValid(Map<String, Object> metaDataTuples) {
        this.metaDataUtil = new MetaDataUtil(metaDataTuples);
        return metaDataUtil.isValid(Meta.CONFIGURATION, Meta.REPITITIONS, Meta.DICTIONARIES);
    }
}