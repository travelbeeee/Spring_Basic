# SpringMVC_Head,Options메소드

HTTP메소드에는 Head, Options 메소드가 있습니다. Spring MVC에서는 어떤 방식으로 Head, Options 메소드를 지원해주는지 정리해보겠습니다.

### 1) Head 메소드

먼저, Head 메소드는 Get 메소드와 동일하지만 이름 그대로 HTTP Head 데이터만 요청하는 메소드입니다. 따라서, Get 메소드 요청에 대한 응답만 해주면 됩니다.

- Head 메소드는 @GetMapping 을 이용해서 처리할 수 있다.

```java
@RestController
public class TestController {

    @GetMapping("/methodTest")
    public void methodTestGet(){
    }
}
```

Post 맨을 이용해서 `http://localhost:8080/methodTest` 에 `Head` 메소드로 요쳥을 해보겠습니다.

![image-20211001232440343](https://user-images.githubusercontent.com/59816811/135636547-5318ea30-8075-4bd7-9ea2-9c12b0d9ad42.png)

`200 OK` 와 함께 `HTTP Head` 정보가 응답되는 것을 확인 할 수 있습니다.

<br>

`@GetMapping` 이 없다면 어떻게 될까요??

```java
@RestController
public class TestController {

//    @GetMapping("/methodTest")
//    public void methodTestGet(){
//    }
}
```

![image-20211001232643964](https://user-images.githubusercontent.com/59816811/135636863-fe5b024a-c14b-4e95-be37-36b563387e3f.png)

`405 Method Not Allowed` 에러가 발생하는 것을 확인 할 수 있습니다.

<br>

### 2) Options 메소드

`Options 메소드` 는 대상 리소스에 대한 통신 기능 옵션을 요청할 때 사용하는 메소드입니다.

```java
@RestController
public class TestController {

    @GetMapping("/methodTest")
    public void methodTestGet(){
    }

    @PostMapping("/methodTest")
    public void methodTestPost(){
    }

    @DeleteMapping("/methodTest")
    public void methodTestDelete(){
    }
}
```

이번에는 `@PostMapping` `@DeleteMapping` 을 추가해보겠습니다.

마찬가지로 포스트맨을 이용해 `http://localhost:8080/methodTest` 에 `Options 메소드`로 요청을 해보겠습니다.

![image-20211001232951186](https://user-images.githubusercontent.com/59816811/135637256-e7265e86-5418-4370-90f9-ddd7a0e6dd31.png)

`Allow` 헤더 필드에 선택 가능한 메소드 옵션이 응답으로 온 것을 확인 할 수 있습니다.