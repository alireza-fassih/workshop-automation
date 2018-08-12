package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.manager.AbstractManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public abstract class AbstractRestService<T, I extends Serializable> implements RestUtils {

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


}
