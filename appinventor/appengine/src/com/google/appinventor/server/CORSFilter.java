package com.google.appinventor.server;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by warrenchan on 30/5/2017.
 */
public class CORSFilter implements Filter {
  // For security reasons set this regex to an appropriate value
  // example: ".*example\\.com"
  private static final String ALLOWED_DOMAINS_REGEXP = ".*[localhost|projectc|projc|lcd].*";
  private static final Logger LOG = Logger.getLogger("CORS");

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                       FilterChain filterChain) throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) servletRequest;
    HttpServletResponse resp = (HttpServletResponse) servletResponse;

    String origin = req.getHeader("Origin");
    if (origin != null && origin.matches(ALLOWED_DOMAINS_REGEXP)) {
      LOG.log(Level.INFO, origin + ": Allowed");
      resp.addHeader("Access-Control-Allow-Origin", origin);
      resp.addHeader("Access-Control-Allow-Credentials", "true");
      if ("options".equalsIgnoreCase(req.getMethod())) {
        resp.setHeader("Allow", "GET, POST, OPTIONS");
        if (origin != null) {
          String headers = req.getHeader("Access-Control-Request-Headers");
          String method = req.getHeader("Access-Control-Request-Method");
          resp.addHeader("Access-Control-Allow-Methods", method);
          resp.addHeader("Access-Control-Allow-Headers", headers);
          // optional, only needed if you want to allow cookies.
          resp.setContentType("text/x-gwt-rpc");
        }
        resp.getWriter().flush();
        return;
      }
    } else {
      LOG.log(Level.WARNING, origin + ": Rejected");
    }

    // Fix ios6 caching post requests
    if ("post".equalsIgnoreCase(req.getMethod())) {
      resp.addHeader("Cache-Control", "no-cache");
    }

    if (filterChain != null) {
      filterChain.doFilter(req, resp);
    }
  }

  @Override
  public void destroy() {
  }

  @Override
  public void init(FilterConfig arg0) throws ServletException {
  }
}
