package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.manager.NotificationManager;
import ir.fassih.workshopautomation.manager.OrderManager;
import ir.fassih.workshopautomation.manager.StateOfOrderManager;
import lombok.Data;
import lombok.Value;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/rest/customer")
public class CostumerService {

    @Autowired
    NotificationManager notificationManager;

    @Autowired
    OrderManager orderManager;

    @Autowired
    StateOfOrderManager stateOfOrderManager;

    @Autowired
    ModelMapper mapper;

    @GetMapping("/roles")
    public UserDto getRoles() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return new UserDto(auth.getName(),
            auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }

    @GetMapping("/news")
    public Map<String, Object> getNews() {
        Map<String, Object> news = new HashMap<>();
        news.put("notifications", notificationManager.getTop5());
        news.put("orderCount", orderManager.loadCountOfUserOrder());
        news.put("lastState", stateOfOrderManager.loadLastSateOfUser().stream()
            .map( s -> mapper.map( s, StateOfOrderDto.class ) ).collect(Collectors.toList()) );
        return news;
    }

    @Value
    public static class UserDto {
        String name;
        List<String> roles;
    }

    @Data
    public static class StateOfOrderDto {
        protected Long id;
        private String orderTitle;
        private Long orderId;
        private String stateTitle;
        private Date createDate;
    }


}
