<%@ page import="it.unitn.padalino.assignment3.SSEngine" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Mini Spreadsheet</title>
</head>

<style>
    table, th {
        border: 1px solid #464F51;
        border-collapse: collapse;
        min-width: 32px;
        height: 20px;
        text-align: center
    }

    td {
        border: 1px solid #464F51;
        border-collapse: collapse;
        min-width: 32px;
        height: 20px;
        padding: 4px 8px;
        text-align: right
    }

    body {
        font-family: "Helvetica Neue", Helvetica, Arial, sans-serif
    }
</style>

<body>

<h1>Mini Spreadsheet</h1>

<p><span>Last update: </span><span id="last-update">never</span></p>
<br>

<div id="container">
    <form id="cellForm" onsubmit="saveCell()">
        <input type="text" id="cellSpace" name="cellForm" value="cell content">
    </form>
</div>
<br>

<%SSEngine engine = SSEngine.getSSEngine();%>

<table>
    <tr>
        <th></th>
        <%for (int i = 0; i < engine.getColumns().length; i++) {%>
        <th><%=engine.getColumns()[i]%>
        </th>
        <%}%>
    </tr>
        <%for (int i = 0; i < engine.getNRows(); i++) {%>
    <tr>
        <th><%=i + 1%>
        </th>
        <%for (int j = 0; j < engine.getColumns().length; j++) {%>
        <%String cellname = engine.getColumns()[j] + (i + 1);%>
        <td id=<%=cellname%> onclick="editCell(this.id)"></td>
        <%}%>
    </tr>
        <%}%>

    <script>

        console.log("[INFO] Welcome. You are not currently editing any cell.");

        let editing = false;
        let curCell = null;
        let cellFormulas = new Map();  // create client-side map to store all cells with edited formulas


        /* ↓↓↓ INTERACTION ↓↓↓ */

        // When a cell is clicked, display its content in the form above the table
        function editCell(cellId) {
            console.log("[DEBUG] `editing` set to true");
            editing = true;

            curCell = cellId;
            console.log("[DEBUG] Currently editing " + cellId);

            document.getElementById("cellSpace").value = document.getElementById(curCell).textContent;

            document.getElementById(curCell).style.backgroundColor = "rgb(142, 180, 251, 0.25)";
            document.getElementById(curCell).style.boxShadow = "inset 0 0 0 2px rgb(142, 180, 251, 0.5)";

            document.getElementById("cellSpace").focus();

            // if cell is in the map, and it's not empty, display its formula

            if (cellFormulas.has(curCell) && cellFormulas.get(curCell) !== "") {

                console.log("[DEBUG] cell " + curCell + " has a formula: " + cellFormulas.get(curCell));

                document.getElementById("cellSpace").value = cellFormulas.get(curCell);

            } else {

                console.log("[DEBUG] cell " + curCell + " has an empty formula");

                document.getElementById("cellSpace").value = "=" + document.getElementById("cellSpace").value;
            }

            document.getElementById(curCell).textContent = document.getElementById("cellSpace").value;

        }


        // prevent cellForm to be submitted (we need our own dynamics)
        document.getElementById("cellForm").addEventListener("submit", function (event) {
            event.preventDefault();
        });


        // if the user is editing a cell display its content in the form
        document.getElementById("cellSpace").addEventListener("input", function (event) {
            if (editing) {
                document.getElementById(curCell).textContent = document.getElementById("cellSpace").value;
            }
        });


        /* ↓↓↓ LOGIC ↓↓↓ */

        // When the form is submitted, send a http request to save modifications to sheetState.json
        function saveCell() {
            console.log("[DEBUG] You are not currently editing any cell");
            editing = false;
            document.getElementById(curCell).textContent = document.getElementById("cellSpace").value;
            document.getElementById("cellSpace").value = "";

            var xhttp = new XMLHttpRequest();
            xhttp.responseType = "json";
            xhttp.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {

                    // since the server made some changes to sheetState.json, immediately check for updates
                    checkUpdates();

                }
            }

            xhttp.open("POST", "spreadsheet", true);
            xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

            let cellContent = document.getElementById(curCell).textContent;

            if (cellContent != "=") {  // if the user actually entered some formula

                // encode cell content for + / * and - signs
                cellContent = encodeURIComponent(cellContent);

                // this little workaround is needed to avoid getting wrong timezone time
                let x = (new Date()).getTimezoneOffset() * 60000;
                let localISOTime = (new Date(Date.now() - x)).toISOString().slice(0, -1);

                // we're sending current timestamp, cell ID and its content
                let toSend = "cell=" + curCell + "&content=" + cellContent + "&timestamp=" + localISOTime;

                console.log("[DEBUG] Sending POST request with: " + toSend);

                xhttp.send(toSend);

            } else {  // if the user left the cell blank

                document.getElementById(curCell).textContent = "";

            }

            document.getElementById(curCell).style.backgroundColor = "white";
            document.getElementById(curCell).style.boxShadow = "inset 0 0 0 2px white";
            document.getElementById("cellSpace").blur();

        }


        // if the user is editing a cell and a mouse click happens outside the form, call saveCell()
        document.addEventListener('mousedown', function (event) {
            if (editing) {
                if (event.target.id !== "cellSpace") {
                    saveCell();
                }
            }
        }, true);


        // Send a http request to check for updates in sheetState.json and update the table
        function checkUpdates() {

            console.log("[DEBUG] Checking for updates");

            var sheetState;
            var xhttp = new XMLHttpRequest();
            xhttp.responseType = "json";
            xhttp.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {

                    sheetState = this.response;

                    if (sheetState != null) {

                        console.log("[DEBUG] New sheetState received");
                        console.log(sheetState);

                        // cycle through sheetState.cells and update the cells
                        for (let cell in sheetState.cells) {

                            document.getElementById(cell).textContent = sheetState.cells[cell].cellValue;

                            // add to cellFormulas map the cell and its formula
                            cellFormulas.set(cell, sheetState.cells[cell].formula);

                        }

                        document.getElementById("last-update").innerText = sheetState.timestamp;

                    } else {
                        console.log("[INFO] No updates");
                    }
                }
            }

            xhttp.open("POST", "spreadsheet", true);
            xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

            // send the timestamp of the last state of the sheet
            let toSend = "timestamp=" + document.getElementById("last-update").innerText;

            console.log("[DEBUG] Sending POST request with: " + toSend);

            xhttp.send(toSend);

        }

        checkUpdates();

        setInterval(checkUpdates, 500);  // check for updates every half a second

    </script>

</body>
</html>