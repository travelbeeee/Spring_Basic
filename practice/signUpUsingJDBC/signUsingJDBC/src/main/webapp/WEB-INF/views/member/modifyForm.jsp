<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>회원정보수정페이지</title>
</head>
<body>
	회원정보수정
	<form action="${cp}/member/modify" method="post">
		UserName : <input type="text" name="username" value="${member.getUsername()}"/>
		Email : <input type="text" name="email" value="${member.getEmail()}"/>
		Password : <input type="password" name="pwd" value="${member.getPwd()}"/>
		<input type="submit" value="submit"/>
	</form>
	<a href="${cp}/member/main">돌아가기</a>
</body>
</html>