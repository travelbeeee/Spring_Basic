# BeanScope

### 1) 빈 스코프

빈 스코프란 스프링 빈이 생성되서 종료되는 범위를 뜻한다.

- 싱글톤

  기본 스코프, 스프링 컨테이너의 시작과 종료까지 유지되는 가장 넓은 범위의 스코프

- 프로토타입 

  빈이 요청될 때마다 스프링 컨테이너가 새롭게 생성해주는 스코프, 스프링 컨테이너가 생성과 의존관계 주입까지만 관여하고 그 뒤로는 관리하지 않는 매우 짧은 범위의 스코프

  - 스프링 컨테이너에 요청할 때 마다 새로 생성된다.
  - 스프링 컨테이너가 빈의 생성과 의존관계 주입, 초기화까지만 관여하므로 종료 메서드가 호출되지 않는다.

- 웹 스코프

  - request : 웹 요청이 들어오고 나갈때 까지 유지되는 스코프
  - session : 웹 세션이 생성되고 종료될 때 까지 유지되는 스코프
  - application : 웹의 서블릿 컨텍스와 같은 범위로 유지되는 스코프
  - websocket : 웹 소켓과 동일한 생명주기를 가지는 스코프

  웹 스코프는 웹 환경에서만 동작하고 스프링이 해당 스코프의 종료시점까지 관리해준다. ( 종료 메서드 호출 )

<br>

### 2) 싱글톤 && 프로톤타입 문제

프로토타입은 빈이 요청될 때마다 스프링 컨테이너가 새롭게 오브젝트를 생성해준다.

그러면, 싱글톤 내에서 프로토타입 빈을 DI 받아 사용하면 어떻게 될까??

**싱글톤 오브젝트는 처음 생성될 때 프로토타입 빈을 DI 받게되고, 싱글톤 오브젝트는 다시 생성되지 않으므로 프로토타입 빈이 싱글톤으로 사용되는 문제가 발생한다.**

--> 의존 관계를 외부에서 주입(DI) 받는게 아니라 직접 필요한 의존 관계를 찾아야한다(DL).

#### 2-1) ObjectProvider 인터페이스

지정한 빈을 컨테이너에서 대신 찾아주는 DL 서비스를 스프링에서는 `ObjectProvider` , `ObjectFactory` 인터페이스를 통해 제공해준다. 과거에는 `ObjectFactory` 인터페이스 하나만 있었는데 기능을 추가해서 `ObjectProvider` 인터페이스가 만들어졌다.

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

- 스프링에서 제공하므로 별도의 라이브러리가 필요 없지만 스프링에 의존하게 된다.

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

- 자바 표준에서 제공하는 인터페이스이고 딱 DL 정도의 기능만 제공해준다.
- `javax.inject:javax.inejct:1` 라이브러리를 추가해야 사용할 수 있다.