<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Session 등록 후</title>
</head>
<body>
	<h3>
	현재 session 에 있는 정보를 출력하자!
	</h3>
	<h5>
	Session ID : <%=session.getId() %><br>
	Session Value1 : <%=session.getAttribute("sessionKey1") %><br>
	Session Value2 : <%=session.getAttribute("sessionKey2") %><br>
	Session Interval : <%=session.getMaxInactiveInterval() %><br>
	</h5>
	
	<a href="${cp}/">Home으로 돌아가기</a>
	<form action="${cp}/sessionDelete" method="get">
		<input type="submit" value="session삭제"/>
	</form>
	
	<form action="${cp}/attributeDelete" method="get">
		<input type="submit" value="session Attribute 하나 삭제"/>
	</form>
	
</body>
</body>
</html>