package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel;
import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel.SearchType;
import ir.fassih.workshopautomation.entity.core.Traceable;
import ir.fassih.workshopautomation.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractManager<T, I extends Serializable> {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected final AbstractRepository<T, I> repository;

    @Transactional
    public void save(T entity) {
        if (entity instanceof Traceable) {
            Traceable traceable = (Traceable) entity;
            if (traceable.getCreateDate() == null) {
                traceable.setCreateDate(new Date());
            }
        }
        repository.save(entity);
    }

    @Transactional
    public Iterable<T> saveAll(Iterable<T> entities) {
        return repository.save(entities);
    }

    @Transactional
    public void delete(I id) {
        repository.delete(id);
    }



    @Transactional(readOnly = true)
    public Iterable<T> loadAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public T find(I id) {
        return repository.findOne(id);
    }

    @Transactional
    public void update(I id, T entity) {
        T db = repository.findOne(id);
        BeanUtils.copyProperties(entity, db, ignoreFieldWhenUpdate());
        if (db instanceof Traceable) {
            Traceable traceable = (Traceable) db;
            traceable.setLastModificationDate(new Date());
        }
        save(db);
    }

    protected String[] ignoreFieldWhenUpdate() {
        return new String[]{};
    }


    @Transactional(readOnly = true)
    public Page<T> search(SearchModel model) {
        Specification<T> specification = createSpecification(model);
        return repository.findAll(specification, new PageRequest(model.getPage(), model.getPageSize()));
    }

    private Specification<T> createSpecification(SearchModel model) {
        return (root, query, builder) -> {
            return createPredicate(root, query, builder, model);
        };
    }

    private Predicate createPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder,
                                      SearchModel model) {
        List<Predicate> predicates = new ArrayList<>();
        model.getFilters().forEach((key, value) -> {

            String field = null;
            SearchType searchType = null;

            for (SearchModel.SearchType type : SearchModel.SearchType.values()) {
                String prefix = type.getPrefix();
                if (key.startsWith(prefix)) {
                    field = key.replace(prefix, "");
                    searchType = type;
                    break;
                }
            }

            if (searchType == null) {
                throw new IllegalStateException();
            }

            Path<Object> element = null;
            if (field.contains(".")) {
                for (String path : field.split("\\.")) {
                    element = ( element == null ? root.get( path ) : element.get( path ) );
                }
            } else {
                element = root.get(field);
            }

            Object realVal = convertValue(element.getJavaType(), value);

            if (searchType == SearchType.EQ) {
                predicates.add(builder.equal(element, realVal));
            } else if (searchType == SearchType.LIKE) {
                builder.like(root.get(field), realVal.toString());
            }
        });
        return builder.and(predicates.toArray(new Predicate[]{}));
    }

    @Transactional(readOnly = true)
    public List<T> loadNotDeletes() {
        return repository.findAll(
            (root, query, builder) ->
                builder.or(builder.notEqual(root.get("deleted"), Boolean.TRUE), builder.isNull(root.get("deleted")))
        );
    }

    private Object convertValue(Class<?> javaType, String value) {
        if (Date.class.isAssignableFrom(javaType)) {
            return new Date(Long.parseLong(value));
        } else if (Long.class.isAssignableFrom(javaType)) {
            return Long.parseLong(value);
        } else if (Boolean.class.isAssignableFrom(javaType)) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }

}
