package it.unitn.padalino.assignment2;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class AdminFilter implements Filter {
    protected FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession();

        if (session.getAttribute("curUser") != null) {

            UserBean curUserBean = (UserBean) session.getAttribute("curUser");

            if (curUserBean.getUsername().equals("admin")) {
                chain.doFilter(request, response);
            } else {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }

        } else {
            RequestDispatcher rs = req.getRequestDispatcher("login");
            rs.include(request, response);
        }
    }
}
