package ir.fassih.workshopautomation.dao;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel.SearchType;
import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.repository.report.AbstractReportModel.DoubleReportModel;
import ir.fassih.workshopautomation.rest.model.ReportOrderUnitByTime;
import ir.fassih.workshopautomation.rest.model.ReportOrderUnitByTime.DateRange;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.text.ParseException;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportDao {

    @FunctionalInterface
    private interface TriFunction<T, R, Y, H> {
        H apply(T t, R r, Y y);
    }

    private final EntityManager entityManager;



    private Map<DateRange, BiFunction<CriteriaBuilder, Root<OrderEntity>, Selection<DoubleReportModel>>>
        constructorMapper = new EnumMap<>(DateRange.class);


    private Map<DateRange, BiFunction<CriteriaBuilder, Root<OrderEntity>, Expression<?>[]>>
        groupByMapper = new EnumMap<>(DateRange.class);


    private Map<SearchType, TriFunction<CriteriaBuilder, Path<?>, String, Predicate>>
        searchTypeMapper = new EnumMap<>(SearchType.class);



    @PostConstruct
    public void init() {
        constructorMapper.put(DateRange.DAY,   this::constructForDay);
        constructorMapper.put(DateRange.MONTH, this::constructForMonth);

        groupByMapper.put(DateRange.DAY,   this::groupedByForDay);
        groupByMapper.put(DateRange.MONTH, this::groupedByForMonth);

        searchTypeMapper.put(SearchType.EQ, this::eqPredicateBuilder);
    }


    public List<DoubleReportModel> report(ReportOrderUnitByTime model) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<DoubleReportModel> query = cb.createQuery(DoubleReportModel.class);

        Root<OrderEntity> order = query.from(OrderEntity.class);

        Predicate[] predicates = model.getFilter().entrySet().stream()
            .map(e -> this.toPredicate(order, cb, e.getKey(), e.getValue()))
            .toArray(Predicate[]::new);

        query.select( constructorMapper.get(model.getRange()).apply(cb, order) )
            .where( predicates )
            .groupBy( groupByMapper.get(model.getRange()).apply(cb, order) );

        return entityManager.createQuery(query).getResultList();
    }


    private Predicate toPredicate(Root<OrderEntity> root, CriteriaBuilder cb, String key, String value) {
        int doubleColon = key.indexOf(':');
        SearchType type = SearchType.valueOf( key.substring(0, doubleColon) );
        Path<?> path = extractRoot(root, key.substring(doubleColon));
        return searchTypeMapper.get( type ).apply( cb, path, value );
    }


    private Predicate eqPredicateBuilder(CriteriaBuilder cb, Path<?> path, String value) {
        Class<?> javaType = path.getJavaType();
        Object realVal = convertValue(javaType, value);
        if(Boolean.class.isAssignableFrom(javaType) && Boolean.FALSE.equals(realVal) ) {
            return cb.or(
                cb.equal(path, realVal),
                cb.isNull(path)
            );
        } else {
            return cb.equal(path, realVal);
        }
    }


    private Object convertValue(Class<?> javaType, String value) {
        if (Date.class.isAssignableFrom(javaType)) {
            try {
                return  new StdDateFormat().parse(value);
            } catch (ParseException e) {
                return new Date(Long.parseLong(value));
            }
        } else if (Long.class.isAssignableFrom(javaType)) {
            return Long.parseLong(value);
        } else if (Boolean.class.isAssignableFrom(javaType)) {
            return Boolean.parseBoolean(value);
        }
        return value;
    }

    private Path<?> extractRoot(Root<OrderEntity> root, String field) {
        if (field.contains(".")) {
            String[] split = field.split("\\.");
            Join<?, ?> element = null;
            Path<?> path = null;
            for(int i = 0; i < split.length; i ++) {
                if( i == ( split.length - 1 ) ) {
                    path = element.get(split[i]);
                } else {
                    element = ( element == null ?
                        root.join( split[i]) :
                        element.join( split[i]) );
                }
            }
            return path;
        } else {
            return root.get( field );
        }
    }




    private Selection<DoubleReportModel> constructForDay(CriteriaBuilder cb, Root<OrderEntity> order) {
        return cb.construct(
            DoubleReportModel.class,
            cb.function("YEAR" , Integer.class, order.get("createDate")).alias("year"),
            cb.function("MONTH", Integer.class, order.get("createDate")).alias("month"),
            cb.function("DAY"  , Integer.class, order.get("createDate")).alias("day"),
            cb.sum(order.get("unit") ).alias("data") );
    }


    private Selection<DoubleReportModel> constructForMonth(CriteriaBuilder cb, Root<OrderEntity> order) {
        return cb.construct(
            DoubleReportModel.class,
            cb.function("YEAR" , Integer.class, order.get("createDate")).alias("year"),
            cb.function("MONTH", Integer.class, order.get("createDate")).alias("month"),
            cb.sum( order.get("unit") ).alias("data") );
    }




    private Expression<?>[] groupedByForDay(CriteriaBuilder cb, Root<OrderEntity> order) {
        return new Expression[]{
            cb.function("YEAR"  , Integer.class, order.get("createDate")),
            cb.function("MONTH" , Integer.class, order.get("createDate")),
            cb.function("DAY"   , Integer.class, order.get("createDate"))
        };
    }


    private Expression<?>[] groupedByForMonth(CriteriaBuilder cb, Root<OrderEntity> order) {
        return new Expression[]{
            cb.function("YEAR"  , Integer.class, order.get("createDate")),
            cb.function("MONTH" , Integer.class, order.get("createDate"))
        };
    }


}
