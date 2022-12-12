package it.unitn.padalino.assignment4remote;

import it.unitn.padalino.assignment4remote.ejb.StudentHandlerBeanIf;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");

        InitialContext ctx = null;

        String jndi = "java:module/StudentHandlerBean!it.unitn.padalino.assignment4remote.ejb.StudentHandlerBeanIf";

        StudentHandlerBeanIf studentHandler = null;
        try {
            ctx = new InitialContext();
            studentHandler = (StudentHandlerBeanIf) ctx.lookup(jndi);
        } catch (NamingException e) {
            e.printStackTrace();
        }

        StudentDTO student = studentHandler.getStudent(2);

        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Hello, " + student.getName() + " " + student.getSurname() + "</h1>");
        out.println("</body></html>");


    }
}