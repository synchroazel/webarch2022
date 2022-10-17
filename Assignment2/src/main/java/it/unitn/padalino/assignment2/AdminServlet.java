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
import java.util.Enumeration;

@WebServlet(name = "Control", urlPatterns = {"/control"})
public class AdminServlet extends HttpServlet {

    PrintWriter out = null;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        out = response.getWriter();

        response.setContentType("text/html;charset=UTF-8");

        RequestDispatcher rs = request.getRequestDispatcher("control.jsp");
        rs.include(request, response);

        ServletContext ctx = getServletConfig().getServletContext();

        Enumeration allAttributes = ctx.getAttributeNames();

        while (allAttributes.hasMoreElements()) {

            String curAttribute = (String) allAttributes.nextElement();

            if (ctx.getAttribute(curAttribute) instanceof UserBean) {

                UserBean b = (UserBean) ctx.getAttribute(curAttribute);

                if (b.getActive() & !curAttribute.equals("admin")) {
                    out.println("<h4>" + b.toString(false) + "</h4>");

                }
            }
        }
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        out = response.getWriter();

        response.setContentType("text/html;charset=UTF-8");

        RequestDispatcher rs = request.getRequestDispatcher("control.jsp");
        rs.include(request, response);

        ServletContext ctx = getServletConfig().getServletContext();

        Enumeration allAttributes = ctx.getAttributeNames();

        while (allAttributes.hasMoreElements()) {

            String curAttribute = (String) allAttributes.nextElement();

            if (ctx.getAttribute(curAttribute) instanceof UserBean) {

                UserBean b = (UserBean) ctx.getAttribute(curAttribute);

                if (b.getActive() & !curAttribute.equals("admin")) {
                    out.println("<h4>" + b.toString(false) + "</h4>");

                }
            }
        }
    }

}
