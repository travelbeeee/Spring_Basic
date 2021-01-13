# IoC Container

저번 01_What_Is_IoC 글에서 IoC 가 무엇이고 IoC 기술을 이용하면 객체 지향적인 프로그래밍을 할 수 있다고 정리했습니다. 그러면, 스프링에서는 어떻게 IoC 기술을 제공해줄까요??

**IoC Container 는 오브젝트의 생성과 관계설정, 사용, 제거 등의 작업을 대신 해주는 IoC 기술을 제공**해줘서 붙여진 이름입니다.

스프링에서는 IoC Container 역할을 **"ApplicationContext" 인터페이스를 구현한 구현체가 해주고 이를 Spring Container** 라고 부릅니다. 또, **Spring Container 에서 관리하는 객체들을 Bean 이라고 부릅니다.**

Spring Container 역할을 하는 ApplicationContext 인터페이스에 대해서 먼저 알아보고, Bean 을 어떻게 등록하는지, 왜 다양한 방법으로 Bean 등록이 가능한지 순서대로 알아보겠습니다.

<br>

### 1) BeanFactory / ApplicationContext

스프링 BeanFacotry 인터페이스와 ApplicationContext 인터페이스를 알아보자.

##### BeanFactory

- 스프링 컨테이너의 최상위 인터페이스
- 스프링 빈을 관리하고 조회하는 역할을 담당
- getBean() 메소드 등을 제공

BeanFactory 인터페이스를 보면 위에서 언급한 빈에 관한 대부분의 기능을 제공해준다.

그러면 왜 BeanFactory 인터페이스가 아니라 ApplicationContext 인터페이스의 구현체를 Spring Container 라고 부를까??

**ApplcationContext 인터페이스는 먼저 BeanFactory 기능을 모두 상속받아서 제공한다. 또, 이 외에도 아래 그림과 같은 인터페이스들을 상속받아서 제공한다.** 애플리케이션을 개발할 때는 빈을 관리하고 조회하는 기능은 물론이고, 수 많은 부가기능이 필요한데 ApplicationContext 인터페이스는 이런 수 많은 부가기능을 담고 있다. 따라서, Spring Container 로 실제 사용하는 것을 BeanFactory 인터페이스를 상속한 ApplicationContext 인터페이스를 구현한 구현체가 된다.

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

그럼 ApplicationContext 를 구현한 구현체가 Spring Container 역할을 해주는 것은 알겠고, 이 Spring Container에 어떻게 빈을 설정 할 수 있는지 알아보자.

IoC 컨테이너가 관리하는 Bean으로 등록하기 위해서는 적절한 메타정보를 알려줘야하는데, 반드시 들어가야하는 메타 정보는 **"클래스이름"** 과 **"빈의 이름"** 입니다. **빈의 이름은 명시하지 않는 경우 클래스 이름에서 첫 글자를 소문자로 바꿔서 사용하게 됩니다.**

메타정보를 작성하는 방법은 크게 XML, @Configuration, @Component 등록 세 가지로 나뉩니다.

<br>

#### 2-1) @Component



#### 2-2) @Configuration

Java Class 파일을 이용해서 Spring Container 에게 빈에 관한 정보를 알려줄 수 있습니다.

Bean 메타 정보를 담고 있다는 뜻의 @Configuration 어노테이션을 붙여주고, 각 메서드에 @Bean 어노테이션을 붙여주면 스프링 컨테이너에 스프링 빈을 등록할 수 있습니다.

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

AnnotationConfigApplicationContext 객체를 이용해서 class 정보를 넘기면 스프링 컨테이너를 이용할 수 있습니다.

```java
ApplicationContext applicationContext = new
AnnotationConfigApplicationContext(AppConfig.class);
```



#### 2-3) XML

XML을 이용해서 Spring Container 에게 빈에 관한 정보를 알려줄 수 있습니다. 요즘에는 많이 쓰지 않는 방법이라고 합니다.

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

<constructor-arg> 태그는 의존 관계가 있는 Bean을 생성자를 통해 주입할 때 사용한다. 



다음과 같이 GenericXmlApplicationContext 객체를 이용해서 xml 설정 파일을 넘기면 스프링 컨테이너를 사용할 수 있다.

```java
ApplicationContext ctx = new GenericXmlApplicationContext("appConfig.xml")
```

  