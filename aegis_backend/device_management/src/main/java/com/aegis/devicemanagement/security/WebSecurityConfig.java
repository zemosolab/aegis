package com.aegis.devicemanagement.security;

import com.auth0.spring.security.api.JwtWebSecurityConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    @Value(value = "${auth0.apiAudience}")
    private String apiAudience;
    @Value(value = "${auth0.issuer}")
    private String issuer;
    @Value(value = "${auth0.token.claimLink}")
    private String claimLink;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JwtWebSecurityConfigurer
                .forRS256(apiAudience, issuer)
                .configure(http)
                .cors().and().csrf().disable().authorizeRequests()
                .antMatchers(
                        "/v2/api-docs",
                        "/swagger-resources",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources/configuration/security")
                .permitAll()
                .antMatchers("/h2-console/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/devices/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/devices/**")
                .permitAll()
                .antMatchers(HttpMethod.GET,"/api/health**").permitAll();

    }

}
