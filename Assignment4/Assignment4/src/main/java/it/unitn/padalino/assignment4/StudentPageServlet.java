package it.unitn.padalino.assignment4;

import it.unitn.padalino.assignment4remote.StudentDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(name = "student-servlet", value = "/student")
public class StudentPageServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        request.getRequestDispatcher("/student.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("text/html");

        String mat = request.getParameter("mat");

        BusinessDelegate bd = new BusinessDelegate();

        try {

            StudentDTO student = bd.getStudent(Integer.parseInt(mat));
            request.setAttribute("student", student);
            request.getRequestDispatcher("/student.jsp").forward(request, response);

        } catch (javax.ejb.EJBException e) {
            request.setAttribute("error", "No student found with matriculation number " + mat + ".");
            request.getRequestDispatcher("/student.jsp").forward(request, response);

        }

    }

}