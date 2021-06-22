# ResourceLoader

### 1) ResourceLoader

`ResourceLoader`는 이름 그대로 리소스를 읽어오는 기능을 제공하는 인터페이스입니다.

핵심 메서드는 `getResource` 하나뿐입니다.

```java
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ResourceLoader resourceLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Resource resource = resourceLoader.getResource("classpath:test.txt");
    }
}

```

 리소스는 다음과 같이 4가지 방법으로 읽어올 수 있습니다.

- 파일 시스템에서 읽어오기
- 클래스패스에서 읽어오기
- URL로 읽어오기
- 상대/절대 경로로 읽어오기

<br>

### 2) Resource

스프링은 자바에 존재하는 일관성 없는 리소스 접근 API를 추상화해서 `Resource`라는 추상화 인터페이스를 정의했습니다. `java.net.URL`을 추상화했고, 기존에 사용하던 `java.net.URL` 은 다음과 같은 기능이 부족했습니다.

- 클래스패스 기준으로 리소스를 읽어오는 기능이 없다.
- `ServletContext`를 기준으로 상대 경로로 읽어오는 기능이 없다.
- 특별한 URL 접미사를 만들어 사용할 수 있지만, 구현이 복잡하고 편의성 메소드가 부족하다.

<br>

우리가 사용하던 ApplicationContext 를 생성하던 코드에서도 내부적으로 `Resource` 가 사용되고 있습니다.

```java
new ClassPathXmlApplicationContext("configuration.xml");
// --> configuration.xml 이라는 텍스트를 내부적으로 resourceLoader.getResource("classpath:configuration.xml") 메소드를 통해 Resource로 얻어오고 ApplicationContext 에게 넘겨준다.
new FileSystemXmlApplicationContext("configuration.xml");
// --> configuration.xml 이라는 텍스트를 내부적으로 파일 시스템 경로에서 얻어와서 Resource로 얻어와서 똑같이 동작한다.
// --> Resource 추상화를 통해 스프링은 모든 리소스를 Resource 인터페이스를 이용해서 다룬다.
```

<br>

#### Resource 주요 메서드

Resource 에는 다음과 같은 주요 메서드가 있습니다.

- getInputStream()
- exist()
- isOpen()
- getDescription()
- isFile()

<br>

#### Resource 구현체

Resource 의 구현체로는 `UrlResource`, `ClassPathResource`, `FileSystemResource`, `ServletContextResource` 등이 있습니다.

- `UrlResource` : 기본으로 http, https, ftp, file, jar 프로토콜을 지원
- `ClassPathResource` : classpath: 접두어를 지원
- `FileSystemResource` : file:/// 접두어를 지원
- `ServletContextResource` : 웹 애플리케이션 루트에서 상대 경로로 리소스를 찾는다.

위에서 `ApplicationContext`를 가지고 올 때, 스프링 내부적으로 `Resource`가 이용된다고 했습니다. 따라서, 우리가 `Resource`를 가지고 올 때, 접두어를 명확하게 명시해주면 어떤 `Resource` 구현체가 동작하는지 명확히 알 수 있습니다.

- `ClassPathXmlApplicationContext("~~")` --> `ClassPathResource`
- `ApplicationContext("classpath:~~")` --> `ClassPathResource`
- `WebApplicationContext("~~")` --> `ServletContextResource`



