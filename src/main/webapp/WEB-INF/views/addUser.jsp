<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 02.03.2018
  Time: 18:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add user</title>
    <style>
        .field {
            clear: both;
            text-align: right;
            line-height: 25px;
        }
        .submit {
            margin-top: 5px;
        }
        label {
            float: left;
            padding-right: 10px;
        }
        .main {
            width: 253px;
            margin: 0 auto;
        }
        .warning {
            color: red;
            background-color: khaki;
        }
    </style>
</head>
<body>
<div align="center">
<div class="main">
    <c:if test="${isNotValid}">
        <p class="warning">Invalid name or password!</p>
    </c:if>
    <form action="${pageContext.servletContext.contextPath}/admin/addUser" method="POST">
        <div class="field">
            <label for="uName">Username:</label>
            <input type="text" id="uName" name="login" />
        </div>
        <div class="field">
            <label for="uPass">Password:</label>
            <input type="text" id="uPass" name="password" />
        </div>
        <div class="field">
            <label for="uRole">Role:</label>
            <select style="width: 173px" name="role" id="uRole">
                <option value="user">user</option>
                <option value="admin">admin</option>
                <option value="admin, user">admin, user</option>
            </select>
        </div>
        <div class="submit">
            <input type="submit" align="center" value="Submit"/>
        </div>

    </form>
</div>
</div>
</body>
</html>
