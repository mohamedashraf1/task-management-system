package banquemisr.challenge05.filter;

import banquemisr.challenge05.constants.Constant;
import banquemisr.challenge05.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Log4j2
@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public AuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(Constant.AUTHORIZATION_HEADER);

        if (authHeader == null || !authHeader.startsWith(Constant.AUTH_HEADER_PREFIX)) {

            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.replace(Constant.AUTH_HEADER_PREFIX, "");
            Claims claims = jwtService.extractAllClaims(token);

            if (!jwtService.isTokenValid(claims.getSubject(), token)) {

                filterChain.doFilter(request, response);
                return;
            }

            ThreadContext.put(Constant.USER_ID, claims.get(Constant.USER_ID) + "");

            List<SimpleGrantedAuthority> ga = ((List<String>) claims.get("roles")).stream().map(SimpleGrantedAuthority::new).toList();
            UserDetails userDetails = new User(claims.get(Constant.USER_ID) + "", "", ga);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error("Exception in Authentication Filter", e);
            SecurityContextHolder.clearContext();
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            ThreadContext.clearAll();
        }
    }

}
