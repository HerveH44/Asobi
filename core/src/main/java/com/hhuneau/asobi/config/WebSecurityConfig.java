package com.hhuneau.asobi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Refactor login form
//            // See https://jira.springsource.org/browse/SPR-11496
//            .headers().addHeaderWriter(
//            new XFrameOptionsHeaderWriter(
//                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)).and()
//
//            .formLogin()
//            .defaultSuccessUrl("/index.html")
//            .loginPage("/login.html")
//            .failureUrl("/login.html?error")
//            .permitAll()
//            .and()
//            .logout()
//            .logoutSuccessUrl("/login.html?logout")
//            .logoutUrl("/logout.html")
//            .permitAll()
//            .and()
            .authorizeRequests()
            .antMatchers("/static/**").permitAll()
            .antMatchers("/webjars/**").permitAll()
            .antMatchers("/stomp/**").permitAll()
        .and()
        .authorizeRequests().anyRequest().authenticated();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("fabrice").password("fab123").roles("USER").and()
            .withUser("login").password("password").roles("USER").and()
            .withUser("paulson").password("bond").roles("ADMIN","USER");
    }
}
