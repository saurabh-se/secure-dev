package com.se.compsecure.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Component;

import com.se.compsecure.utility.CompSecureConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class AdminFilter implements Filter {

	private static final Logger LOGGER = Logger.getLogger(AdminFilter.class.getName());

	private static final String AUTH_HEADER_KEY = "Authorization";
	private static final String AUTH_HEADER_VALUE_PREFIX = "Bearer "; //with  trailing  space  to  separate  token

	private static final int STATUS_CODE_UNAUTHORIZED = 401;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOGGER.info("JwtAuthenticationFilter initialized");
	}

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException {

		HttpServletResponse res = (HttpServletResponse) servletResponse;
		HttpServletRequest req = (HttpServletRequest)servletRequest;
		
		boolean loggedIn = false;
		try {

			String jwt = getBearerToken(req);

			if (jwt != null && !jwt.isEmpty()) {
				loggedIn = true;
			
				final Claims claims = Jwts.parser()         
					       .setSigningKey(DatatypeConverter.parseBase64Binary("secretkey"))
					       .parseClaimsJws(jwt).getBody();
				if (null == claims) {
					throw new ServletException("Unauthorized!!");
				}
				String role = (String)claims.get("roles");
				
				if (CompSecureConstants.ADMIN_ROLE_ID.equals(role)) {
					filterChain.doFilter(servletRequest, servletResponse);
				} else {
					 res = (HttpServletResponse) servletResponse;
					res.sendRedirect("login");
				}

			} else {
				LOGGER.info("No JWT provided, go on unauthenticated");
				
				res.sendRedirect(req.getContextPath()+"/");
			}
		} catch (final Exception e) {
			LOGGER.log(Level.WARNING, "Failed logging in with security token", e);
			HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
			httpResponse.setContentLength(0);
			httpResponse.setStatus(STATUS_CODE_UNAUTHORIZED);
		}
	}

	@Override
	public void destroy() {
		LOGGER.info("JwtAuthenticationFilter destroyed");
	}

	/**
	 * Get the bearer token from the HTTP request. The token is in the HTTP
	 * request "Authorization" header in the form of: "Bearer [token]"
	 */
	private String getBearerToken(HttpServletRequest request) {
		String authHeader = request.getHeader(AUTH_HEADER_KEY);
		if (authHeader != null && authHeader.startsWith(AUTH_HEADER_VALUE_PREFIX)) {
			return authHeader.substring(AUTH_HEADER_VALUE_PREFIX.length());
		}
		return null;
	}
}
