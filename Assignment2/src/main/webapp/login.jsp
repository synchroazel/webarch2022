<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>login form</title>
    <link rel="stylesheet" href="simple.css">
</head>
<body>
<h1>Login</h1>
<form method="post" action="login">
    <label for="user" id="user"><b>Username:</b></label>
    <input type="text" placeholder="Enter Username" name="user" required>
    <label for="pass" id="pass"><b>Password:</b></label>
    <input type="password" placeholder="Enter Password" name="pass" required>
    <input type="submit" value="login"/>
    <br>
    <p>Or register <a href="register">here</a> if it's your first time!</p>
</form>
</body>
</html>
