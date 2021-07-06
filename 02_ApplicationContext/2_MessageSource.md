# MessageSource

### 1) MessageSource

`ApplicationContext` 는 `MessageSource`를 상속하고 있습니다. `MessageSource` 인터페이스는 `메시지관리기능`을 제공해줍니다.

메시지 관리 기능을 사용하려면 `MessageSource`를 스프링 빈으로 등록해야되는데, 구현체인 `ResourcebundleMessageSource`를 스프링 빈으로 등록하면 된다.

```java
@Bean
public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasenames("messages");
    messageSource.setDefaultEncoding("utf-8");
    return messageSource;
}
```

> 스프링부트에서는 `MessageSource`를 자동으로 빈으로 등록해준다.

- `basenames` : 설정 파일의 이름을 지정합니다.
  - `messages` 를 지정하면 `messages.properties` 파일을 읽어서 사용하고, 국제화 기능도 제공해주기 때문에 `messages_en.properties` 와 같은 파일도 다 읽어준다.
  - 파일의 위치는 `resources` 폴더 안에 지정하면 된다.
- `defaultEncoding` : 인코딩 정보를 지정한다.

> 스프링 부트를 사용한다면 별다른 설정없이 `Resource` 폴더 안에 `messages.properties` 파일을 만들어서 이용할 수 있습니다. 
>
> 추가로 설정을 원한다면 `applicaton.properties`에서 설정을 추가할 수 있습니다. spring.messages.basename=messages 를 통해 설정 파일의 이름을 지정할 수 있습니다. message.messages 로 지정하면 /resources/message 폴더 안에 있는 messages.properties를 찾아줍니다.

<br>

`MessageSource`는 `getMessage` 메소드를 지원해주고, 이를 통해 메시지를 읽어올 수 있습니다.

```java
public interface MessageSource {
	@Nullable
	String getMessage(String code, @Nullable Object[] args, @Nullable String defaultMessage, Locale locale);

	String getMessage(String code, @Nullable Object[] args, Locale locale) throws NoSuchMessageException;
}
```

```java
// code : "hello", args = null, locale = null --> default locale인 Korea --> messages.properties의 "hello"
ms.getMessage("hello", null, null);  // == "안녕"

// code : "no_code", args = null, locale = null --> messages.properties의 "no_code" --> 없음! 에러 발생
ms.getMessage("no_code", null, null)) // NoSuchMessageException.class

// code : "no_code", args = null, defaultMessage = "기본 메시지" locale = null 
// --> messages.properties의 "no_code" --> 없음! --> 기본 메시지로 변환!
ms.getMessage("no_code", null, "기본 메시지", null); // == "기본 메시지"

// code : "hello", args = {"Spring"}, locale = null 
// --> default locale인 Korea --> messages.properties의 "hello.name"
ms.getMessage("hello.name", new Object[]{"Spring"}, null); // == "안녕 Spring"

// code : "hello", args = null, locale = ENGLISH
// --> locale ENGLISH --> messages_en.properties의 "hello"
// messages_en.properties가 없다면 Default인 messages.properties 탐색
ms.getMessage("hello", null, Locale.ENGLISH) // == "hello"

// messages.properties
hello=안녕
hello.name=안녕 {0}
// messages_en.properties
hello=hello
hello.name=hello {0}
```

<br>

`ApplicationContext`는 `MessageSource`를 상속하고 있고, 또 빈으로 등록되어있기 때문에 다음과 같이 직접 `MessageSource` 빈을 얻어와서 사용할 수도 있고, `ApplicationContext.getMessage()`를 이용할 수도 있습니다.

```java
@SpringBootApplication
public class Application implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // ctx.getMessage()
        MessageSource bean = ctx.getBean(MessageSource.class);
        System.out.println(bean.getClass()); 
        // class org.springframework.context.support.ResourceBundleMessageSource
    }
}
```

<br>

`ApplicationContext` 의 getMessage 메소드를 이용해 properties 파일에 등록한 메시지를 사용할 수 있습니다.

<br>

### 2) ResourceBundleMessageSource

`ResourceBundleMessageSource` 를 이용하면 릴로딩 기능을 이용할 수 있습니다.

스프링 부트에서는 `MessageSource`를 상속하는 `ResourceBundleMessageSource` 클래스가 자동으로 빈으로 등록되고 Message를 자동으로 읽어준다. ( 원래는 따로 Bean으로 등록해야된다. )

```java
@Bean
public MessageSource messageSource(){
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:/messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setCacheSeconds(3);
    return messageSource;
}
```

