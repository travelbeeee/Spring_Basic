# ApplicationEventPublisher

`ApplicationContext`는 이벤트 프로그래밍에 필요한 `ApplicationEventPublisher` 인터페이스도 상속받아 기능을 제공하고 있습니다.

<br>

### 1) 이벤트 만들기

스프링 4.2 이전에는 `ApplicationEvent`를 상속한 `Event`클래스를 직접 만들고 이벤트 발생, 이벤트 처리를 모두 커스튬해서 사용했어야한다.

```java
public class MyEvent extends ApplicationEvent {

    private int data;

    public MyEvent(Object source) {
        super(source);
    }

    public MyEvent(Object source, int data) {
        super(source);
        this.data = data;
    }

    public int getData() {
        return data;
    }
}
```

<br>

스프링 4.2 이후부터는 아래처럼 스프링 코드없이 POJO로 이벤트를 만들 수 있습니다.

```java
public class MyEvent {

    private int data;

    public MyEvent(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }
}
```

<br>

### 2) 이벤트 처리하기

이벤트에 해당하는 `MyEvent` 클래스를 만들고 이를 다루는 `EventHandler` 역할의 `MyEventHandler` 클래스를 빈으로 등록해주면 된다.

```java
@Component
public class MyEventHandler implements ApplicationListener<MyEvent> {

    @Override
    public void onApplicationEvent(MyEvent event) {
        // 여기에서 내가 원하는 이벤트 처리 구현!
        System.out.println("이벤트 발생! 데이터는 " + event.getData());
    }
}
```

스프링 4.2이후부터는 아래 코드처럼 @EventListener 애노테이션을 이용해 스프링 코드 없이 이벤트 처리를 구현할 수 있습니다.

```java
@Component
public class MyEventHandler{

    @EventListener
    public void myEventHandleMethod(MyEvent event) {
        System.out.println("이벤트 발생! 데이터는 " + event.getData());
    }
}
```

<br>

### 3) 이벤트 발생시키기

이런 `Event` 클래스를 발생시키는 `EventPublisher` 기능이 `ApplicationContext` 가 추가로 가지고 있는 기능이다.

```java
@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    ApplicationEventPublisher publisher;
    // ApplicationContext context; 로 주입받아도된다.

    @Override
    public void run(ApplicationArguments args) throws Exception {
        publisher.publishEvent(new MyEvent(this, 100));
        // publisher.publishEvent(new MyEvent(100));
    }
    
}
```

<br>

스프링 4.2 이후부터는 더 이상 스프링 클래스들을 상속하지않고, POJO 를 지키면서 애노테이션 기반으로 이벤트를 처리할 수 있는 것을 볼 수 있습니다.
