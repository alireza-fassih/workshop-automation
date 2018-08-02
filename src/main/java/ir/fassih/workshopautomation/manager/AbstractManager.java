package ir.fassih.workshopautomation.manager;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel;
import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel.SearchType;
import ir.fassih.workshopautomation.repository.AbstractRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractManager<T, I extends Serializable> {

    protected Logger log = LoggerFactory.getLogger(getClass());

    protected final AbstractRepository<T, I> repository;

    @Transactional
    public void save(T entity) {
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
    public T find(I id) {
        return repository.findOne(id);
    }

    @Transactional
    public void update(I id, T entity) {
        T db = repository.findOne(id);
        BeanUtils.copyProperties(entity, db, ignoreFieldWhenUpdate());
        save( db );
    }

    protected String[] ignoreFieldWhenUpdate() {
        return new String[] { } ;
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

            Path<Object> element = root.get(field);
            Object realVal = convertValue(element.getJavaType(), value);

            if (searchType == SearchType.EQ) {
                predicates.add(builder.equal(element, realVal));
            }
        });
        return builder.and(predicates.toArray(new Predicate[]{}));
    }

    private Date convertToDate(String value) {
        return new Date( Long.parseLong( value ) );
    }

    private Object convertValue(Class<?> javaType, String value) {
        if (Date.class.isAssignableFrom(javaType)) {
            return convertToDate(value);
        } else if (Long.class.isAssignableFrom(javaType)) {
            return Long.parseLong(value);
        }
        return value;
    }

}
