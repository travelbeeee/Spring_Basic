# 빈 생명 주기

### 1) 빈 생명주기 (BeanLifeCycle)

스프링 빈은 다음과 같은 라이프사이클을 가진다.

스프링 컨테이너 생성 ==> 스프링 빈 생성 ==> 의존관계 주입 ==> 초기화 콜백 ==> 빈 사용 ==> 소멸 전 콜백 ==> 스프링 종료

> - 초기화 콜백 : 빈이 생성되고, 빈의 의존관계 주입이 완료된 후 호출
> - 소멸전 콜백 : 빈이 소멸되기 직전에 호출

스프링 빈은 의존관계 주입까지 다 끝난 뒤에 우리가 사용할 수 있다. 따라서 초기화 작업은 의존관계 주입이 모두 완료되고 난 다음에 호출해야 한다.

그러면 어떻게 의존관계 주입이 모두 완료되고 내가 사용할 수 있는 상태인지 알 수 있을까??

**스프링은 의존관계 주입이 완료되면 스프링 빈에게 콜백 메서드를 통해서 초기화 시점을 알려준다. 또한, 스프링은 스프링 컨테이너가 종료되기 직전에 소멸 콜백을 준다.**

> 콜백이란!
>
> 프로그래밍에서 **콜백**(callback) 또는 **콜애프터 함수**(call-after function) 는 다른 코드의 인수로서 넘겨주는 실행 가능한 코드를 말한다. 콜백을 넘겨받는 코드는 이 콜백을 필요에 따라 즉시 실행할 수도 있고, 아니면 나중에 실행할 수도 있다.

그러면 어떻게 콜백을 사용할 수 있을까??

<br>

### 2) 콜백 사용하기

#### 2-1) 인터페이스( InitializingBean, DisposableBean )

이 방법은 스프링에서 지원해주는 InitializingBean, DisposableBean  인터페이스를 구현해서 사용하는 방법이다.

따라서, 다음과 같은 단점이 있다.

- 스프링 전용 인터페이스로 코드가 스프링 전용 인터페이스에 의존하게 된다.
- 초기화, 소멸 메서드의 이름을 변경할 수 없다.
  - 따라서 외부 라이브러리에 적용할 수 없다.

```java
public class NetworkClient implements InitializingBean, DisposableBean {
    
    @Override
    public void afterPropertiesSet() throws Exception 
        // 초기화 콜백
    }
    
    @Override
    public void destroy() throws Exception {
        // 소멸전 콜백
    }
}
```

> 참고 ! 이 방법은 스프링 개발 초창기에 나온 방법이고 현재는 거의 사용하지 않는다.

<br>

#### 2-2) 빈 등록 초기화, 소멸 메서드 지정

설정 정보에 `@Bean(initMethod = "init", destoryMethod = "close")` 처럼 초기화, 소멸 메서드를 직접 만들어서 지정할 수 있다.

```java
@Bean(initMethod = "init", destroyMethod = "close")
public NetworkClient networkClient(){
    return new NetworkClient();
}


public class NetworkClient{
    public void init() throws Exception 
        // 초기화 콜백
    }
    
    public void close() throws Exception {
        // 소멸전 콜백
    }
}
```

초기화 콜백으로 사용하고 싶은 메소드, 소멸전 콜백으로 사용하고 싶은 메소드를 구현하고 설정 정보에 메소드 명을 지정해주면 됩니다.

- 메소드 이름을 자유롭게 지정 가능
- 스프링 빈이 스프링 코드에 의존하지 않는다.
- 코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있다.

> 참고 ! 
>
> destryMethod 속성은 대부분의 라이브러리가 close, shutdown 이라는 이름의 종료 메서드를 사용하므로 기본값으로 close, shutdown 이름의 종료 메서드가 있으면 자동으로 사용해준다.
>
> 따라서, 스프링 빈으로 등록하면 종료 메서드는 따로 적어주지 않아도 잘 동작합니다.

<br>

#### 2-3) @PostConstruct, @PreDestroy

@PostConstruct, @PreDestroy 애노테이션을 사용하면 편리하게 초기화와 종료를 실행할 수 있습니다.

```java
@Bean
public NetworkClient networkClient(){
    return new NetworkClient();
}


public class NetworkClient {
    
    @PostConstruct
    public void init() throws Exception 
        // 초기화 콜백
    }
    
    @PreDestroy
    public void close() throws Exception {
        // 소멸전 콜백
    }
}
```

초기화와 종료 메서드로 설정하고 싶은 메소드에 애노테이션만 붙여주면 됩니다.

- 최신 스프링에서 가장 권장하는 방법!
- 스프링이 아닌 자바 표준에서 지원하는 애노테이션
- 단점으로는 외부 라이브러리에는 사용하지 못한다.