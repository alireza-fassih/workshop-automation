package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.core.datamanagment.model.SearchModel;

import java.util.Map;
import java.util.Optional;

public interface RestUtils {

    default SearchModel createSearchModel(Map<String, String> param) {
        int page = Integer.parseInt(Optional.ofNullable(param.get("page")).orElse("0"));
        int pageSize = Integer.parseInt(Optional.ofNullable(param.get("pageSize")).orElse("20"));

        param.remove("page");
        param.remove("pageSize");

        return SearchModel.builder()
                .filters(param).page(page).pageSize(pageSize)
                .build();
    }

}

