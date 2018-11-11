package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.manager.AbstractManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
public abstract class AbstractRestService<T, I extends Serializable>
        implements RestUtils, ApplicationContextAware, ExcelGenerator {

    protected final AbstractManager<T, I> manager;
    private ApplicationContext applicationContext;

    @PostMapping
    public void save(@RequestBody T entity) {
        manager.save(entity);
    }


    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") I id) {
        manager.delete(id);
    }


    @GetMapping("/{id}")
    public T get(@PathVariable("id") I id) {
        return manager.find(id);
    }


    @PutMapping("/{id}")
    public void update(@PathVariable("id") I id, @RequestBody T entity) {
        manager.update(id, entity);
    }


    @PostMapping("/restore/{id}")
    public void restore(@PathVariable("id") I id) {
        manager.restore(id);
    }

    @GetMapping("/search")
    public Page<T> search(@RequestParam Map<String, String> params) {
        return manager.search(createSearchModel(params));
    }

    @GetMapping("/reportExcelReport")
    public void makeExcelReport(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {
        List<List<String>> entities = manager.findAll(createSearchModel(params)).stream().map(this::convertToRaw)
                .collect(Collectors.toList());
        generateExcelFile(getEntityName(), entities, response);
    }

    protected String getEntityName() {
        return "";
    }

    protected List<String> convertToRaw(T entity) {
        return new ArrayList<>();
    }


    @GetMapping("/options")
    public Map<String, Object> options() {
        return optionsInternal();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    protected Map<Class<? extends AbstractManager>, String> getOptionsMetadata() {
        return new HashMap<>();
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
