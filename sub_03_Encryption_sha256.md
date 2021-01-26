# Encryption_SHA-256

고객의 중요정보는 무조건 암호화를 해서 보관해야한다. 그렇지 않으면 정보 유출 등 심각한 상황이 발생하게 된다. 

이를 위해 스프링에서는 Spring-Security를 제공해주고, 자바에서는 Java Security 를 지원해준다. 또, 각종 데이터베이스에서도 암호화를 위한 기능을 제공해준다.

먼저, 데이터베이스에서 제공해주는 암호화 기능을 이용하면 다음과 같은 문제가 생긴다.  처음에 서비스가 MySql 을 이용해 개발했으나 나중에 Oracle 등 다른 DBMS 로 갈아타게 된다면 현재 저장된 정보들은 MySql 에서 지원해주는 암호화 기능을 이용해 암호화된 정보들이므로 문제가 생기게된다.

Spring-Security 는 아직 학습하지않았으므로 오늘은 Java Security 라이브러리에서 지원해주는 sha256 해쉬 알고리즘을 이용해서 데이터를 암호화하는 방법을 정리해보겠다.

> 참고!
>
> 서비스를 관리하는 관리자도 패스워드 같은 회원정보의 원본 내용을 확인할 수 없게 암호화를 해서 저장해야된다. 또, 복호화를 하는 방법은 제공하면 안되고 오직 단방향으로 암호화만 진행해야된다.

<br>

### SHA-256

- SHA-2 알고리즘의 한 종류로 256비트로 구성되며 64자리 문자열을 반환해주는 **해쉬 알고리즘** 입니다.
- 해쉬 알고리즘이기 때문에 암호화는 가능하지만, 복호화는 불가능합니다.
- 어떤 길이의 값을 입력하더라도 256비트의 고정된 결과값을 출력합니다.
- 2^256 만큼 경우의 수를 만들고 입력 값이 조금이라도 다르면 완전히 다른 값을 출력합니다.
  - 반대로 말하면 입력 값이 같으면 같은 값을 출력합니다. 이는 통계적 공격에 이용될 여지가 있습니다.

<br>

java.security 라이브러리에서 지원해주는 MessageDigest 클래스를 이용하면 SHA-256 알고리즘을 이용할 수 있습니다.

```java
    /**
     * SHA-256으로 해싱하는 메소드
     *
     * @param bytes
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String sha256(String msg) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());

        return bytesToHex(md.digest());
    }

    /**
     * 바이트를 헥스값으로 변환한다.
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b: bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
```

SHA-256 알고리즘에는 위에서 언급했던 다음과 같은 문제가 있습니다.

```java
    public static void main(String[] args) {
        try{
            System.out.println(sha256("helloworld"));
            System.out.println(sha256("helloworld"));
            System.out.println(sha256("helloworld"));
            System.out.println(sha256("helloworld"));
        }catch(Exception e){
        }
    }
```

```
[ 출력결과 ]
936a185caaa266bb9cbe981e9e05cb78cd732b0b3280eb944412bb6f8f8f07af
936a185caaa266bb9cbe981e9e05cb78cd732b0b3280eb944412bb6f8f8f07af
936a185caaa266bb9cbe981e9e05cb78cd732b0b3280eb944412bb6f8f8f07af
936a185caaa266bb9cbe981e9e05cb78cd732b0b3280eb944412bb6f8f8f07af
```

이처럼 같은 값을 넣으면 같은 결과를 얻을 수 있습니다.

<br>

해커에 의해서 데이터베이스를 탈취당했고, 여러 명의 유저 패스워드 정보가 동일하게 abcd로 암호화되어있다고 가정해보겠습니다. 그럼 다음과 같은 문제가 발생합니다.

1) 어떤 값을 해시화해서 abcd가 나오는지 알아낸다면 동일한 패스워드를 사용하는 모두의 아이디를 해킹할 수 있습니다.

2) 패스워드가 겹치는 사람이 많으므로 자주 사용되는 패스워드이고 이는 취약점이 된다.

따라서, Salt 를 이용하여 같은 입력이라도 다르게 만들어서 SHA-256 해시함수를 이용해야한다.

> 솔트 (Salt)
>
> 기본 양념 소금처럼 원문에 가미하여 암호문을 다른 값으로 만드는 기법입니다.
>
> 해시기법은 동일한 입력에 대해서 동일한 결과를 가지므로 여러 취약점이 생긴다. 이를 방지하기 위해 문자열의 앞 뒤에 난수생성을 통해 작성된 특정 문자열을 끼어넣은 상태로 해시를 돌려버리면 같은 입력이라도 다른 출력을 가지게 된다.

<br>

```java
	/**
     * SHA-256으로 해싱하는 메소드
     *
     * @param bytes
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String sha256(String msg) throws NoSuchAlgorithmException {
        String salt = UUID.randomUUID().toString();
        msg += salt;

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());

        return bytesToHex(md.digest());
    }

    /**
     * 바이트를 헥스값으로 변환한다.
     *
     * @param bytes
     * @return
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b: bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
```

Salt 로는 자바에서 제공해주는 UUID 식별자를 이용했다.

```
[ 출력결과 ]
48d1b6275b0bee229e0447826f0dd38e9efc8b4f2fbd0dd53fdac028aada4f3c
4998f5636f02c6ed7050c0d471730857e57c6cacc76660ecd72955e20ea222ab
1225c40b1de02ebe3079b6eecc85b1420379edab6fe935be45a1c4c360dadee1
4716f0612008a262bb3fda12653350507f9aaf61a63e9be844f5ff4decfa5e62
```

그럼 우리가 원하는대로 같은 입력에 대해서 다른 해시결과를 가지게 된다.

<br>

#### Question 

SHA-256 는 해쉬함수니까 어떤 컴퓨터에서든 "helloworld"를 입력하면 다 같은 값이 나오는게 아닌가?? 그럼 내 서비스가 SHA-256 을 이용하고있다는 것을 알면 해킹에 취약해지는건가??

#### Answer

맞다. 내 로컬이랑 친구 로컬에서 java.security 라이브러리에서 지원해주는 "SHA-256" 을 이용하면 당연히 해시함수니까 똑같은 입력에 대해서 똑같은 출력값을 가진다.

<br>

#### Question

Salt 를 이용해가면서까지 해시함수를 이용해서 암호화를 진행하는 이유??

#### Answer

대칭기 혹은 비대칭키 암호화 알고리즘을 사용하게되면 일단 내가 키를 안전하게 보관해야된다는 책임이 생긴다. 또, 해시함수에 비해서 속도가 느리다는 단점이 있다.

 

