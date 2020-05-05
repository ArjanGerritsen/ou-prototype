package nl.ou.se.rest.fuzzer.components.service.rmd;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import nl.ou.se.rest.fuzzer.components.data.rmd.dao.RmdActionDependencyService;
import nl.ou.se.rest.fuzzer.components.data.rmd.dao.RmdActionService;
import nl.ou.se.rest.fuzzer.components.data.rmd.dao.RmdParameterService;
import nl.ou.se.rest.fuzzer.components.data.rmd.domain.DiscoveryModus;
import nl.ou.se.rest.fuzzer.components.data.rmd.domain.HttpMethod;
import nl.ou.se.rest.fuzzer.components.data.rmd.domain.RmdAction;
import nl.ou.se.rest.fuzzer.components.data.rmd.domain.RmdActionDependency;
import nl.ou.se.rest.fuzzer.components.data.rmd.domain.RmdParameter;
import nl.ou.se.rest.fuzzer.components.data.rmd.factory.RmdActionDependencyFactory;
import nl.ou.se.rest.fuzzer.components.service.rmd.mapper.RmdActionDependencyMapper;
import nl.ou.se.rest.fuzzer.components.service.rmd.mapper.RmdActionMapper;
import nl.ou.se.rest.fuzzer.components.service.rmd.mapper.RmdParameterMapper;
import nl.ou.se.rest.fuzzer.components.service.util.ValidatorUtil;
import nl.ou.se.rest.fuzzer.components.shared.Constants;
import nl.ou.se.rest.fuzzer.components.shared.FilterUtil;

@RestController()
@RequestMapping("/rest/suts/{id}/actions")
public class RmdSutActionsController {

    // variables
    private Logger logger = LoggerFactory.getLogger(RmdSutActionsController.class);

    @Autowired
    private RmdActionService actionService;

    @Autowired
    private RmdParameterService parameterService;

    @Autowired
    private RmdActionDependencyService actionDependencyService;

    // methods
    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> findAllActionsBySutId(@PathVariable(name = "id") Long id,
            @RequestParam(name = "filter", required = false) String path) {
        return ResponseEntity.ok(RmdActionMapper.toDtos(actionService.findBySutId(id)));
    }

    @RequestMapping(path = "{actionId}/parameters", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> findAllParametersBySutIdAndActionId(@PathVariable(name = "id") Long id,
            @PathVariable(name = "actionId") Long actionId) {
        return ResponseEntity.ok(RmdParameterMapper.toDtos(parameterService.findByActionId(actionId)));
    }

    @RequestMapping(path = "count", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> countActionsBySutId(@PathVariable(name = "id") Long id,
            @RequestParam(name = "filter", required = false) String filter) {

        List<HttpMethod> httpMethods = FilterUtil.getHttpMethods(filter);
        String path = FilterUtil.getValueFromFilter(filter, FilterUtil.PATH);

        return ResponseEntity.ok(actionService.countByFilter(id, httpMethods, FilterUtil.toLike(path)));
    }

    @RequestMapping(path = "paginated", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> findActionsBySutId(@PathVariable(name = "id") Long id,
            @RequestParam(name = "curPage") int curPage, @RequestParam(name = "perPage") int perPage,
            @RequestParam(name = "filter", required = false) String filter) {

        List<HttpMethod> httpMethods = FilterUtil.getHttpMethods(filter);
        String path = FilterUtil.getValueFromFilter(filter, FilterUtil.PATH);

        return ResponseEntity.ok(RmdActionMapper.toDtos(actionService.findByFilter(id, httpMethods,
                FilterUtil.toLike(path), FilterUtil.toPageRequest(curPage, perPage))));
    }

    @RequestMapping(path = "dependencies", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> addActionDepdency(@RequestBody Map<String, Long> parameters) {
        Optional<RmdParameter> parameter = parameterService
                .findById(parameters.get("parameter") == null ? -1 : parameters.get("parameter"));

        if (!parameter.isPresent()) {
            logger.warn(String.format(Constants.Service.VALIDATION_OBJECT_NOT_FOUND, RmdParameter.class,
                    parameters.get("parameterId")));
        }

        Optional<RmdAction> action = actionService
                .findById(parameters.get("action") == null ? -1 : parameters.get("action"));

        if (!action.isPresent()) {
            logger.warn(String.format(Constants.Service.VALIDATION_OBJECT_NOT_FOUND, RmdAction.class,
                    parameters.get("actionId")));
        }

        Optional<RmdAction> actionDependsOn = actionService
                .findById(parameters.get("actionDependsOn") == null ? -1 : parameters.get("actionDependsOn"));

        if (!actionDependsOn.isPresent()) {
            logger.warn(String.format(Constants.Service.VALIDATION_OBJECT_NOT_FOUND, RmdAction.class,
                    parameters.get("actionDependsOnId")));
        }

        Optional<RmdParameter> parameterDependsOn = parameterService
                .findById(parameters.get("parameterDependsOnId") == null ? -1 : parameters.get("parameterDependsOnId"));

        if (!parameterDependsOn.isPresent()) {
            logger.warn(String.format(Constants.Service.VALIDATION_OBJECT_NOT_FOUND, RmdParameter.class,
                    parameters.get("parameterDependsOnId")));
        }

        RmdActionDependency actionDependency = new RmdActionDependencyFactory()
                .create(DiscoveryModus.MANUAL, action.orElse(null), parameter.orElse(null),
                        actionDependsOn.orElse(null), parameterDependsOn.orElse(null))
                .build();

        List<String> violations = ValidatorUtil.getViolations(actionDependency);
        if (!violations.isEmpty()) {
            logger.warn(String.format(Constants.Service.VALIDATION_OBJECT_FAILED, RmdActionDependency.class,
                    violations.size()));
            return ValidatorUtil.getResponseForViolations(violations);
        }

        actionDependency = actionDependencyService.save(actionDependency);
        return ResponseEntity.ok(RmdActionDependencyMapper.toDto(actionDependency));
    }

    @RequestMapping(path = "dependencies/count", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> countActionsDependenciesBySutId(@PathVariable(name = "id") Long id,
            @RequestParam(name = "filter", required = false) String filter) {

        List<DiscoveryModus> discoveryModes = FilterUtil.getDiscoveryModes(filter);
        List<HttpMethod> httpMethods = FilterUtil.getHttpMethods(filter);
        String path = FilterUtil.getValueFromFilter(filter, FilterUtil.PATH);

        return ResponseEntity
                .ok(actionDependencyService.countByFilter(id, discoveryModes, httpMethods, FilterUtil.toLike(path)));
    }

    @RequestMapping(path = "dependencies/paginated", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<?> findActionsDependenciesByFilter(@PathVariable(name = "id") Long id,
            @RequestParam(name = "curPage") int curPage, @RequestParam(name = "perPage") int perPage,
            @RequestParam(name = "filter", required = false) String filter) {

        List<DiscoveryModus> discoveryModes = FilterUtil.getDiscoveryModes(filter);
        List<HttpMethod> httpMethods = FilterUtil.getHttpMethods(filter);
        String path = FilterUtil.getValueFromFilter(filter, FilterUtil.PATH);

        return ResponseEntity.ok(RmdActionDependencyMapper.toDtos(actionDependencyService.findByFilter(id,
                discoveryModes, httpMethods, FilterUtil.toLike(path), FilterUtil.toPageRequest(curPage, perPage))));
    }
}