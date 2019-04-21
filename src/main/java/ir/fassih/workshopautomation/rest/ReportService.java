package ir.fassih.workshopautomation.rest;


import ir.fassih.workshopautomation.manager.AbstractManager;
import ir.fassih.workshopautomation.manager.OrderStateManager;
import ir.fassih.workshopautomation.manager.ReportManager;
import ir.fassih.workshopautomation.manager.UserManager;
import ir.fassih.workshopautomation.repository.report.CountByTimeModel;
import ir.fassih.workshopautomation.rest.model.ReportByStateModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Secured("ADMIN")
@RestController
@RequestMapping("/rest/report")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportService implements RestUtils, ApplicationContextAware {


    private final ReportManager reportManager;

    private ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @GetMapping("/options")
    public Map<String, Object> options() {
        return optionsInternal();
    }


    @PostMapping("/byState")
    public List<CountByTimeModel<Long>> reportByState(@Valid @RequestBody ReportByStateModel model) {
        return reportManager.generateReportByStateAndUser(model);
    }


    protected Map<Class<? extends AbstractManager>, String> getOptionsMetadata() {
        Map<Class<? extends  AbstractManager>, String> map = new HashMap<>();
        map.put(UserManager.class, "users");
        map.put(OrderStateManager.class, "states");
        return map;
    }

    protected Map<String, Object> optionsInternal() {
        HashMap<String, Object> map = new HashMap<>();
        getOptionsMetadata().forEach((managerClass, path) -> {
            AbstractManager manager = applicationContext.getBean(managerClass);
            map.put(path, manager.loadOptions());
        });
        return map;
    }


}
