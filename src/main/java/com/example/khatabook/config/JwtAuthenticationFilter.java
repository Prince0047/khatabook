package com.example.khatabook.config;

import com.example.khatabook.service.JwtUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtUserDetailService userDetailService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get Header
        String requestTokenHeader = request.getHeader("Authorization");
        String username = null;
        String token = null;

        if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer ")) {
            token = requestTokenHeader.substring(7);

            try {
                username = jwtTokenUtil.getUsernameFromToken(token);
            } catch (Exception e) {
                e.printStackTrace();
            }

            UserDetails userDetails = userDetailService.loadUserByUsername(username);

            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

            } else {
                System.out.println("Error in JWT FIlter...");
            }
        }

        filterChain.doFilter(request, response);
    }
}
