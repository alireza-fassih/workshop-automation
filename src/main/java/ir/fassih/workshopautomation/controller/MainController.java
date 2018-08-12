package ir.fassih.workshopautomation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {



    @GetMapping("/dashboard")
    public String getDashboardView() {
        return "dashboard-view";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login-view";
    }

}
