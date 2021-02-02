### [ 상황 ]

회원가입 입력 폼에서 아이디, 비밀번호, 이메일을 입력하면 Controller 에서 다음과 같이 커맨드 객체를 이용해서 값을 받고 있는 상황

```java
@PostMapping("/member/signUp")
public String signUp(SignUpForm signUpForm) throws NoSuchAlgorithmException {
    
}
```

하지만, signUpForm 의 null 말고 값이 넘어오지않음!!

<br>

### [ 해결방법 ]

**커맨드객체를 이용해서 값을 받아오려면 Setter/Getter 가 모두 설정되어있어야한다**...!!

```java
@Getter @ToString
public class SignUpForm {
    private String username;
    private String userpwd;
    private String email;
}

```

Setter가 필요없다고 판단해서 설정하지않고있었음...!!!!

반성하자...