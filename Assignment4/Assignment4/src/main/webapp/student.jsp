<%@ page import="it.unitn.padalino.assignment4remote.StudentDTO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

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

<head>
    <title>Student page</title>
</head>
<body>
<h1>Student page</h1>

<form action="student" method="post">
    <label for="mat">Insert matriculation number:</label>
    <input type="number" id="mat" name="mat" min="1">
    <input type="submit">
</form>

<%String error = (String) request.getAttribute("error"); %>
<%String mat = (String) request.getParameter("mat"); %>
<%StudentDTO student = (StudentDTO) request.getAttribute("student"); %>

<%if (error != null) {%> <p><%=request.getAttribute("error")%> </p> <%}%>

<%if (mat != null && error == null) { %>

<br>

<h3><%=(String) student.getName()%> <%=(String) student.getSurname()%> (matricule no. <%=mat%>)</h3>

<table>
    <td><b><%=student.getSurname()%>'s exams</b></td>
    <%for (int i = 0; i < student.getCourses().size(); i++) { %>
    <tr>
        <td><%=student.getCourses().get(i)%>
        </td>
        <%if (student.getGrades().get(i).equals(0)) {%>
        <td>NA</td>
        <%} else {%>
        <td><%=student.getGrades().get(i)%>
        </td>
        <%}%>
    </tr>
    <%}%>
</table>

<%}%>

<br><br><br>
<a href="index.jsp">back home</a> • <a href="advisor">advisor page</a>

</body>
</html>
