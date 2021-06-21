# EnvironmentCapable	

스프링에서는 Bean Factory에서 기능을 확장해서 ApplicationContext 가 스프링 컨테이너 역할을 하고 있습니다. 

확장된 기능 중 하나인 EnvironmentCapable 에 대해서 정리해보겠습니다.

```java
public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory, MessageSource, ApplicationEventPublisher, ResourcePatternResolver { }
```

<br>

### 1) EnvironmentCapable 인터페이스

이름 그대로 로컬, 개발, 운영 등을 구분해서 처리하는 환경변수 기능을 제공해줍니다. 실제 서비스를 운영할 때는 local(개발환경) / dev(테스트, 서버환경) / prod(운영환경) 으로 나누어서 개발을 한다고 합니다.

```java
public interface EnvironmentCapable {
	/**
	 * Return the {@link Environment} associated with this component.
	 */
	Environment getEnvironment();
}
```

`EnvironmentCapable` 인터페이스를 이용하면 다음과 같이 `Environment` 인터페이스에 접근할 수 있습니다. 최종적으로 `Environment` 인터페이스를 통해서 `Profiles` 설정과 `Property` 설정에 접근할 수 있습니다.

<br>

### 2) Profiles

`Profiles` 는 빈들의 그룹으로 `Environment` 를 통해 활성화할 프로파일을 확인하고 설정할 수 있습니다. `Profiles` 를 이용하면 애플리케이션 환경에 따라 약간씩 변경이 되어야 할 설정 값들을 **코드의 변경없이 설정만으로 편리하게 변경**할 수 있습니다. 또, 애플리케이션 환경에 따라 이용해야 되는 빈이 다른 경우에도 @Profile 애노테이션을 이용해 쉽게 해결할 수 있습니다.

```properties
spring:
  profiles:
    active: prod

---
spring:
  profiles: dev
  datasource:
    url: jdbc:h2:testdb
    dirver-class-name: org.h2.Driver
    username: travlebeeee
    password: 1234
  jpa:
    hibernate:
      ddl-auto: create
      
# application.yml
```

<br>

#### 2-1) @Profile

`@Profile` 애노테이션을 이용하면 빈에 어떤 프로파일에서 빈을 사용할지 지정할 수 있습니다.

```java
@Component
@Profiles("test")
// @Profiles("!test") --> test Profile이 아닌 경우에 모두 적용
```

프로파일 이름에는 `!(not)` , `&(and)`, `|(or)` 논리 연산자를 사용할 수 있습니다. 따라서, 여러 조건을 이용해서 사용해야 되는 환경을 지정해 줄 수 있습니다.

<br>

#### 2-2) 활성 프로파일 설정

특정 프로파일에 정의된 빈을 사용하려면 해당 프로파일을 활성 프로파일로 설정해줘야합니다.

JVM 옵션으로 설정할 수 있습니다. 또, 여러 개의 프로파일을 활성화 할 수도 있습니다.

![image](https://user-images.githubusercontent.com/59816811/122521799-37610b80-d050-11eb-8d33-67f2ce07e2c5.png)

```properties
-Dspring.profiles.active=test
```

<br>

#### 2-3) 실습

```java
@Component
@Profile("test")
public class classForTestProfile { }
```

`test` 프로파일에서만 등록되는 빈을 만들고 활성 프로파일 설정 없이 빈을 받아오겠습니다.

```java
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        classForTestProfile bean = ctx.getBean(classForTestProfile.class);
    }
}
```

@Component 애노테이션이 있지만 활성 프로파일이 `test`가 아니므로 에러가 발생합니다.

```
A component required a bean of type 'travelbeeee.study.classForTestProfile' that could not be found.
```

JVM 옵션을 통해 `test` 프로파일로 설정하고 동일한 작업을 해보겠습니다.

```java
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Environment env = ctx.getEnvironment();
        classForTestProfile bean = ctx.getBean(classForTestProfile.class);

        System.out.println("Profile 테스트");
        System.out.println(Arrays.toString(env.getActiveProfiles()));
        System.out.println(bean);
    }
}
```

이번에는 Environment 객체를 통해서 현재 활성 프로파일도 출력해보겠습니다.

```
Profile 테스트
[test] // 현재 활성프로파일
travelbeeee.study.classForTestProfile@a2ddf26 // 빈이 제대로 할당되어있다.
```

<br>

### 3) Properties

프로퍼티란 `키-벨류` 형식의 데이터를 의미합니다. Environment 객체를 통해 애플리케이션에 등록되어 있는 프로퍼티들을 접근할 수 있습니다.

<br>

#### 3-1) @PropertySource

`@PropertySource` 애노테이션에 프로퍼티파일의 경로를 설정해주면 Environment 객체의 getProperty 메소드를 통해 벨류 값을 얻어올 수 있습니다.

```java
@Component
@PropertySource("classpath:/myproperties.properties")
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Environment env = ctx.getEnvironment();

        System.out.println(env.getProperty("my.name"));
    }
}
```

```properties
my.name= travelbeeee

// myproperties.properties
```

> 스프링 부트에서는 @Value 애노테이션을 통해 접근 가능하도록 편하게 도와준다.
>
> ```java
> @Value("${my.name}")
> String myname;
> ```

