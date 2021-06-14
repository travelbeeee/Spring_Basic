# @Autowired

앞에서 의존관계를 @Autowired 애노테이션을 이용해 자동 주입 할 수 있다고 했다.

자동주입하는 4가지 방법에 대해서 알아보자.

<br>

### 1) 의존관계 자동주입

#### 1-1) 생성자 주입

이름 그대로 생성자를 통해서 의존 관계를 주입 받는 방법.

- 생성자 호출 시점에 딱 1번만 호출되는 것이 보장 --> **불변**이 보장된다.

```java
@Component
public class OrderServiceImpl implements OrderService {
    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;
    
    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy
    discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

> @Autowired 가 없어도 생성자가 딱 1개만 있으면 자동으로 주입 된다. ( 스프링 빈에 한해서만 )

<br>

#### 1-2) 수정자 주입

setter 를 이용해서 의존관계를 주입하는 방법

- **선택, 변경** 가능성이 있는 의존관계에 사용

```java
@Component
public class OrderServiceImpl implements OrderService {
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;
    
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
    	this.memberRepository = memberRepository;
    }
    
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
    	this.discountPolicy = discountPolicy;
    }
}
```

setter 를 이용해서 의존 관계를 바꿔줄 수도 있으므로 Test 에서는 따로 쓰기에 편하다. **하지만, 변경 가능성이 있으므로 위험하다...!**

<br>

#### 1-3) 필드 주입

이름 그대로 필드에 바로 주입하는 방법

- 코드가 간결하지만 외부에서 변경이 불가능해서 테스트하기 힘들다

  --> 치명적인 단점

- DI 프레임워크가 없으면 아무것도 할 수 없다

```java
@Component
public class OrderServiceImpl implements OrderService {
    @Autowired
   	private MemberRepository memberRepository;
    @Autowired
   	private DiscountPolicy discountPolicy;
    
}
```

테스트 코드에서는 사용해도 되지만 애플리케이션의 실제 코드로는 사용하지말자!!

<br>

#### 1-4) 일반 메서드 주입

의존관계를 주입해주는 일반 메서드를 통해서 주입 받는 방법

- 일반적으로 사용 X

```java
@Component
public class OrderServiceImpl implements OrderService {
   	private MemberRepository memberRepository;
   	private DiscountPolicy discountPolicy;
    
    @Autowired
    public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy){
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
}
```

<br>

### 2) 생성자 주입을 써야되는 이유

- 불변! 대부분의 의존 관계 주입은 한 번 일어나면 애플리케이션 종료시점까지 의존관계를 변경할 일이 없다 . 오히려 대부분은 변경하면 안된다.

- 수정자 주입을 사용하면 setter 메서드를 public 으로 열어두어야 하므로, 개발자가 setter 메서드를 잘못 사용해 문제가 생길 수 있다.

  --> 다른 개발자가 실수로 변경할 수도 있고, 변경하면 안되는 메서드를 public 으로 열어두는 것 자체가 좋은 설계 방법이 아니다.

- 생성자 주입을 사용하면 필드에 final 키워드를 사용해 혹시라도 값이 설정되지 않는 오류를 컴파일 시점에 막아준다.

  - 생성자 주입을 제외한 다른 주입 방식은 모두 생성자 이후에 호출되므로, 필드에 final 키워드를 사용 할 수 없다.

**예시)**

```java
@Component
public class OrderServiceImpl implements OrderService {
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;
    
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
    	this.memberRepository = memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
    	this.discountPolicy = discountPolicy;
    }
    
    // ~~ OrderServiceImpl 비지니스 로직 ~~
}
```

Test 코드에서 위의 OrderServiceImpl 을 사용한다고 가정하자. 생성자 주입이 아니라 setter 주입으로 OrderServiceImpl 클래스의 의존관계를 설정해주었다.

```java
@Test
void createOrder() {
    OrderServiceImpl orderService = new OrderServiceImpl();
    orderService.createOrder(1L, "itemA", 10000);
}
```

**위처럼 프레임워크 없이 순수한 자바 코드로만 테스트를 수행하면 new 키워드를 이용해서 orderServiceImpl 객체에 의존 관계 주입을 하지 않고도 생성할 수 있다.**

**하지만, 막상 실행해보면 Null Point Exception 에러가 발생한다.**

**생성자 주입으로 작성하면 실행 전에 컴파일에서 오류가 발생하므로 IDE 에서 바로 어떤 값을 주입해야하는지 알 수 있다.**

<br>

### 3) 주입할 빈이 없는 경우

주입할 스프링 빈이 없으면 당연히 에러가 발생한다. 이때, 스프링 빈이 없어도 동작해야 하면 다음과 같은 설정을 이용할 수 있다.

#### 3-1) @Autowired(required=false)

Default 값은 true로 되어있고, false로 변경해주면 자동 주입할 대상이 없으면 호출이 안된다.

```java
public class MemberServiceImpl implements  MemberService{

    private MemberRepository memberRepository;

    @Autowired(required=false)
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}

```

<br>

#### 3-2) @Nullable 애노테이션

자동 주입할 대상이 없으면 null 을 주입해준다.

```java
public class MemberServiceImpl implements  MemberService{

    private MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(@Nullable MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
}

```

<br>

#### 3-3) Optional<>

자동 주입할 대상이 없으면 Optional.empty 를 주입해준다.

```java
public class MemberServiceImpl implements  MemberService{

    private MemberRepository memberRepository;

    @Autowired(required=false)
    public MemberServiceImpl(Optional<MemberRepository> memberRepository) {
        this.memberRepository = memberRepository;
    }
}

```

<br>

### 4) 주입할 빈이 중복되는 경우

기본적으로 @Autowired 는 주입해야하는 빈을 타입(Type) 으로 조회한다.

같은 타입(Type)의 빈이 2개 이상 있다면 `NoUniqueBeanDefinitionException` 오류가 발생한다.

DiscountPolicy Interface 가 있고, RateDiscountPolicy 구현체, FixDiscountPolicy 구현체가 모두 빈으로 등록되어있는 상황이라고 하자.

#### 4-1) @Autowired 필드 명 매칭

@Autowired 를 타입 매칭을 시도하고, 빈이 여러개 있으면 필드 이름, 파라미터 이름으로 빈 이름을 추가 매칭한다.

```java
@Autowired
private DiscountPolicy rateDiscountPolicy; // 필드이름 변경
```

필드 이름을 원하는 클래스에 맞춰서 설정하면 빈이 여러개 있을 때 문제를 해결 할 수 있다.

```java
public class OrderServiceImpl implements OrderService{

    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;

	@Autowired
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy rateDiscountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = rateDiscountPolicy;
    }
}
```

위처럼 파라미터의 이름을 변경해줘도 된다.

> 주의 ! 
>
> 필드명 매칭은 타입 매칭을 시도 하고 그 결과에 빈이 여러 개 있을 때 추가로 동작하는 기능.

<br>

### 4-2) @Qualifier

@Qualifier 애노테이션을 통해 추가 구분자를 붙여줄 수도 있다. 주입시 추가적인 방법을 제공하는 것이지 빈 이름을 변경하는 것은 아니다.

```java
@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy {}
```

위와 같이 RateDiscountPolicy 에 @Qualifier 애노테이션을 통해 "mainDiscountPolicy" 라는 이름의 추가 구분자를 붙여줬다.

```java
@Autowired
public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
    
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
    
}
```

자동주입 시에 @Qualifier 를 붙여주고 등록한 이름을 적어주면 중복 빈 문제를 해결할 수 있다.

> 스프링은 기본적으로 빈으로 등록할 때, 클래스 이름의 앞 글자를 소문자로 바꾸어서 빈 이름으로 등록합니다. 따라서 RateDiscountPolicy 클래스에서 @Qualifier를 통한 이름을 설정하지않고도,  @Qualifier("rateDiscountPolicy") 로 주입받아도 된다.

<br>

### 4-3) @Qualifier 단점 및 해결방안

@Qualifier 는 "mainDiscountPolicy" 처럼 문자를 적어준다. 따라서, 컴파일시 타입 체크가 안된다. 이를 해결하기 위해 애노테이션을 직접 만들어서 사용할 수도 있다.

```java
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER,
ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
}
```

 @MainDiscountPolicy 이름의 애노테이션을 직접 만들어서 사용하면 된다.

```java
@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy {}

@Autowired
public OrderServiceImpl(MemberRepository memberRepository,
@MainDiscountPolicy DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
}
```

<br>

### 4-4) @Primary

@Primary 애노테이션은 우선순위를 정하는 방법이다.

```java
@Component
@Primary
public class RateDiscountPolicy implements DiscountPolicy {}

@Component
public class FixDiscountPolicy implements DiscountPolicy {}
```

위와 같이 설정하면 RateDiscountPolicy 가 자동 주입 시 우선 순위를 가지게 된다.

<br>

### 4-5) @Primary vs @Qualifier

@Primary 는 자동으로 작동하고, @Qualifier 는 매칭되는 @Qualfiier 를 찾아서 수동으로 작동한다. 따라서, @Qualifier 가 우선 순위가 더 높다. 

<br>

### 4-6) @Primary, @Qualifier 활용

코드에서 자주 사용하는 메인 스프링 빈은 @Primary 를 적용해서 @Qualifier 지정 없이 편리하게 조회하고, 서브 스프링 빈을 획득할 때는 @Qualifier 를 지정해서 명시적으로 획득하는 방식으로 활용하면 코드를 깔끔하게 유지할 수 있다.

<br>

### 5) 빈 모두 주입받기

조회한 빈이 모두 필요하면 List, Map 자료구조를 이용하면 됩니다.

예를 들어, DiscountPolicy 가 RateDiscountPolicy, FixDiscountPolicy 2개로 나뉘어져 있는 상황에서 둘 다 이용하려면 다음과 같이 구현하면 된다.

```java
class DiscountService {
    private final Map<String, DiscountPolicy> policyMap;
    private final List<DiscountPolicy> policies;
    
    @Autowired
    public DiscountService(Map<String, DiscountPolicy> policyMap,
    List<DiscountPolicy> policies) {
        this.policyMap = policyMap;
        this.policies = policies;
    }
}
```

DiscountService 에서 위와 같이 Map, List 를 선언하면 스프링 빈으로 등록된 모든 DiscountPolicy 가 자동 주입 된다. 따라서, 상황에 맞춰서 꺼내서 사용할 수 있다.