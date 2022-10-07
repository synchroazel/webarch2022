package it.unitn.disi.webarch.ronchet.Sessions;

import java.io.*;
import java.util.Date;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "demoSession", value = "/DemoSession")
public class DemoSessionServlet extends HttpServlet {
    PrintWriter out=null;
    private void p(String s) {
        out.println(s);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        out = response.getWriter();

        // Return the existing session if there is one.
        // Create a new session otherwise.
        HttpSession session = request.getSession();
        Integer accessCount;
        synchronized(session) {
            accessCount =
                    (Integer)session.getAttribute("accessCount");
            if (accessCount == null) {
                accessCount = 0;   // autobox int to Integer
            } else {
                accessCount = accessCount + 1;
            }
            session.setAttribute("accessCount", accessCount);
        }
        try {
            response.setContentType("text/html;charset=UTF-8");
            p("<!DOCTYPE html>"
                    +"<html>"
                    +"<head><title>Session Test Servlet</title></head><body>");
            p("Session is new? "+session.isNew());
            p("<h2>You accessed this site " + accessCount
                    + " times in this session.</h2>");
            p("<ul><li>Your session ID is " + session.getId() + "</li>");
            p("<li>Session creation time is " +
                    new Date(session.getCreationTime()) + "</li>");
            p("<li>Session last access time is " +
                    new Date(session.getLastAccessedTime()) + "</li>");
            p("<li>Session max inactive interval  is " +
                    session.getMaxInactiveInterval() + " seconds)</li></ul>");
            p("<p><a  href='" + request.getRequestURI()
                    +  "'>Refresh</a>");
            p("<p><a  href='"
                    + response.encodeURL(request.getRequestURI())
                    + "'>Refresh with  URL rewriting</a>\n");
            p("<form method=\"GET\" action=\"endSession\">\n"
                    +"<input type=\"submit\" value=\"End Session\">\n"
                    +"</form>");
            p("</body></html>");
        } finally {
            out.close(); // Always close the output writer
        }
    } // end DoGet
}