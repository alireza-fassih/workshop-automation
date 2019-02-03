package ir.fassih.workshopautomation.rest;

import ir.fassih.workshopautomation.entity.pricelist.PriceListEntity;
import ir.fassih.workshopautomation.entity.user.UserEntity;
import ir.fassih.workshopautomation.manager.NotificationManager;
import ir.fassih.workshopautomation.manager.OrderManager;
import ir.fassih.workshopautomation.manager.StateOfOrderManager;
import ir.fassih.workshopautomation.manager.UserManager;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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

    @Autowired
    UserManager userManager;

    @GetMapping("/price-list")
    public void getPriceList(HttpServletResponse response) {
        UserEntity userEntity = userManager.loadCurrentUser();
        Optional.ofNullable(userEntity.getPriceList())
            .map(PriceListEntity::getContent)
                .ifPresent( bytes -> {
                    try {
                        generateFile(bytes, response);
                    } catch (IOException e) {
                        log.error("cannot send file To Client" , e);
                    }
                });
    }

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


    void setHeadersOnRequest(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("text/csv; charset=UTF-8");
    }


    void generateFile(byte[] content, HttpServletResponse response) throws IOException {
        setHeadersOnRequest(response, "priceList.pdf");
        try (ServletOutputStream p =  response.getOutputStream()) {
            p.write(content);
            p.flush();
        }
    }

}
