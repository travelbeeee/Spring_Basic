# IoC Container

스프링에서는 IoC 기술을 제공해주는 역할을 하는 객체를 IoC Container 혹은 Spring Container 라고 부릅니다.**IoC Container 는 객체의 생성과 관계설정, 사용, 제거 등의 작업을 대신 해주는 IoC 기술을 제공**해줘서 붙여진 이름입니다.

**위와 같은 일을 하는 오브젝트를 팩토리 (factory) 라고도 부르고 팩토리가 제어권을 가지고 만들고 관계를 부여하는 오브젝트를 빈(bean) 이라고 부른다.  그래서 다른 말로는 빈 팩토리(bean factory)라고도 부릅니다.**

전의 포스팅에서 살펴보았듯이 우리가 직접 IoC 컨테이너 역할을 하는 오브젝트를 구현해도 되지만 스프링에서 제공하는 IoC 컨테이너를 이용하면 스코프, 콜백 등 더욱 다양한 편리한 기능들을 이용할 수 있습니다.

<img src="https://user-images.githubusercontent.com/59816811/104285295-c04ea680-54f6-11eb-96be-7cdd3b2fc3d3.png" alt="pic1" width="500"/>

<br>

스프링에서는 IoC Container 역할을 **"ApplicationContext" 인터페이스를 구현한 구현체가 해줍니다.** 

Spring Container 역할을 하는 ApplicationContext 인터페이스에 대해서 먼저 알아보고, Bean 을 어떻게 등록하는지, 왜 다양한 방법으로 Bean 등록이 가능한지 순서대로 알아보겠습니다.

<br>

### 1) BeanFactory / ApplicationContext 인터페이스

스프링에는 BeanFacotry 인터페이스와 ApplicationContext 인터페이스가 있습니다.

##### BeanFactory

- 스프링 컨테이너의 최상위 인터페이스
- 스프링 빈을 관리하고 조회하는 역할을 담당
- getBean() 메소드 등을 제공

BeanFactory 인터페이스를 보면 IoC 기술을 위한 대부분의 기능을 제공해줍니다.

그러면 왜 BeanFactory 인터페이스가 아니라 ApplicationContext 인터페이스의 구현체를 Spring Container 라고 부를까요??

**ApplicationContext 인터페이스는 먼저 BeanFactory 기능을 모두 상속받아서 제공한다. 또, 이 외에도 아래 그림과 같은 인터페이스들을 상속받아서 제공한다.** 애플리케이션을 개발할 때는 빈을 관리하고 조회하는 기능은 물론이고, 수 많은 부가기능이 필요한데 ApplicationContext 인터페이스는 이런 수 많은 부가기능을 담고 있다. 따라서, Spring Container 로 실제 사용하는 것을 BeanFactory 인터페이스를 상속한 ApplicationContext 인터페이스를 구현한 구현체가 된다.

![spinrg2_1](https://user-images.githubusercontent.com/59816811/104418961-1dac2b80-55bb-11eb-810d-deb9aaa9d6f5.png)

- MessageSource 인터페이스
  - 메시지 소스를 활용한 국제화 기능 제공
- EnvironmentCapable
  - 로컬, 개발, 운영등을 구분해서 처리하는 환경변수 기능 제공
- ApplicationEventPublisher
  - 이벤트를 발행하고 구독하는 모델을 편리하게 지원하는 애플리케이션 이벤트 기능 제공
- ResourceLoader
  - 파일, 클래스패스, 외부 등에서 리소스를 편리하게 조회하는 기능 제공

<br>

### 2) 다양한 빈 설정 방법

ApplicationContext를 구현한 Spring Container 에 Bean을 등록하는 방법과 Spring Container 를 사용하는 방법에 대해서 알아보겠습니다.

IoC 컨테이너가 관리하는 Bean으로 등록하기 위해서는 적절한 메타정보를 알려줘야하는데, 반드시 들어가야하는 메타 정보는 **"클래스이름"** 과 **"빈의 이름"** 입니다. **빈의 이름은 명시하지 않는 경우 클래스 이름에서 첫 글자를 소문자로 바꿔서 사용하게 됩니다.**

메타정보를 작성하는 방법은 크게 @Configuration, XML, @ComponentScan 세 가지로 나뉩니다.

<br>

#### 2-1) @Configuration

Java Class 파일을 이용해서 Spring Container 에게 빈에 관한 정보를 알려줄 수 있습니다.

Bean 메타 정보를 담고 있다는 뜻의 @Configuration 어노테이션을 붙여주고, 각 메서드에 @Bean 어노테이션을 붙여주면 스프링 컨테이너에 스프링 빈을 등록할 수 있습니다.

클래스 파일에서 한 눈에 Bean 들의 의존관계를 파악할 수 있기 때문에 자주 사용됩니다.

```java
@Configuration
public class AppConfig {

    @Bean
    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy(){
        return new RateDiscountPolicy();
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

}

```

- @Bean으로 등록된 메서드의 return 객체를 Bean으로 등록합니다.
- @Bean("name")으로 이름을 지정할 수 있습니다. 이름을 지정하지 않을 시에는 메소드 명이 이름이 됩니다.

AnnotationConfigApplicationContext 객체를 이용해서 Java class 파일 정보를 넘기면 스프링 컨테이너에 Java Class 파일에 담겨있는 Bean 정보들이 등록이 됩니다.

```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
```

ApplicationContext 를 구현한 AnnotationConfigApplicationContext 가 스프링 컨테이너의 역할을 하게 됩니다.

<br>

#### 2-2) XML

XML 파일을 이용해서 Spring Container 에게 빈에 관한 정보를 알려줄 수 있습니다. 요즘에는 많이 쓰지 않는 방법이라고 합니다.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="memberService" class="hello.core.member.MemberServiceImpl">
        <constructor-arg name="memberRepository" ref="memberRepository"/>
    </bean>

    <bean id="memberRepository" class="hello.core.member.MemoryMemberRepository"/>

    <bean id="discountPolicy" class="hello.core.discount.RateDiscountPolicy"/>

    <bean id="orderService" class="hello.core.order.OrderServiceImpl">
        <constructor-arg name="memberRepository" ref="memberRepository"/>
        <constructor-arg name="discountPolicy" ref="discountPolicy"/>
    </bean>
</beans>
```

<bean> 태그를 이용해서 스프링 컨테이너에 빈을 등록할 수 있다. 

- id : 빈 이름 설정
- class : 빈 타입 설정 ( 필수 속성 )
- scope : 빈 scope 설정  ( singleton / prototype )
- primary : true 일 경우 같은 타입의 빈이 여러개면 우선적으로 사용
- <constructor-arg> 태그는 의존 관계가 있는 Bean을 생성자를 통해 주입할 때 사용
  - ref 는 다른 빈으로 등록이 되어있는 빈의 id를 설정해줘야한다.
- <property> 태그는 setter를 통해서 의존 관계까 있는 Bean을 주입할 때 사용

다음과 같이 GenericXmlApplicationContext 객체를 이용해서 xml 설정 파일을 넘기면 스프링 컨테이너를 사용할 수 있다.

```java
ApplicationContext ctx = new GenericXmlApplicationContext("appConfig.xml");
ApplicationContext ctx = new ClassPathXmlApplicationContext("appConfig.xml");
```

  <br>

#### 2-3) @ComponentScan / @Component

XML과 @Configuration 방법은 Bean을 하나하나 다 등록해줘야하는 단점이 있습니다. 이를 Component-Scan을 이용하면 해결할 수 있습니다. @ComponentScan 애노테이션을 이용해도 되고 XML 파일에 <component-scan> 태그를 이용해도 됩니다.

```xml
<context:component-scan base-package=""/>
```

@ComponentScan 애노테이션을 이용하면 이름 그대로 @Component 로 등록된 클래스들을 찾아서 Scan을 합니다. 그리고 찾은 Component 들을 Bean으로 등록해줍니다.

우리가 빈으로 등록을 원하는 클래스들에게는 @Component 애노테이션을 설정해주면 되고, 다음과 같이 @ComponentScan 애노테이션을 이용한 Bean 메타 정보를 담고 있는 @Configuration 클래스를 만들어 주면 됩니다.

```java
@Configuration
@ComponentScan
public class AutoAppConfig{
    
}
```

@Component 애노테이션이 있으면 컴포넌트 스캔의 대상이 된다고 했습니다. 아래의 애노테이션들은 @Component 애노테이션을 확장한 애노테이션들로 @Component 애노테이션 보다는 상황에 맞는 애노테이션을 사용하는 것이 더 좋습니다.

- @Component : 컴포넌트 스캔의 대상으로 등록
- @Controller : 스프링 MVC 컨트롤러에서 사용하는 애노테이션으로 @Component 애노테이션을 내포하고 있다.
- @Service : 스프링 비지니스 로직에서 사용하는 애노테이션으로 @Component 애노테이션을 내포하고 있다. ( 특별한 기능은 없지만 핵심 비지니스 로직이 있다는 뜻으로도 사용 )
- @Repository : 스프링 비지니스 로직에서 사용하는 애노테이션으로 @Component 애노테이션을 내포하고 있다. 또, 스프링이 데이터 접근 계층으로 인식하고 데이터 계층의 예외를 스프링 예외로 변환해준다.
- @Configuration : 스프링 설정 정보에서 사용하는 애노테이션으로 @Component 애노테이션을 내포하고 있다. 스프링이 싱글톤을 유지하도록 추가 처리를 한다.

<br>

따라서 @Configuration 애노테이션도 @Component 애노테이션을 포함하고 있으므로 자동으로 스프링 컨테이너에 등록이 됩니다.

```java
@Component
public class MemoryMemberRepository implements MemberRepository {
    // ~~ 핵심 비지니스 로직 코드 ~~
}
```

스프링 컨테이너에 Bean으로 등록을 원하는 클래스들에 @Component 애노테이션을 붙여주면 됩니다.

그러면 의존관계 주입은 어떻게 되는걸까요??

@Autowired 애노테이션을 이용하면 됩니다.

```java
@Component
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
   
    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
    	this.memberRepository = memberRepository;
    }
}
```

@Autowired 애노테이션을 이용하면 Component Scan 중에 자동으로 의존관계를 주입해줍니다. 물론 주입받은 MemberRepository 를 @Component 로 설정해놓아야 주입받을 수 있습니다. 

- @Component("name") 을 통해서 Bean의 이름을 지정할 수도 있습니다.

- ComponentScan 은 스캔 위치를 지정할 수 있습니다.

  모든 자바 클래스를 다 스캔하려면 시간이 오래걸리므로 스캔 위치를 다음과 같이 지정할 수 있습니다.

  ```java
  // 단일 지정
  @ComponentScan(
  	basePackages = "hello.core",
  )
  
  // 복수 지정
  @ComponentScan(
  	basePackages = {"hello.core", "hello.core2"},
  )
  ```

  hello.core 패키지 하위 자바 클래스를 다 컴포넌트 스캔 해달라는 뜻이고, Default 값으로는 @ComponentScan이 붙은 설정 정보 클래스의 패키지가 시작 위치가 됩니다.

- ComponentScan 은 필터를 지정할 수 있습니다.

  - includeFilters : 컴포넌트 스캔 대상을 추가로 지정
  - excludeFilters : 컴포넌트 스캔 대상에서 제외

  ```java
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface MyIncludeComponent {
  }
  ```

  ```java
  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface MyExcludeComponent {
  }
  ```

  ```java
  @Configuration
  @ComponentScan(
      includeFilters = @Filter(type = FilterType.ANNOTATION, classes =
      MyIncludeComponent.class),
      excludeFilters = @Filter(type = FilterType.ANNOTATION, classes =
      MyExcludeComponent.class)
  )
  public class autoConfig{
  
  }
  ```

  위와 같이 커스텀 애노테이션을 만들어주고 @ComponentScan 의 includeFilters, excludeFilters 속성에 애노테이션을 지정해주면 @MyIncludeComponent 가 붙은 class는 빈으로 등록이 되고, @MyExcludeComponent 이 붙은 class는 빈에서 제외가 됩니다.

  - FilterType 으로는 5가지 옵션이 있습니다.

    - ANNOTATION : 기본값, 애노테이션을 인식해서 동작
    - ASSIGNABLE_TYPE : 지정한 타입과 자식 타입을 인식해서 동작
    - ASPECTJ : AspectJ 패턴 사용
    - REGEX : 정규 표현식
    - CUSTOM : TypeFilter 라는 인터페이스를 구현해서 처리

<br>

마찬가지로 AnnotationConfigApplicationContext 구현체를 통해 스프링 컨테이너를 사용할 수 있습니다.

```java
ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
```

<br>

#### 2-4) 정리

XML 파일을 이용하는 방법은 이용하지 않는 추세이고 @Component, @Configuration 방법을 같이 이용해서 빈 등록을 하는 추세라고 한다. @Component 를 이용하는 방법은 @Service, @Controller, @Repository 등 개발을 할 때 어느 정도 정형화된 객체들을 빈으로 등록할 때 이용하고, @Configuration 방법은 의존 관계 파악이 한 눈에 필요한 객체들을 빈으로 등록할 때 이용한다고 한다.

**--> @Component & @Configuration 방법을 이용하자!**

<br> 