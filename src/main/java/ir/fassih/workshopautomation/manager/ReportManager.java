package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.repository.StateOfOrderRepository;
import ir.fassih.workshopautomation.repository.report.AbstractReportModel;
import ir.fassih.workshopautomation.repository.report.CountByTimeModel;
import ir.fassih.workshopautomation.rest.model.ReportByStateModel;
import ir.fassih.workshopautomation.rest.model.ReportOrderUnitByTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportManager {

    private final StateOfOrderRepository stateOfOrderRepository;
    private final OrderManager orderManager;



    @Transactional(readOnly = true)
    public List<CountByTimeModel<Long>> generateReportByStateAndUser(ReportByStateModel model) {
        if(model.getUser() != null) {
            return stateOfOrderRepository.reportByStateAndUser(model.getState(), model.getUser(), model.getStartDate(), model.getEndDate());
        } else {
            return stateOfOrderRepository.reportByState(model.getState(), model.getStartDate(), model.getEndDate());
        }
    }

    @Transactional(readOnly = true)
    public List<AbstractReportModel.DoubleReportModel> unitsByTime(ReportOrderUnitByTime model) {
        return orderManager.report(model);
    }
}
