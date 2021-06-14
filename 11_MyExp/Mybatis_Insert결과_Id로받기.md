Mybatis를 통해서 Insert를 하면 결과가 insert 성공 여부에 따라 0 / 1 로 반환된다.

나는 Insert를 하고 Id값을 반환을 원한다...!!!

<br>

#### 기존코드

```xml
<insert id="insert" parameterType="uploadfile">
        INSERT INTO uploadfile VALUES (uploadfile_seq.nextval, #{originFileName}, #{changedFileName})
</insert>
```

```java
Long res = uploadFileRepository.insert(uploadFile);
```

uploadFileService 클래스에서 uploadFileRepository insert 메소드 결과를 바로 직접적으로 받고 있었음

--> 결과는 영향을 준 행 0 / 1 을 반환!!

--> Select Key를 이용하자

<br>

#### 변경코드

```xml
<insert id="insert" parameterType="uploadfile">
    <selectKey keyProperty="uploadFileId" order="BEFORE" resultType="Long">
    	SELECT uploadfile_seq.nextval FROM DUAL
    </selectKey>
    INSERT INTO uploadfile VALUES (uploadfile_seq.nextval, #{originFileName}, #{changedFileName},)
</insert>
```

```java
uploadFileRepository.insert(uploadFile);
Long res = uploadFile.getUploadFileId();
```

uploadFileRepository insert 메소드 결과가 아니라 insert 메소드 결과로 반환된 uploadFile 의 id 값을 참조하면된다.