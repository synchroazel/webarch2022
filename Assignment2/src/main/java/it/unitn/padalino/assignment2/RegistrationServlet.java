package it.unitn.padalino.assignment2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "Register", urlPatterns = {"/register"})
public class RegistrationServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        ServletContext ctx = getServletConfig().getServletContext();

        String username = request.getParameter("user");
        String password1 = request.getParameter("pass1");
        String password2 = request.getParameter("pass2");

        // if inserted username & password are valid ...

        if (username != null & password1 != null & password2 != null) {

            if (!username.equals("admin")) {

                if (password1.equals(password2)) {

                    StringHasher stringHasher = new StringHasher();
                    String hashedPass = stringHasher.sha256(password1);

                    boolean auth = false;

                    // ... check if the username already exists in ServletContext

                    boolean userExist = ctx.getAttribute(username) != null;

                    if (userExist) {

                        UserBean b = (UserBean) ctx.getAttribute(username);

                        boolean passMatch = b.getPassword().equals(hashedPass);

                        // if the user exists and the inserted password matches ...

                        if (passMatch) {
                            auth = true;  // ... the user already registered
                        }
                    }

                    if (!auth) {  // if the user didn't already register

                        // put the user info in a UserBean

                        UserBean userBean = new UserBean();
                        userBean.setUsername(username);
                        userBean.setPassword(hashedPass);
                        userBean.setPoints(0);
                        userBean.setActive(false);

                        // and put the bean in ServletContext

                        ctx.setAttribute(userBean.getUsername(), userBean);

                        out.println("<p>You registered successfully!</p>");
                        RequestDispatcher rs = request.getRequestDispatcher("login.jsp");
                        rs.include(request, response);

                    } else {
                        out.println("<p>You are already registered!</p>");
                        RequestDispatcher rs = request.getRequestDispatcher("register.jsp");
                        rs.include(request, response);
                    }

                } else {
                    out.println("<p>Passwords do not match!</p>");
                    RequestDispatcher rs = request.getRequestDispatcher("register.jsp");
                    rs.include(request, response);
                }

            } else {
                out.println("<p>Cannot register as admin!</p>");
                RequestDispatcher rs = request.getRequestDispatcher("register.jsp");
                rs.include(request, response);
            }

        } else {
            RequestDispatcher rs = request.getRequestDispatcher("register.jsp");
            rs.include(request, response);
        }
    }
}