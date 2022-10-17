package it.unitn.padalino.assignment2;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    protected FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) throws ServletException {
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

            boolean auth = false;

            ServletContext ctx = request.getServletContext();

            boolean userExist = ctx.getAttribute(curUserBean.getUsername()) != null;

            if (userExist) {
                UserBean b = (UserBean) ctx.getAttribute(curUserBean.getUsername());

                boolean passMatch = b.getPassword().equals(curUserBean.getPassword());

                if (passMatch) {
                    auth = true;
                }
            }

            if (auth) {
                chain.doFilter(request, response);
            } else {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            }

        } else {

            RequestDispatcher rs = req.getRequestDispatcher("login");
            rs.forward(request, response);
//            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);

        }
    }
}
