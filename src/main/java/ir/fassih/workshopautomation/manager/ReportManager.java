package ir.fassih.workshopautomation.manager;

import ir.fassih.workshopautomation.repository.StateOfOrderRepository;
import ir.fassih.workshopautomation.repository.report.CountByTimeModel;
import ir.fassih.workshopautomation.rest.model.ReportByStateModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportManager {

    private final StateOfOrderRepository stateOfOrderRepository;


    @Transactional
    public List<CountByTimeModel> generateReportByStateAndUser(ReportByStateModel model) {
        if(model.getUser() != null) {
            return stateOfOrderRepository.reportByStateAndUser(model.getState(), model.getUser(), model.getStartDate(), model.getEndDate());
        } else {
            return stateOfOrderRepository.reportByState(model.getState(), model.getStartDate(), model.getEndDate());
        }
    }

}
