# Singleton

스프링은 주로 웹 어플리케이션을 개발하는데 많이 사용되는 프레임워크입니다.

웹 어플리케이션은 보통 여러 고객에게 동시에 요청이 들어오는데 그럴 때마다 매번 객체를 새롭게 만들어서 할당하게 된다면 메모리 낭비가 심하고 속도 효율이 떨어지게 됩니다.

<img src="https://user-images.githubusercontent.com/59816811/105664416-f1c46a80-5f17-11eb-954e-ccf54d68435c.png" alt="20210114_123446" width="500" />

고객 트래픽이 초당 100이 나온다고 가정하면 초당 100개의 객체가 생성되고 소멸되는데 굉장히 비효율적인 것을 알 수 있습니다.

그래서 아래 그림과 같이 객체를 1개만 생성하고 공유하도록 싱글톤 패턴을 이용해서 설계를 하는 것이 효율적입니다.

<img src="https://user-images.githubusercontent.com/59816811/105664495-23d5cc80-5f18-11eb-9e72-3525c9ba0a2d.png" alt="20210114_124038" width="500" />

<br>

### 1) 싱글톤 패턴이란

싱글톤 패턴은 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴을 말합니다.

객체를 하나만 생성하도록 하여, 생성된 객체를 필요한 경우에 공유해서 사용하는 패턴입니다. 하나의 인스턴스를 메모리에 등록해서 여러 스레드가 동시에 해당 인스턴스를 공유하여 사용하게끔 할 수 있으므로 요청이 많은 곳에서 사용하면 효율을 높여주는 패턴입니다.

<br>

싱글톤 패턴이 적용된 Service 객체를 만들어 보겠습니다.

```java
package hello.core.singleton;

// 싱글톤 패턴으로 만드는 서비스 클래스!
public class SingletonService {

    // static 영역으로 instance를 미리 하나 생성해서 올려둔다.
    private static final SingletonService instance = new SingletonService();

    // 이미 static으로 만들어진 객체를 참조하는 것 말고는 사용 불가능!
    public static SingletonService getInstance(){
        return instance;
    }

    // 생성자를 private으로 막아버린다. --> 새롭게 객체 생성 불가능!
    private SingletonService(){ }
}
```

싱글톤 패턴을 적용하는 방법은 여러가지가 있지만 그 중 가장 간단한 방법으로 싱글톤 패턴을 구현했습니다.

```java
SingletonService singletonService1 = SingletonService.getInstance();
```

Service 객체를 이용해야되면 위와 같이 이용할 수 있습니다. 처음에 static 영역으로 생성된 객체를 계속 공유해서 사용하게 됩니다.

<br>

### 2) 싱글톤 패턴 문제점

우리가 직접 싱글톤 패턴을 구현하려면 다음과 같은 문제가 있습니다.

- 싱글톤 패턴을 구현하는 코드가 많이 추가된다. 내가 원하는건 Service 클래스에는 Service 관련된 로직만 들어갔으면 좋겠는데, 일단 싱글톤 패턴을 위한 코드가 들어가야됨!
- 의존관계상 클라이언트가 구체 클래스에 의존해서 DIP, OCP 원칙을 위반할 가능성이 높다.
- private 생성자로 자식 클래스를 만들기가 어렵고, 내부 속성을 변경하거나 초기화 하기 어렵다.

<br>

### 3) 싱글톤 컨테이너

스프링 컨테이너는 위의 문제점을 해결하면서 싱글톤으로 객체를 관리해줍니다. 그래서 싱글톤 컨테이너라고 불리기도 합니다.

즉, 스프링 컨테이너는 빈으로 등록된 객체를 싱글톤 패턴으로 관리해주면서 위에서 우리가 직접 구현해서 사용했을 때 나오는 문제점을 다 해결해줍니다! ( 역시 스프링 갓... )

그럼 스프링은 어떻게 싱글톤으로 객체를 관리할 수 있는걸까??

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
}
```

@Configuration, @Bean 애노테이션을 이용해서 스프링 컨테이너에 빈을 등록하는 상황이라고 가정해보자. MemberService 객체를 빈으로 등록하기 위해서 memberRepository() 메서드를 호출하게 되고 이는 new MemoryMemberRepository() 를 실행하게 된다. 또, MemberRepository 객체를 빈으로 등록하기 위해서 new MemoryMemberRepository() 를 실행하게 된다.

그럼 결국 new MemoryMemberRepository() 가 두 번 실행되게 되는데 어떻게 싱글톤으로 객체를 관리할 수 있는걸까??

<img src="https://user-images.githubusercontent.com/59816811/105666662-196a0180-5f1d-11eb-8721-a7a3a10cb675.png" alt="20210114_124921" width="500" />

스프링 컨테이너에 @Configuration 어노테이션을 통해 AppConfig.class 를 등록하게 되면 AppConfig 클래스도 스프링 빈으로 등록된다. 그리고 AppConfig 클래스를 그대로 상속하면서 싱글톤이 보장되도록 확장된 **AppConfig@CGLIB 클래스가 새롭게 만들어지고** **스프링에서는 AppConfig 클래스가 아니라 AppConfig@CGLIB 클래스를 이용하게 된다.**

AppConfig@CGLIB 클래스에서는 아래 코드와 같이 이미 스프링 컨테이너에 등록되어있는 빈은 찾아서 반환해주고 없다면 새롭게 만들어주면서 싱글톤으로 빈을 관리해준다.

```java
@Bean
public MemberRepository memberRepository() {
		if (memoryMemberRepository가 이미 스프링 컨테이너에 등록되어 있으면?) { 
				return 스프링 컨테이너에서 찾아서 반환;
		} else { //스프링 컨테이너에 없으면
				기존 로직을 호출해서 MemoryMemberRepository를 생성하고 스프링 컨테이너에 등록 return 반환
		}
}
```

이를 통해 우리가 AppConfig.class 에서 @Configuration, @Bean 어노테이션만 사용한다면 싱글톤이 보장이 되는 것이다. 다른 방법으로 빈을 등록해도 마찬가지다.

> 주의!!
>
> @Configuration 애노테이션 없이 Appconfig.class 를 빈 메타정보로 넘겨주면, 싱글톤이 보장이 되지않고, new Bean() 으로 객체를 직접 만드는 것과 동일하게 작동한다.

<br>

### 4) 싱글톤 방식 주의점

싱글톤 패턴을 이용하면 객체 인스턴스를 하나만 생성해서 공유하게 된다. 따라서 **싱글톤 객체는 상태를 유지 (stateful) 하게 설계하면 안되고 항상 무상태(stateless)로 설계**해야한다!!!

- 특정 클라이언트에 의존적인 필드가 있으면 안된다.
- 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
- 가급적 읽기만 가능하게! 변경 X

<br>

그럼 stateful 하게 설계하면 어떤 문제가 발생할까?? 간단하게 알아보자.

```java
public class StatefulService {
    private int price; //상태를 유지하는 필드
    
    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price; //여기가 문제!
    }
    
    public int getPrice() {
        return price;
    }
}
```

order 메소드에서 필드 값을 주입하는 부분이 있다. 즉, 클라이언트가 값을 변경할 수 있다.

```java
ApplicationContext ctx = new
AnnotationConfigApplicationContext(TestConfig.class);

StatefulService statefulService1 = ac.getBean("statefulService",
StatefulService.class);

StatefulService statefulService2 = ac.getBean("statefulService",
StatefulService.class);

//ThreadA: A사용자 10000원 주문
statefulService1.order("userA", 10000);

//ThreadB: B사용자 20000원 주문
statefulService2.order("userB", 20000);
```

사용자 A와 사용자 B가 Service를 이용해 order 메소드를 이용했다고 해보자.

먼저, 사용자 A가 price 값에 10000을 저장하지만 사용자 B가 price 값에 20000을 저장하면서 사용자 A가 저장한 값이 사라지게 된다.

이게 만약 실제 결제와 관련된 부분이라고 생각하면 사용자 A는 10000원을 결제 요청했지만, 사용자 B가 20000원을 바로 결제 요청을 하면서 결제 값이 20000원으로 바뀌게 된다. 즉, A는 20000원을 결제하게되는 말도 안되는 상황이 발생한다!!

**--> 항상 항상 stateless 하게 설계하자!**

<br>

