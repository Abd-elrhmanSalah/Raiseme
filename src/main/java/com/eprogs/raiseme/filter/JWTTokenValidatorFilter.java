package com.eprogs.raiseme.filter;


import com.eprogs.raiseme.constant.ApplicationConstant;
import com.eprogs.raiseme.service.authServices.JwtTokenService;
import com.eprogs.raiseme.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.eprogs.raiseme.constant.ApplicationConstant.JWT_SECRET_KEY;


@Component
public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenService jwtService;
    @Autowired
    private UserService userService;

    /**
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwtHeader = request.getHeader(ApplicationConstant.JWT_HEADER);
        if (null != jwtHeader && jwtHeader.startsWith("Bearer ")) {
            try {
                String jwtToken = jwtHeader.substring(7);

                SecretKey secretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
                if (null != secretKey) {
                    Claims claims = jwtService.getAllClaimsFromToken(jwtToken);
                    String username = String.valueOf(claims.get("username"));
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userService.loadUserByUsername(username);

                        String token = userService.findByEmail(userDetails.getUsername()).getToken();
                        if (!ObjectUtils.isEmpty(token)) {
                            if (jwtService.validateToken(jwtToken, userDetails)) {
                                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                                        userDetails.getAuthorities());
                                SecurityContextHolder.getContext().setAuthentication(authentication);
                            }
                        } else throw new BadCredentialsException("Invalid Token received!");
                    }
                }


            } catch (Exception exception) {
                throw new BadCredentialsException("Invalid Token received!");
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/v1/authentication/login") ||
                request.getServletPath().equals("/swagger-ui/**") ||
                request.getServletPath().equals("/v3/api-docs/**") ||
                request.getServletPath().equals("/webjars/** ");
    }
}
