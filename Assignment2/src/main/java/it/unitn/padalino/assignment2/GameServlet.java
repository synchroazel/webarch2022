package it.unitn.padalino.assignment2;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "gameServlet", value = "/play")
public class GameServlet extends HttpServlet {

    GameLogic gl = new GameLogic();

    String[] countries = {"Algeria", "Armenia", "Chad", "Czech Republic", "Djibouti", "Gabon", "Indonesia", "Lithuania", "Malta", "Ukraine"};

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int cap1 = Integer.valueOf(request.getParameter("cap1"));
        int cap2 = Integer.valueOf(request.getParameter("cap2"));
        int cap3 = Integer.valueOf(request.getParameter("cap3"));

        int flagId1 = Arrays.asList(countries).indexOf(request.getSession().getAttribute("flag1"));
        int flagId2 = Arrays.asList(countries).indexOf(request.getSession().getAttribute("flag2"));
        int flagId3 = Arrays.asList(countries).indexOf(request.getSession().getAttribute("flag3"));

        HttpSession session = request.getSession();

        if (flagId1 == cap1 - 1 & flagId2 == cap2 - 1 & flagId3 == cap3 - 1) {
            UserBean curUserBean = (UserBean) (session.getAttribute("curUser"));
            curUserBean.setPoints(curUserBean.getPoints() + 3);

        } else {

            UserBean curUserBean = (UserBean) (session.getAttribute("curUser"));
            curUserBean.setPoints(curUserBean.getPoints() - 1);

        }

        RequestDispatcher rs = request.getRequestDispatcher("welcome");
        rs.forward(request, response);

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html");

        String[] flags = gl.randomPick(countries);

        HttpSession session = request.getSession();

        session.setAttribute("flag1", flags[0]);
        session.setAttribute("flag2", flags[1]);
        session.setAttribute("flag3", flags[2]);

        RequestDispatcher rs = request.getRequestDispatcher("game.jsp");
        rs.forward(request, response);

    }
}