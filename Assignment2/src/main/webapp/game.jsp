<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="simple.css">
    <meta charset="UTF-8">
    <title>game session</title>
</head>
<body>

<jsp:useBean id="curUser" class="it.unitn.padalino.assignment2.UserBean" scope="session"/>
<h1>Authenticated as
    <jsp:getProperty name="curUser" property="username"/>
</h1>

<p>Here's a set of capitals.<br>Can you match the flag to its country's capital?</p>

<ol type="1">
    <div style="float: left; padding-right: 75px">
        <li>Algiers</li>
        <li>Yerevan</li>
        <li>N'Djamena</li>
        <li>Prague</li>
        <li>Djibouti</li>
    </div>

    <div style="float: left">
        <li>Libreville</li>
        <li>Jakarta</li>
        <li>Vilnius</li>
        <li>La Valletta</li>
        <li>Kiev</li>
    </div>
</ol>

<br style="clear:both"/>

<form method="post" action="validate-input">

    <div style="float:left; padding-left: 10px; padding-right: 25px">
        <br><img src=<%=("/flags/" + session.getAttribute("flag1") + ".jpg").replace(" ","%20")%> alt="flag1"><br>
        <label for="capital1">This country's capital: </label>
        <input type="number" id="capital1" name="cap1" style="width: 75px" required min="1" max="10">
    </div>

    <div style="float:left; padding-left: 10px; padding-right: 25px">
        <br><img src=<%=("/flags/" + session.getAttribute("flag2") + ".jpg").replace(" ","%20")%> alt="flag2"><br>
        <label for="capital2">This country's capital: </label>
        <input type="number" id="capital2" name="cap2" style="width: 75px" required min="1" max="10">
    </div>

    <div style="float:left; padding-left: 10px; padding-right: 25px">
        <br><img src=<%=("/flags/" + session.getAttribute("flag3") + ".jpg").replace(" ","%20")%> alt="flag3"><br>
        <label for="capital3">This country's capital: </label>
        <input type="number" id="capital3" name="cap3" style="width: 75px" required min="1" max="10">
    </div>

    <div style="clear: left">
        <br><input type="submit" value="submit"><br>

        <a class="error">${errors.err0}</a>
        <a class="error">${errors.err1}</a>
        <a class="error">${errors.err2}</a>
        <a class="error">${errors.err3}</a>

        <br>
    </div>

</form>

</body>
</html>
