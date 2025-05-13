package com.diplom.analytics.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        if (request.getRequestURI().startsWith("/tracker/")) {
            String clientIdHeader = request.getHeader("X-Client-ID");
            String origin = request.getHeader("Origin");

            if (clientIdHeader == null || origin == null) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Missing client ID or origin");
                return;
            }

            try {
                Long userId = Long.parseLong(clientIdHeader);
                User user = userService.getUserById(userId);

                if (user == null || !normalizeUrl(user.getUrl()).equals(normalizeUrl(origin))) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid client ID or domain");
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid client ID format");
                return;
            }
        }

        String header = request.getHeader("Authorization");
        String token;
        String username = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            username = jwtUtil.extractUsername(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private String normalizeUrl(String url) {
        return url.replaceAll("/+$", "").toLowerCase();
    }
}