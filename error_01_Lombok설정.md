### [상황]

![lombok_01](https://user-images.githubusercontent.com/59816811/106232695-f6e72980-6237-11eb-892a-344c99cfefd0.png)

Member 클래스에서 lombok 애노테이션 사용을 위해, Lombok Dependency를 Build.gradle 에 다음과 같이 추가.

```grad
implementation 'org.projectlombok:lombok'
```

Test 클래스를 만들고 Member 클래스를 사용하던 중 다음과 같이 setId() 메소드가 없다고 에러 발생.

<img src="https://user-images.githubusercontent.com/59816811/106233033-eb483280-6238-11eb-88dc-60de893905ed.png" alt="lombok_05" width="700"/>

Dependencies를 보면 lombok library가 잘 추가되어있음...!

<br>

### [해결방법]

#### 1) Plugin 설정

Project 의 셋팅에서 Lombok plugin을 추가해줍니다.

<img src="https://user-images.githubusercontent.com/59816811/106232940-a4f2d380-6238-11eb-9ded-5afd2ba83916.png" alt="lombok_02" width="600"/>

<br>

#### 2) Compiler 설정

Project 의 셋팅에서 Compiler로 가서 Build Project automatically를 설정

![lombok_03](https://user-images.githubusercontent.com/59816811/106232943-a6bc9700-6238-11eb-8593-f0ab1fc26b83.png)

<br>

#### 3)  Annotation Processors 설정

Settings -> Compiler --> Annotation Processors 의 Enable annotation processing 설정 체크

![lombok_04](https://user-images.githubusercontent.com/59816811/106232945-a7552d80-6238-11eb-982f-1cadd64114bd.png)