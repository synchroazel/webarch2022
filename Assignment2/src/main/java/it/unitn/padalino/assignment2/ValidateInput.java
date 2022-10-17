package it.unitn.padalino.assignment2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@WebServlet(name = "ValidateInput", urlPatterns = {"/validate-input"})
public class ValidateInput extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Map<String, String> errors = new HashMap<>();

        if (!request.getParameter("cap1").equals("") &
                !request.getParameter("cap2").equals("") &
                !request.getParameter("cap3").equals("")) {

            int cap1 = Integer.parseInt(request.getParameter("cap1"));
            int cap2 = Integer.parseInt(request.getParameter("cap2"));
            int cap3 = Integer.parseInt(request.getParameter("cap3"));

            if (cap1 < 1 || cap1 > 10) {
                errors.put("err1", "Your first input is invalid.<br>");
            }
            if (cap2 < 1 || cap2 > 10) {
                errors.put("err2", "Your second input is invalid.<br>");
            }
            if (cap3 < 1 || cap3 > 10) {
                errors.put("err3", "Your third input is invalid.<br>");
            }

        } else {
            errors.put("err0", "Make sure you filled every form.");
        }

        if (errors.isEmpty()) {
            RequestDispatcher rs = request.getRequestDispatcher("play");
            rs.forward(request, response);

        } else {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("game.jsp").forward(request, response);

        }
    }

}
