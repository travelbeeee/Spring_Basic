# MyBatis

`MyBatis` 는 자바에서 지원하는 JDBC API 를 이용했을 때 생기는 단점들을 개선해 자바 객체와 SQL 문 사이의 자동 매핑 기능을 지원하는 개발 ORM 프레임워크입니다. 

- 객체 지향적인 코드로 인해 더 직관적이고 비지니스 로직에 집중할 수 있게 도와준다.
- 재사용 및 유지보수의 편리성이 증가한다.
- DBMS에 대한 종속성이 줄어든다.

<br>

`MyBatis`도 내부적으로는 JDBC API 를 사용한다. 따라서, JDBC 커넥션에 관한 메타정보를 Properties에 저장해야 Spring 에서 사용할 수 있다.

```properties
spring.datasource.url=jdbc:oracle:thin:@//localhost:1521
spring.datasource.username=travelbeeee
spring.datasource.password=1234
```

<br>

#### **1) 객체 생성**

```java
package study.signUp.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Alias("member")
@Setter @Getter @ToString
public class Member {
    private Long id;
    private String username;
    private String userpwd;
    private String salt;
    private String email;
    private String auth;
}
```

Member Domain 객체를 만들었다.

<br>

#### 2) 객체 매퍼 생성

```java
@Mapper
public interface MemberRepository {
    int insert(Member member);
    int update(Member member);
    int updateAuth(Long id);
    int delete(Long id);
    Member selectByName(String username);
    Member selectById(Long id);
    Member selectByNamePwd(String username, String userpwd);
}
```

`@Mapper `애노테이션이 붙은 인터페이스는 `MapperScan`에 의해 읽혀져, 프록시가 생성된다.

<br>

#### 3) MapperScan

```java
@Configuration
@MapperScan(basePackages = "study.signUp.repository")
public class MyBatisConfiguration {
}

```

`@MapperScan` 애노테이션을 이용해서 `@Mapper` 애노테이션이 붙은 인터페이스들의 경로를 설정해줘야한다. 

<br>

#### 4) 매핑파일

```xml
<?xml version="1.0" encoding="UTF-8"?>

<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="study.signUp.repository.MemberRepository">

    <insert id="insert" parameterType="member">
        INSERT INTO member (id, username,userpwd, salt, email, auth)
        VALUES (member_seq.nextval, #{username}, #{userpwd}, #{salt}, #{email}, 'UNAUTHORIZATION')
    </insert>
    <update id="update" parameterType="member">
        UPDATE member SET username = #{username}, userpwd = #{userpwd}, email = #{email} WHERE id = #{id}
    </update>
    <delete id="delete" parameterType="Long">
        DELETE member WHERE id = #{id}
    </delete>
    <select id="selectByName" parameterType="String" resultType="member">
        SELECT * FROM member WHERE username = #{username}
    </select>
    <select id="selectById" parameterType="Long" resultType="member">
        SELECT * FROM member WHERE id = #{id}
    </select>
    <select id="selectByNamePwd" parameterType="String" resultType="member">
        SELECT * FROM member WHERE username = #{param1} and userpwd = #{param2}
    </select>
    <update id="updateAuth" parameterType="Long">
        UPDATE member SET auth = 'AUTHORIZATION' where id = #{id}
    </update>
</mapper>
```

이제 SQL문을 작성하고 연결시켜주는 xml 파일을 만들어야한다. MyBatis 는 XML 파일로 작성할 수 있어서 유지보수하기 쉽다.

- 

