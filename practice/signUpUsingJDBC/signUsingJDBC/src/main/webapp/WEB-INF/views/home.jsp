<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home</title>
</head>
<body>
    <h1>데모 서비스</h1>
    <form action="${cp}/member/login" method="get">
    	<input type="submit" value="Login"/>
    </form>
    <form action="${cp}/member/signUp" method="get">
    	<input type="submit" value="signUp"/>
    </form>
</body>
</html>
