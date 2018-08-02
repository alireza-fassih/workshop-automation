package ir.fassih.workshopautomation.rest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import ir.fassih.workshopautomation.manager.AbstractManager;
import lombok.RequiredArgsConstructor;


@Slf4j
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public abstract class AbstractRestService<T, I extends Serializable> {

    protected final AbstractManager<T, I> manager;


    @PostMapping
    public void save(@RequestBody T entity) {
        manager.save(entity);
    }


    @DeleteMapping(path = "/{id}")
    public void delete(@PathParam("id") I id) {
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


    @GetMapping("/search")
    public Page<T> search(@RequestParam Map<String, String> params) {
        return manager.search( createSearchModel( params ) );
    }

    @GetMapping("/options")
    public Map<String,Object> options() {
        return optionsInternal();
    }


    protected Map<String, Object> optionsInternal() {
        return new HashMap<>();
    }

    protected SearchModel createSearchModel(Map<String, String> param) {
        int page = Integer.parseInt(param.get("page"));
        int pageSize = Integer.parseInt(param.get("pageSize"));

        param.remove("page");
        param.remove("pageSize");

        return SearchModel.builder()
                .filters(param).page(page).pageSize(pageSize)
                .build();
    }

}
