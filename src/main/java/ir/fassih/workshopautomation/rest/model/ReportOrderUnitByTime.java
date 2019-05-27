package ir.fassih.workshopautomation.rest.model;

import ir.fassih.workshopautomation.entity.order.OrderEntity;
import ir.fassih.workshopautomation.repository.report.AbstractReportModel.DoubleReportModel;
import lombok.Data;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

@Data
public class ReportOrderUnitByTime extends AbstractReportParam<DoubleReportModel, OrderEntity>{

    private static final String DATE_FIELD = "createDate";

    public enum DateRange {
        DAY, MONTH
    }

    public ReportOrderUnitByTime() {
        super(DoubleReportModel.class);
    }

    private DateRange range = DateRange.DAY;

    @Override
    public Selection<DoubleReportModel> createConstructor(CriteriaBuilder cb, Root<OrderEntity> root) {
        if( range == DateRange.DAY ) {
            return cb.construct(
                DoubleReportModel.class,
                cb.function("YEAR", Integer.class, root.get(DATE_FIELD)).alias("year"),
                cb.function("MONTH", Integer.class, root.get(DATE_FIELD)).alias("month"),
                cb.function("DAY"  , Integer.class, root.get(DATE_FIELD)).alias("day"),
                cb.sum(root.get("unit")).alias("data"));
        } else {
            return cb.construct(
                DoubleReportModel.class,
                cb.function("YEAR", Integer.class, root.get(DATE_FIELD)).alias("year"),
                cb.function("MONTH", Integer.class, root.get(DATE_FIELD)).alias("month"),
                cb.sum(root.get("unit")).alias("data"));
        }
    }

    @Override
    public Expression<?>[] createGroupedBy(CriteriaBuilder cb, Root<OrderEntity> root) {
        if( range == DateRange.DAY ) {
            return new Expression[]{
                cb.function("YEAR"  , Integer.class, root.get( DATE_FIELD )),
                cb.function("MONTH" , Integer.class, root.get( DATE_FIELD )),
                cb.function("DAY"   , Integer.class, root.get( DATE_FIELD ))
            };
        } else {
            return new Expression[]{
                cb.function("YEAR"  , Integer.class, root.get( DATE_FIELD )),
                cb.function("MONTH" , Integer.class, root.get( DATE_FIELD ))
            };
        }
    }


}
