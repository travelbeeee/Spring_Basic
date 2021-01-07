<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<% HttpSession session = request.getSession(); %>
<body>
<h3>
	session 에 추가할 정보를 넘겨보자!
</h3>
	<form action="${cp}/session" method="post">
		<input type="text" name="sessionValue1" value="firstValue"/>
		<input type="text" name="sessionValue2" value="secondValue"/>
		<input type="submit" value="submit"/>
	</form>
<h3>
	현재 session 에 있는 정보를 출력하자!
</h3>
<h5>
	Session ID : <%=session.getId() %><br>
	Session Value1 : <%=session.getAttribute("sessionKey1") %><br>
	Session Value2 : <%=session.getAttribute("sessionKey2") %><br>
	Session Interval : <%=session.getMaxInactiveInterval() %><br>
</h5>
</body>
</html>
