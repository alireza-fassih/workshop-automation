package ir.fassih.workshopautomation.rest.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class AbstractReportParam< C, E > {

    private final Class<C> constructType;

    private final Map<String, String> filters = new LinkedHashMap<>();



    @JsonAnySetter
    public void handleUnknown(String key, Object value) {
        if( value != null ) {
            filters.put( key, value.toString() );
        }
    }


    public abstract Selection< C > createConstructor(CriteriaBuilder cb, Root< E > root);

    public abstract Expression<?>[] createGroupedBy(CriteriaBuilder cb, Root< E > root);


}
