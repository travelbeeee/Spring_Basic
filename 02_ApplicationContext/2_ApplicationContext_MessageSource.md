# MessageSource

### 1) MessageSource

`ApplicationContext` 는 `MessageSource`를 상속하고 있습니다. `MessageSource` 인터페이스는 `국제화기능` 을 제공해줍니다.

또, 스프링 부트를 사용한다면 별다른 설정없이 messages.properties 파일을 이용할 수 있습니다. resources 폴더 안에 messages 로 시작하는 properties 파일을 스프링 부트에서 자동으로 읽어준다.

```properties
greeting=안녕, {0}
# messages.properties

greeting=Hello, {0}
# messages_en.properties
```

<br>

```java
@Autowired
ApplicationContext ctx;

@Override
public void run(ApplicationArguments args) throws Exception {
    System.out.println(Locale.getDefault());
    System.out.println(ctx.getMessage("greeting", new String[]{"travelbeeee"}, Locale.getDefault()));
    System.out.println(ctx.getMessage("greeting", new String[]{"travelbeeee"}, Locale.ENGLISH));
}
```

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

