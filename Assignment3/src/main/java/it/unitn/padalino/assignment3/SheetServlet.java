package it.unitn.padalino.assignment3;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;


@WebServlet(value = "/")
public class SheetServlet extends HttpServlet {

    Timestamp lastTimestamp = null;

    SheetState sheetState;

    private void initState(HttpServletRequest request, HttpServletResponse response) {

        SSEngine engine = SSEngine.getSSEngine();

        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());

        // initialize sheetstate with initial timestamp and all cells with formula "" and value ""

        sheetState = new SheetState();

        sheetState.setTimestamp(timestamp);
        sheetState.setCells(new HashMap<>());

        for (int i = 0; i < engine.getNRows(); i++) {
            for (int j = 0; j < engine.getColumns().length; j++) {
                String cellId = engine.getColumns()[j] + (i + 1);
                CellState cellContent = new CellState("", "");
                sheetState.cells.put(cellId, cellContent);

            }
        }
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        if (sheetState == null) {
            initState(request, response);
        }

        response.setContentType("text/html");

        request.getRequestDispatcher("spreadsheet.jsp").forward(request, response);

    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html");

        String cell = request.getParameter("cell");
        String content = request.getParameter("content");
        String strTimestamp = request.getParameter("timestamp");

        Timestamp timestamp;  // timestamp parameter needs some further parsing

        if (strTimestamp.equals("never")) {  // if timestamp is "never", then is set to null
            timestamp = null;
        } else {  // else, timestamp is parsed
            timestamp = Timestamp.valueOf(
                    request.getParameter("timestamp")
                            .replace("T", " ")
                            .replace("Z", "")
            );
        }

        /* IF CHECKING FOR UPDATES */

        if (cell == null && content == null) {

            System.out.println("[INFO] Checking for updates");

            System.out.println("[INFO] Last timestamp: " + lastTimestamp);
            System.out.println("[INFO] Current timestamp: " + timestamp);

            if (timestamp == null || timestamp.before(lastTimestamp)) {  // if client table is outdated

                System.out.println("[INFO] Updates found");

                // update lastTimestamp
                lastTimestamp = Timestamp.valueOf(sheetState.timestamp);

                // send JSON to client
                response.getWriter().println(new Gson().toJson(sheetState));

            } else {

                System.out.println("[INFO] No updates");

            }

            /* IF USER MADE ACTUAL CHANGES */

        } else {

            System.out.println("[INFO] User made changes");

            SSEngine engine = SSEngine.getSSEngine();

            Set<Cell> modifiedCells = engine.modifyCell(cell, content);

            System.out.println("[INFO] Modified cells: " + modifiedCells);

            // update timestamp of sheetState
            sheetState.timestamp = String.valueOf(timestamp);

            // update lastTimestamp
            lastTimestamp = Timestamp.valueOf(sheetState.timestamp);

            // cycle through modifiedCells and update sheetState cells map

            if (modifiedCells != null) {  // if modifiedCells is not null, hence no illegal formulas were used
                for (Cell c : modifiedCells) {
                    String id = c.id;
                    String value = String.valueOf(c.value);
                    String formula = c.formula;
                    sheetState.cells.replace(
                            id,
                            new CellState(value, formula)
                    );
                }
            } else {  // if modifiedCells is null, hence illegal formulas were used
                sheetState.cells.replace(
                        // evaluate such formulas to zero and append [!] to the formula to alert the user
                        cell,
                        new CellState("0", content + "[!]")
                );
            }

            System.out.println("[INFO] sheetState.json file updated");

        }

    }


    public void destroy() {
    }
}