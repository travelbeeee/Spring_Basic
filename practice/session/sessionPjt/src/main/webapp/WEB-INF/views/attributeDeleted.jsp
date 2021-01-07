<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Session 정보 삭제 후</title>
</head>
<body>
	<h5>
		Session ID : <%=session.getId() %><br>
		Session Value1 : <%=session.getAttribute("sessionKey1") %><br>
		Session Value2 : <%=session.getAttribute("sessionKey2") %><br>
		Session Interval : <%=session.getMaxInactiveInterval() %><br>
	</h5>
	
	<a href="${cp}">Home으로 돌아가기</a>
	
</body>
</html>