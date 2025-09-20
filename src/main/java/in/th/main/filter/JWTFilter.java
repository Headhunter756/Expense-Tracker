package in.th.main.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import in.th.main.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

	@Autowired
	private ApplicationContext context;
	@Autowired
	private JWTService service;
	private Logger logger = Logger.getLogger(JWTFilter.class.getName());
	private AntPathMatcher matcher = new AntPathMatcher();
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String authHeader = request.getHeader("Authorization");
			String token = "";
			String username = "";
			if (!authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
				logger.log(Level.INFO, "Header Received at Filter");
				token = authHeader.substring(7);
				username = service.extractUsername(token);
			} else {
				logger.log(Level.SEVERE, "Blank Header is received at filter");
				throw new ServletException("Header is missing or malformed");
			}
			if (username != null && !username.isBlank()
					&& SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = context.getBean(UserDetailsService.class).loadUserByUsername(username);
				if (service.validate(token, userDetails)) {
					logger.log(Level.INFO, "Validation Successful");
					UsernamePasswordAuthenticationToken authtoken = 
							new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
					authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authtoken);
				} else {
					logger.log(Level.SEVERE, "Validation failed");
					throw new ServletException("Validation error occured");
				}

			} else if (username == null) {
				logger.log(Level.SEVERE, "No User Found with such email");
				throw new ServletException("No user found");
			} else if (username.isBlank()) {
				logger.log(Level.SEVERE, "Username received is blank");
			}
		} finally {
			filterChain.doFilter(request, response);
		}
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return matcher.match("/auth/**", request.getRequestURI());
	}
	
}
