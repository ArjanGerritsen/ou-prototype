package nl.ou.se.rest.fuzzer.components.fuzzer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.ou.se.rest.fuzzer.components.data.rmd.domain.RmdAction;
import nl.ou.se.rest.fuzzer.components.data.rmd.domain.RmdActionDependency;

public class SequenceUtil {

    // variables
    private Logger logger = LoggerFactory.getLogger(SequenceUtil.class);

    private static final String SEPERATOR = ",";

    private List<RmdAction> actions = new ArrayList<>();
    private List<RmdActionDependency> dependencies = new ArrayList<>();

    private Map<Long, List<Long>> mappedDepencies = new HashMap<>();
    private Map<Long, RmdAction> mappedActions = new HashMap<>();

    public SequenceUtil(List<RmdAction> actions, List<RmdActionDependency> dependencies) {
        this.actions = actions;
        this.dependencies = dependencies;
        this.init();
    }

    // methods
    private void init() {
        dependencies.forEach(d -> {
            List<Long> dependsOnActionIds = new ArrayList<>();
            Long actionId = d.getAction().getId();
            if (this.mappedDepencies.containsKey(actionId)) {
                dependsOnActionIds = this.mappedDepencies.get(actionId);
            }
            dependsOnActionIds.add(d.getActionDependsOn().getId());
            this.mappedDepencies.put(actionId, dependsOnActionIds);
        });

        actions.forEach(a -> {
            mappedActions.put(a.getId(), a);
        });
    }

    public List<String> getValidSequences(Integer sequenceLength) {
        List<String> sequences = new ArrayList<String>();
        for (int sequence = 1; sequence <= sequenceLength; sequence++) {
            sequences = this.createNewSequences(sequences);
        }

        return sequences;
    }

    private List<String> createNewSequences(List<String> sequences) {
        List<String> newSequences = new ArrayList<String>();
        if (sequences.isEmpty()) {
            newSequences = this.actions.stream().map(a -> a.getId().toString()).filter(s -> satisfiesAllDependencies(s))
                    .collect(Collectors.toList());
        } else {
            for (String sequence : sequences) {
                for (RmdAction action : this.actions) {
                    String newSequence = sequence.concat(SEPERATOR + action.getId().toString());
                    if (satisfiesAllDependencies(newSequence)) {
                        newSequences.add(newSequence);
                    }
                }
            }
        }

        sequences.addAll(newSequences);

        return sequences;
    }

    private Boolean satisfiesAllDependencies(String sequence) {
        String[] actionStringIds = sequence.split(",");
        List<Long> actionIds = Arrays.asList(actionStringIds).stream().map(id -> Long.parseLong(id))
                .collect(Collectors.toList());

        // only check last item, the sequence -1 is already checked ant thus valid
        if (!satisfiesDependenciesForLastItem(actionIds)) {
            return false;
        }

        return true;
    }

    private Boolean satisfiesDependenciesForLastItem(List<Long> actionIds) {
        List<Long> requiredDependencies = new ArrayList<>();
        Long actionId = actionIds.get(actionIds.size() - 1);

        if (this.mappedDepencies.containsKey(actionId)) {
            requiredDependencies = this.mappedDepencies.get(actionId);
        }

        if (!requiredDependencies.isEmpty() && actionIds.size() > 1) {
            List<Long> dependenciesInSequence = actionIds.subList(0, actionIds.size() - 1);
            requiredDependencies.removeIf(d -> dependenciesInSequence.contains(d));
        }

        Boolean satisFiesDependencies = requiredDependencies == null || requiredDependencies.isEmpty();

        logger.info(String.format(
                "Last Action with id %s from sequence %s has dependencies %s and dependencies are satisfied: %s ",
                actionId, actionIds, requiredDependencies, satisFiesDependencies));

        return satisFiesDependencies;
    }

    public List<RmdAction> getActionsFromSequence(String sequence) {
        List<RmdAction> actions = new ArrayList<>();
        String[] actionIds = sequence.split(SEPERATOR);

        for (String id : actionIds) {
            actions.add(this.mappedActions.get(Long.valueOf(id)));
        }

        return actions;
    }
}