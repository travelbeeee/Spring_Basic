# BeanScope

### 1) 빈 스코프

빈 스코프란 스프링 빈이 생성되서 종료되는 범위를 뜻한다.

- 싱글톤

  기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프

- 프로토타입 

  빈이 요청될 때마다 스프링 컨테이너가 새롭게 생성해주는 스코프, **스프링 컨테이너가 생성과 의존관계 주입까지만 관여하고 그 뒤로는 관리하지 않는** 매우 짧은 범위의 스코프

  - 스프링 컨테이너에 요청할 때 마다 새로 생성된다.
  - **스프링 컨테이너가 빈의 생성과 의존관계 주입, 초기화까지만 관여하므로 종료 메서드가 호출되지 않는다.**

  ```
  @Scope("prototype")
  ```

- 웹 스코프

  웹 환경에서만 동작하는 스코프입니다. 프로토타입과 다르게 스프링이 해당 스코프의 종료시점까지 관리해주고, 종료 메서드가 호출됩니다.
  
  - request : HTTP 요청 하나가 들어오고 나갈 댸 까지 유지되는 스코프, 각각의 HTTP 요청마다 별도의 빈 인스턴스가 생성되고 관리된다.
  - session : HTTP Session과 동일한 생명주기를 가지는 스코프
  - application : 서블릿 컨텍스트(`ServletContet`)와 동일한 생명주기를 가지는 스코프
  - websocket : 웹 소켓과 동일한 생명주기를 가지는 스코프

스프링은 기본적으로 모든 빈을 싱글톤으로 관리합니다. 따라서, 다른 스코프를 적용하고 싶으면 @Scope 애노테이션을 이용해야합니다.

<br>

### 2) 싱글톤 && 프로토타입 문제

 프로토타입은 빈이 요청될 때마다 스프링 컨테이너가 새롭게 오브젝트를 생성해준다.

 프로토타입 빈 내에서 싱글톤 타입 빈을 의존하고 있으면 어떻게 될까??

 프로토타입 빈이 새롭게 생성될 때마다 똑같은 싱글톤 타입 빈을 주입받으므로 문제가 없다.

 그러면, 싱글톤 내에서 프로토타입 빈을 DI 받아 사용하면 어떻게 될까??

 **싱글톤 오브젝트는 처음 생성될 때 프로토타입 빈을 DI 받게되고, 싱글톤 오브젝트는 다시 생성되지 않으므로 프로토타입 빈이 싱글톤으로 사용되는 문제가 발생한다. 즉, 우리가 의도한대로 새로운 프로토타입 빈이 주입되는 것이 아니라 동일한 프로토 타입 빈이 처음에 주입되고 변하지 않는다.**

--> 의존 관계를 외부에서 주입(DI) 받는게 아니라 직접 필요한 의존 관계를 찾아야한다(DL, Dependency Lookup).

<br>

#### 2-1) ObjectProvider 인터페이스

 지정한 빈을 컨테이너에서 대신 찾아주는 DL 서비스를 스프링에서는 `ObjectProvider` , `ObjectFactory` 인터페이스를 통해 제공해준다. 과거에는 `ObjectFactory` 인터페이스 하나만 있었는데 기능을 추가해서 `ObjectProvider` 인터페이스가 만들어졌다.

- ObjectFactory : 기능이 단순, 별도의 라이브러리 필요 없음
- ObjectProvider : ObjectFactory를 상속해 옵션, 스트림 처리 등 편의 기능이 더 늘어났다. 별도의 라이브러리 필요 없음.

 두 인터페이스 모두 스프링에서 지원해주므로 스프링에 의존하게 된다는 단점이 있다.

```java
class SingletonBean{
	@Autowired
    private ObjectProvider<PrototypeBean> prototypeBeanProvider;
    
    public void logic() {
        PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
        
        return;
    }
}
```

ObjectProvider의 getObject() 메소드를 통해 필요할 때마다 PrototypeBean 오브젝트를 생성하면 된다. 

getObject() 메소드를 호출하면 내부적으로 스프링 컨테이너를 통해 빈을 찾아서 반환해준다. (DL)

스프링에서 제공하므로 별도의 라이브러리가 필요 없지만 스프링에 의존하게 된다.

> ObjectProvider 는 PrototypeBean을 사용하기 위해 존재하는 것이 아니라, 스프링컨테이너에서 빈을 찾아주는 과정을 간단하게 도와주기 위해 설계됐다.
>
> --> 스프링이 제공하는 기능을 사용하지만, 기능이 단순하므로 단위테스트를 만들거나 mock 코드를 만들기 훨씬 쉽게 도와준다.

<br>

#### 2-2) JSR-330 Provier  인터페이스

`javax.inject.Provier` 라는 JSR-330 자바 표준을 사용해서도 위의 문제를 해결할 수 있다.

```java
class SingletonBean{
	@Autowired
    private Provider<PrototypeBean> prototypeBeanProvider;
    
    public void logic() {
        PrototypeBean prototypeBean = prototypeBeanProvider.get();
        
        return;
    }
}
```

- 자바 표준에서 제공하는 인터페이스이고 딱 DL 정도의 기능만 제공해준다. ( get() 메서드 하나로 정말 단순! )
- `javax.inject:javax.inejct:1` 라이브러리를 추가해야 사용할 수 있다.

<br>

#### 2-3) Scoped Proxy

Proxy 를 이용해 가짜 프록시 클래스를 만들어서 문제를 해결할 수 있다.

```java
public class Singleton {
    @Autowired
    private PrototypeBean prototypeBean;
}

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PrototypeBean {

}
```

 실제 인스턴스를 감싸는 프록시 인스턴스를 만들고, 프록시 인스턴스를 주입해주도록 하는 기법이다. 프록시 기법을 통해 진짜 원본 객체 조회를 꼭 필요한 시점까지 지연한다. 

 적용 대상이 클래스면 `TARGET_CLASS` 인터페이스면 `INTERFACE` 설정을 적용하면 됩니다.

<br>

