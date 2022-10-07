package it.unitn.disi.webarch.ronchet.Sessions;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "endSession", value = "/endSession")
public class DeleteSessionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession s= request.getSession();
        s.invalidate();
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("SessionHasBeenDeleted.html")
                .include(request, response);

    }
}
