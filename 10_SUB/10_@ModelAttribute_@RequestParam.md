# @ModelAttribute vs @RequestParam

### 0) @ModelAttribute vs @RequestParam

두 애노테이션 모두 Spring MVC에서 클라이언트의 전달값을 핸들러의 매개변수로 매핑할 때 사용하는 애노테이션입니다.

둘은 어떤 차이가 있는지 알아보겠습니다.

<br>

### 1) @RequestParam

`@RequestParam` 애노테이션은 사용자가 요청시 전달하는 값을 1:1로 매핑해줍니다.

```java
@RequestParam("name") String name;
```

url/?name=~~~ 같이 요청이 날라오면 요청 파라미터 중 "name" 이라는 이름을 가진 값을 `name` 이라는 변수명을 가진 `String` 객체에 정확하게 1:1 매핑을 해줍니다.

<br>

단점으로는 여러 값을 받을 때 매핑을 하나하나 다 해줘야하므로, 변경사항이 생기면 유연하게 대처하기가 어렵습니다.

[ 아이디, 비밀번호, 이메일을 입력받아서 회원가입 하는 상황]

```java
@PostMapping("/signUp")
public String signUp(
    @RequestParam("id") String id,
    @RequestParam("pwd") String pwd,
    @RequestParam("email") String email
){
    userService.signUp(id, pwd, email);
}
```

위와 같은 상황에서 전화번호를 추가로 입력받아야된다거나, 이메일을 입력받지않는다거나 하는 변경사항이 생기면, 수정해야될 부분이 많이 생깁니다.

<br>

### 2) @ModelAttribute

`@ModelAttribute` 애노테이션은 입력된 값들과 객체를 매핑해서 값을 받아옵니다. 디폴트로 파라미터로 넘어온 요청 값의 이름과 객체의 필드 명이 일치하면 매핑을 해줍니다.

같은 회원가입 상황에 대해서 @ModelAttribute 를 이용하면 다음과 같이 구현할 수 있습니다. 

```java
class User{
    String id;
    String pwd;
    String email;
}

@PostMapping("/signUp")
public String signUp(@ModelAttribute User user){
    userService.signUp(user);
}
```

<br>

### 3) 차이점

- @RequestParam 애노테이션을 통해 받아온 값이 우리가 매핑하고 싶은 타입과 다르면 `400 Bad Request` 가 발생합니다.

- @ModelAttribute 애노테이션을 통해 받아온 값이 우리가 매핑하고 싶은 타입과 다르다면 `400 Bad Request` 를 발생하는 것이 아니라, 스프링에서 에러 정보를 따로 담아준다.
  - 이때, BindingResult에 담기는데 이는 따로 다른 곳에서 정리하겠다.
- **@ModelAttribute 에 담겨있는 데이터는 자동으로 Model 객체에 추가가 되서 View 로 넘어간다.**

