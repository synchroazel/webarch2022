package it.unitn.disi.webarch.ronchet.Cookies;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "DeleteCookiesServlet", value = "/deleteCookies")
public class DeleteCookiesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        Cookie cookies[]=request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                c.setMaxAge(0);
                response.addCookie(c);
            }
        }
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher(
                "CookiesHaveBeenDeleted.html")
                .include(request, response);
    }
}
