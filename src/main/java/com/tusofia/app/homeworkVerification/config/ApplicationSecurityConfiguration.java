package com.tusofia.app.homeworkVerification.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.cors().disable()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/js/**", "/css/**", "/images/**", "/changePassword").permitAll()
		.antMatchers("/", "/students/register", "/teachers/register", "/login")
		.anonymous()
		.anyRequest().authenticated()
		.and()
		.exceptionHandling().accessDeniedPage("/unauthorized")
		.and()
		.formLogin()
		.loginPage("/login")
		.usernameParameter("email")
		.passwordParameter("password")
		.defaultSuccessUrl("/home")
		.and()
		.logout()
		.logoutSuccessUrl("/");
	}
}