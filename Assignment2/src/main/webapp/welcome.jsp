<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>welcome</title>
    <link rel="stylesheet" href="simple.css">
</head>
<body>

<jsp:useBean id="curUser" class="it.unitn.padalino.assignment2.UserBean" scope="session"/>

<h1>Authenticated as
    <jsp:getProperty name="curUser" property="username"/>
</h1>
<h3>Your score is:
    <jsp:getProperty name="curUser" property="points"/>
</h3>

<br>
<p>How good you are with geography? Can you match flags to their countries capitals?</p>

<form type="button" method="get" action="play">
    <button type="submit">start game</button>
</form>

</body>
</html>