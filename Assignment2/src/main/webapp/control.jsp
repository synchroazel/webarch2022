<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>control panel</title>
    <link rel="stylesheet" href="simple.css">
</head>
<body>

<jsp:useBean id="curUser" class="it.unitn.padalino.assignment2.UserBean" scope="session"/>

<h1>Authenticated as
    <jsp:getProperty name="curUser" property="username"/>
</h1>

<p>This is a control panel for all activity in the webapp. Below you can see all authenticated users and their scores.</p>

</body>
</html>

