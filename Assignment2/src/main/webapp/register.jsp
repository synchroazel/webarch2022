<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>login form</title>
    <link rel="stylesheet" href="simple.css">
</head>
<body>
<h1>Registration</h1>
<form method="post" action="register">
    <label for="user"><b>Username:</b></label>
    <input type="text" placeholder="Enter Username" name="user" required>
    <label for="pass1"><b>Password:</b></label>
    <input type="password" placeholder="Enter Password" name="pass1" required>
    <label for="pass2"><b>Retype password:</b></label>
    <input type="password" placeholder="Retype Password" name="pass2" required>
    <input type="submit" value="register"/>
</form>
</body>
</html>
