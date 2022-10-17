package it.unitn.padalino.assignment2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "Login", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    StringHasher stringHasher = new StringHasher();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher rs = request.getRequestDispatcher("login.jsp");
        rs.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        // get Session and Context
        HttpSession session = request.getSession();
        ServletContext ctx = getServletConfig().getServletContext();

        if (session.getAttribute("curUser") == null) {
            session.setAttribute("curUser", new UserBean());
        }

        String user = request.getParameter("user");
        String pass = stringHasher.sha256(request.getParameter("pass"));

        // Check if the user logged as admin right away
        if (user.equals("admin") & pass.equals(stringHasher.sha256("nimda"))) {

            synchronized (session) {
                UserBean curUserBean = new UserBean();
                curUserBean.setUsername("admin");
                curUserBean.setActive(true);
                session.setAttribute("curUser", curUserBean);
                ctx.setAttribute(curUserBean.getUsername(), curUserBean);
            }
            RequestDispatcher rs = request.getRequestDispatcher("/control");
            rs.forward(request, response);
        }

        // Check if the user exists and the password matches

        boolean auth = false;
        boolean userExist = ctx.getAttribute(user) != null;

        if (userExist) {
            UserBean bctx = (UserBean) ctx.getAttribute(user);
            boolean passMatch = bctx.getPassword().equals(pass);
            if (passMatch) {
                auth = true;
            }
        }

        // ...if that's the case, "move" the user in the session

        if (auth) {
            synchronized (session) {
                session.setAttribute("curUser", ctx.getAttribute(user));
                ((UserBean) ctx.getAttribute(user)).setActive(true);  // the user is currently active
                ((UserBean) ctx.getAttribute(user)).setPoints(0);  // reset points to 0
            }
            RequestDispatcher rs = request.getRequestDispatcher("/welcome");
            rs.forward(request, response);

        } else {
            out.println("<p>Username or password incorrect.</p>");
            RequestDispatcher rs = request.getRequestDispatcher("login.jsp");
            rs.include(request, response);
        }
    }
}