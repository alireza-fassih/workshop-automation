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
		http
			.authorizeRequests()
				.antMatchers("/rest/**", "/dashboard/**").hasAuthority("USER")
				.and()
			.formLogin()
				.loginPage("/login").loginProcessingUrl("/login").failureUrl("/login?login_error=1")
				.defaultSuccessUrl("/dashboard")
			.usernameParameter("username").passwordParameter("password");
	}

	@Autowired
	public void configGlobal(AuthenticationManagerBuilder builder, UserManager userManager) throws Exception {
		builder.userDetailsService(userManager).passwordEncoder(passwordEncoder());
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
	