package com.cyan.Gabriel.model;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenFilter extends GenericFilterBean {

    public static final String SECRET_KEY = "banana";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String header = req.getHeader("Authorization");

        if (header == null) {
            throw new ServletException("Token is null 1");
        }

        if (!header.startsWith("Bearer ")){
            throw new ServletException("Token is in a bad format 1");
        }

        String token = header.substring(7);

        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            throw new ServletException("Invalid Token 1");
        }

        chain.doFilter(request, response);
    }

    public String getLogin(String auth) throws ServletException {
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new ServletException("Token is null or in a bad format 2");
        }
        String token = auth.substring(7);
        String result;
        try {
            result = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            throw new ServletException("Invalid Token 2");
        }
        return result;
    }


}
