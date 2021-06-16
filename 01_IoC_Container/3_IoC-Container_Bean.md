#  Bean

스프링에서 제공해주는 IoC Container에 빈을 등록하는 방법에 대해서 알아보았습니다. 그러면, 등록된 빈을 어떻게 조회하고 사용할 수 있을까요??

### 1) 빈 조회하기 - 단일조회

`ApplicationContext.getBean()` 메서드를 이용해서 등록된 빈을 가져올 수 있습니다.

파라미터로는 `빈의 이름`, `빈의 타입`을 줄 수 있습니다. 이름 없이 타입으로만 조회도 가능합니다.

```java
public class ApplicationContextInfoTest {

    @Configuration
    static class MyAppConfig {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }
    
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyAppConfig.class);

    @Test
    @DisplayName("빈 이름으로 조회하기")
    public void 빈_이름으로_조회() throws Exception{
        Bean1 bean1 = ctx.getBean("bean1", Bean1.class);
        System.out.println("bean1.getClass() = " + bean1.getClass());

        Bean2 bean2 = ctx.getBean("bean2", Bean2.class);
        System.out.println("bean2.getClass() = " + bean2.getClass());
    }
    
    @Test
    @DisplayName("빈 타입으로 조회하기")
    public void 빈_타입으로_조회() throws Exception{
        Bean1 bean1 = ctx.getBean(Bean1.class);
        System.out.println("bean1.getClass() = " + bean1.getClass());

        Bean2 bean2 = ctx.getBean(Bean2.class);
        System.out.println("bean2.getClass() = " + bean2.getClass());
    }
}

// 빈 이름으로 조회하기 출력결과
bean1.getClass() = class travelbeeee.spring_core_concept.bean.Bean1
bean2.getClass() = class travelbeeee.spring_core_concept.bean.Bean2
    
    
// 빈 타입으로 조회하기 출력결과
bean1.getClass() = class travelbeeee.spring_core_concept.bean.Bean1
bean2.getClass() = class travelbeeee.spring_core_concept.bean.Bean2
```

당연히, 없는 이름으로 조회하면 `NoSuchBeanDefinitionException` 에러가 발생합니다.

<br>

### 2) 빈 조회하기 - 동일한 타입이 여러 개

bean1과 bean2 모두 ParentBean 인터페이스의 구현체라고 해보자. 그러면, ParetnBean 인터페이스 타입으로 빈을 조회하면 해당되는 빈이 2개이므로 중복 오류가 발생한다.

```java
public class ApplicationContextInfoTest {

    @Configuration
    static class MyAppConfig {
        @Bean
        public ParentBean bean1() {
            return new Bean1();
        }

        @Bean
        public ParentBean bean2() {
            return new Bean2();
        }
    }

    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyAppConfig.class);

    @Test
    @DisplayName("동일 타입 빈 조회")
    public void 동일한_타입_빈_조회() throws Exception{
        assertThrows(NoUniqueBeanDefinitionException.class,
                    () -> ctx.getBean(ParentBean.class));
    }
}

// 결과
org.springframework.beans.factory.NoUniqueBeanDefinitionException
```

<br>

 특정 타입의 빈이 여러 개 등록되어있을 때는 빈의 이름도 추가적으로 제공해줘야 스프링이 우리가 원하는 빈을 찾아줄 수 있습니다.

```java
@Test
@DisplayName("동일 타입 빈 조회")
public void 동일한_타입_빈_조회() throws Exception{
    ParentBean bean1 = ctx.getBean("bean1", ParentBean.class);
    ParentBean bean2 = ctx.getBean("bean2", ParentBean.class);
    Assertions.assertThat(bean1.getClass()).isEqualTo(Bean1.class);
    Assertions.assertThat(bean2.getClass()).isEqualTo(Bean2.class);
    System.out.println("bean1.getClass() = " + bean1.getClass());
    System.out.println("bean2.getClass() = " + bean2.getClass());
}

// 출력결과
bean1.getClass() = class travelbeeee.spring_core_concept.bean.Bean1
bean2.getClass() = class travelbeeee.spring_core_concept.bean.Bean2
```

<br>

 특정 타입의 빈을 모두 조회하고 싶을 때는 `ApplicationContext.getBeansOfType()` 메소드를 이용하면 된다.

```java
@Test
@DisplayName("특정타입 빈 모두 조회")
public void 특정타입_빈_모두조회() throws Exception {
    Map<String, ParentBean> beansOfType = ctx.getBeansOfType(ParentBean.class);
    for (String key : beansOfType.keySet()) {
        System.out.println("key = " + key + " value = " + beansOfType.get(key));
    }
}

// 출력결과
key = bean1 value = travelbeeee.spring_core_concept.bean.Bean1@63dd899
key = bean2 value = travelbeeee.spring_core_concept.bean.Bean2@59d2400d
```

<br>

### 3) 빈 조회하기 - 전체조회

등록한 빈들을 모두 조회해보자.

`ApplicationContext.getBeanDefinitionNames()` 메소드를 이용해서 등록된 모든 빈들의 이름을 받아올 수 있고, 이름을 이용해서 빈을 꺼내오면 된다.

```java
package travelbeeee.spring_core_concept.beanfind;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import travelbeeee.spring_core_concept.AppConfig;

public class ApplicationContextInfoTest {

    @Configuration
    static class MyAppConfig {
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }
    }
    
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyAppConfig.class);
    
    @Test
    @DisplayName("모든 빈 출력하기")
    public void 모든_빈_출력하기() throws Exception{
        String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            Object bean = ctx.getBean(beanName);
            System.out.println("bean.getClass() = " + bean.getClass());
        }
    }

    @Test
    @DisplayName("애플리케이션 빈 출력하기")
    public void 애플리케이션_빈_출력하기() throws Exception{
        String[] beanDefinitionNames = ctx.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ctx.getBeanDefinition(beanName);
            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ctx.getBean(beanName);
                System.out.println("bean.getClass() = " + bean.getClass());
            }
        }
    }
}

// 모든 빈 출력하기
bean.getClass() = class org.springframework.context.annotation.ConfigurationClassPostProcessor
bean.getClass() = class org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor
bean.getClass() = class org.springframework.context.annotation.CommonAnnotationBeanPostProcessor
bean.getClass() = class org.springframework.context.event.EventListenerMethodProcessor
bean.getClass() = class org.springframework.context.event.DefaultEventListenerFactory
bean.getClass() = class travelbeeee.spring_core_concept.order.MyAppConfig$$EnhancerBySpringCGLIB$$63f31da6
bean.getClass() = class travelbeeee.spring_core_concept.bean.Bean1
bean.getClass() = class travelbeeee.spring_core_concept.bean.Bean2
    
// 애플리케이션 빈 출력하기
bean.getClass() = class travelbeeee.spring_core_concept.order.MyAppConfig$$EnhancerBySpringCGLIB$$63f31da6
bean.getClass() = class travelbeeee.spring_core_concept.bean.Bean1
bean.getClass() = class travelbeeee.spring_core_concept.bean.Bean2
```

스프링에서 필요에 의해 자동으로 추가한 빈들과 내가 등록한 빈들이 모두 조회되는 것을 볼 수 있다. 애플리케이션 빈 출력을 위해서 `AnnotationContext` 인터페이스가 아닌 구현체를 받아왔다.

<br>

### 4) 빈 조회하기 - 상속관계

 스프링에서는 기본적으로 부모 타입으로 조회하면, 자식 타입도 함께 조회됩니다. 따라서, `Object` 타입으로 빈을 조회하면 모든 등록된 빈을 조회할 수 있습니다.

 부모 타입으로 빈을 단일 조회를 하게 되면 당연히 `NoUniqueBeanDefinitionException` 에러가 발생하고, 동일한 타입이 여러 개인 경우와 동일하게 해결할 수 있습니다.

<br>

