package ir.fassih.workshopautomation.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import ir.fassih.workshopautomation.manager.UserManager;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/rest/**", "/dashboard", "/dashboard/**").authenticated()
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login")
                .and()
                .formLogin().permitAll()
                .loginPage("/login").loginProcessingUrl("/login")
                .failureUrl("/login")
                .defaultSuccessUrl("/dashboard")
                .usernameParameter("username").passwordParameter("password");
    }

    @Autowired
    public void configGlobal(AuthenticationManagerBuilder builder, UserManager userManager,
                             BCryptPasswordEncoder encoder) throws Exception {
        builder.userDetailsService(userManager).passwordEncoder(encoder);
    }

}
	