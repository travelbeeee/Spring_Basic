## Lombok

Lombok 은 컴파일 시 흔하게 작성하는 기능(코드)를 완성해주는 라이브러리입니다.

Spring에서 Model(DTO, VO, DAO) Object를 만들 때 유용하게 사용되며 반복되는 코드를 어노테이션을 통해 줄여줍니다.

#### -Maven

```xml
        <!-- Lombok 사용을 위한 dependency 추가 -->
        <dependency>
        	<groupId>org.projectlombok</groupId>
        	<artifactId>lombok</artifactId>
        	<version>1.18.0</version>
        	<scope>provided</scope>
        </dependency>
```

롬복 라이브러리를 사용하기 위해서는 pom.xml에 dependency를 추가해야됩니다. (maven 기준)

<br>

#### -**Gradle**

```gradle
provided 'org.projectlombok:lombok:1.18.0'
```

build.gradle에 Dependency를 추가해야 됩니다.



#### @Data

아래의 모든 어노테이션을 한 번에 처리한 어노테이션.

```java
package com.lombok.test;

import lombok.Data;

@Data
public class Member {
	private int age;
	private String name;
}

```

@Data 어노테이션을 Member 객체에 추가하면 다음과 같이 메소드들이 자동으로 생긴다.

<img src="https://user-images.githubusercontent.com/59816811/103989272-cda42200-51d2-11eb-8759-c8d298b88d16.png" alt="20210108_165829"/>

<br>

#### @ToString

@ToString 어노테이션은 이름 그대로 ToString 메소드를 작성해주는 어노테이션이다.

exclude 속성을 사용해서 원치않는 인스턴스 변수에 대해서는 제외를 할 수도 있다.

```java
package com.lombok.test;

import lombok.ToString;

@ToString
public class Member {
	private int age;
	private String name;
}

@ToString(exclude="name")
public class Member {
	private int age;
	private String name;
}
```

toString 메소드는 Object 클래스에 있는 메소드로 객체가 가지고 있는 정보나 값들을 문자열로 만들어 리턴하는 메소드입니다. 

<img src="https://user-images.githubusercontent.com/59816811/103990604-dac21080-51d4-11eb-9c43-184443f07b0b.png" alt="20210108_171007" />

 <br>

#### @EqualsAndHashCode

@EqualsAndHashCode 어노테이션은 equals 메소드와 hashCode 메소드를 만들어줍니다.

- equals 메소드 : 두 객체의 내용이 같은지 비교하는 연산자
- hashCode 메소드 : 두 객체가 같은 객체인지 비교하는 연산자

로 자주 쓰이는 메소드입니다.

callSuper 속성을 통해 equals와 hashCode 메소드 생성 시 부모 클래스의 필드까지 감안할지 여부를 결정 할 수 있습니다. default 는 false 값으로 자신 클래스의 필드 값만 고려합니다.

```java
package com.lombok.test;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Member {
	private int age;
	private String name;
}

@EqualsAndHashCode(callSuper = true)
public class Member {
	private int age;
	private String name;
}
```

<img src="https://user-images.githubusercontent.com/59816811/103990606-dbf33d80-51d4-11eb-9936-e78408e67967.png" alt="20210108_171320" />

<br>

#### @Getter / @Setter

@Getter 와 @Setter 어노테이션은 인스턴스 변수에 대해서 Getter / Setter 메소드를 자동으로 만들어준다.

```java
package com.lombok.test;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
	private int age;
	private String name;
}

```

<img src="https://user-images.githubusercontent.com/59816811/103990612-dd246a80-51d4-11eb-9c21-5cdfa57b3700.png" alt="20210108_170404"  />

<br>

#### @RequiredArgsConstructor / @NonNull

@NonNull 어노테이션이 설정된 인스턴스 변수 또는 final 지시자로 선언된 인스턴수 변수를 파라미터로 받는 생성자를 작성해준다.

```java
package com.lombok.test;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Member {
	@NonNull
	private int age;
	private String name;
}

```

<img src="https://user-images.githubusercontent.com/59816811/103990609-dc8bd400-51d4-11eb-8a1e-9419c4bbf56b.png" alt="20210108_170314"  />

<br>

#### @AllArgsConstructor

인스턴스 변수로 선언된 모든 것을 파라미터로 받는 생성자를 만들어준다.

```java
package com.lombok.test;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Member {
	private int age;
	private String name;
}

```

<img src="https://user-images.githubusercontent.com/59816811/103990608-dbf33d80-51d4-11eb-821e-c1b25b8fd0c6.png" alt="20210108_170212"/>
