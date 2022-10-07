package it.unitn.disi.webarch.ronchet.Cookies;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "cookieServlet", value = {"/cookieServlet"})
public class CookieServlet extends HttpServlet {

    String msg;
    boolean isInitialIteration;

    public void log(String msg) {
        getServletContext().log(msg);
    }

    private void dealWithInvalidCookie() {
        msg = "Sorry, we do not know each other...<br>"
                + "Please introduce yourself.<br>";
        isInitialIteration = true;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        log("started");
        isInitialIteration = false;
        // manage params and cookies
        String name = request.getParameter("value");
        log(name);
        if (name != null && !name.equals("")) {
            // there is the right parameter,
            // no need to read cookie, but we set them
            log("name != null && ! name.equals(\"\")");
            Cookie cookie = new Cookie("name", name);
            msg = "Hi " + name + ", nice to meet you!";
            response.addCookie(cookie); // identity
            Cookie cookie1 = new Cookie("counter", "0");
            response.addCookie(cookie1); // state
            // finished! Go to end
        } else {
            // no parameter, let's try with cookies
            Cookie cookies[] = request.getCookies();
            if (cookies == null || cookies.length == 0) {
                // no cookies
                log("no cookies found");
                dealWithInvalidCookie();
            } else {
                Cookie n_Cookie = null; // cookie con il nome
                Cookie c_Cookie = null; // cookie con il contatore
                for (Cookie c : cookies) {
                    String cookieName = c.getName();
                    if (cookieName.equals("name")) {
                        n_Cookie = c;
                    } else if (cookieName.equals("counter")) {
                        c_Cookie = c;
                    }
                }
                if (n_Cookie == null) {
                    log("valid cookies not found");
                    // invalid cookie
                    dealWithInvalidCookie();
                } else {
                    // ok, the cookie is good!
                    String userName = n_Cookie.getValue();
                    String counterAsString = c_Cookie.getValue();
                    log("name == " + userName);
                    // let's update the counter, and the cookie
                    int counter = Integer.valueOf(counterAsString) + 1;
                    c_Cookie.setValue("" + counter);
                    response.addCookie(c_Cookie);
                    msg = "Hi " + userName + ", welcome back! (" + counter + ")";
                }
            }
        }
        // prepare response and send it
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html><body>");
            out.println(msg);
            if (isInitialIteration) {
                request.getRequestDispatcher("WhatsYourNameFragment.html")
                        .include(request, response);
            } else {
                request.getRequestDispatcher("DeleteCookiesFragment.html")
                        .include(request, response);
            }
            out.println("</body></html>");
        } catch (IOException|ServletException e) {
            e.printStackTrace();
        }
    }
}