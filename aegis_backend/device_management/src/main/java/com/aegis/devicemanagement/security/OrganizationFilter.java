package com.aegis.devicemanagement.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class OrganizationFilter extends BasicAuthenticationFilter {


    private String claimLink;

    public OrganizationFilter(AuthenticationManager authenticationManager, String claimLink) {
        super(authenticationManager);
        this.claimLink = claimLink;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null)
            throw new ServletException("Invalid Request, No authorization");
        String token = authHeader.substring(authHeader.lastIndexOf(" ") + 1);
        DecodedJWT decodedJWT = JWT.decode(token);
        String userEmail = decodedJWT.getClaim(claimLink).asString();
        request.setAttribute("userEmail", userEmail);
        chain.doFilter(request, response);
    }

}