<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Main Page</title>
</head>
<body>
	<h1>Main Page 입니다.</h1>
	<h3>회원 아이디 : ${member.username}</h3>
	<h3>회원 이메일 : ${member.email }</h3>
	
	<form action="${cp}/member/modify" method="get">
		<input type="submit" value="회원정보수정"/>
	</form>
	<form action="${cp}/member/logout" method="get">
		<input type="submit" value="로그아웃"/>
	</form>
	<form action="${cp }/member/delete" method="post">
		<input type="submit" value="회원탈퇴"/>
	</form>
</body>
</html>