View 단에서 multipart file이 넘어오면 파일이 제대로 등록되었는지 어떻게 알 수 있을까?

```html
<form action="/member/profile" th:object="${profileForm}" method="post" enctype="multipart/form-data">
    <input type="file" th:field="*{profile}" value="프로필사진을 등록해주세요.">
    <p th:if="${#fields.hasErrors('profile')}" th:errors="*{profile}">Incorrect data</p>
    <input type="submit" value="등록하기">
</form>
```

--> 다른 값들처럼 @NotNull, @NotEmpty, @Valid 등의 애노테이션을 이용해서 잡을 수 없다.

```java
MultipartFile file; // --> 스프링이 Class StandardMultipartHttpServletRequest를 주입해줌
```

따라서, 스프링이 주입해주는 Class StandardMultipartHttpServletRequest 의 isEmpty 메소드를 이용해서 체크해야된다.

```java
public class ProfileForm {
//    @NotNull(message = "프로필 사진을 등록해주세요.")
    MultipartFile profile;
}
```

```java
if (profileForm.getProfile().isEmpty()) {
	return "/member/profile";
}
```

