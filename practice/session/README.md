## 세션

#### 기본 개념

* HTTP 프로토콜을 기반으로 하는 웹 서비스는 클라이언트와 서버의 관계를 유지하지 않는다. 

  세션은 클라이언트와 서버의 연결 관계를 유지해주는 기법으로 서버에서 연결 정보를 저장하고 관리해서 유지해준다.

* 세션이란 일정 시간동안 같은 사용자(브라우저)로 부터 들어오는 요구를 하나의 상태로 보고 그 상태를 일정하게 유지시키는 기술

  --> 방문자가 웹 브라우저를 통해 웹 서버에 접속한 시점으로부터 웹 브라우저를 종료함으로써 연결을 끝내는 시점까지 상태를 유지해주는 기술.

<hr>

#### HttpSession 정리

HttpServletRequest 객체의 getSession 메소드를 이용해서 HttpSession 객체를 할당받을 수 있다.



HttpSession 객체는 다음과 같은 메소드가 있다.

- getId() : 세션 ID를 반환한다.
- setAttribute(String key, Object value) : 세션 객체에 { key : value } 정보를 저장한다.
- getAttribute(String key) : 세션 객체에 저장된 정보 중 key에 해당하는 value를 반환한다.
- removeAttribute(String key) : 세션 객체에 저장된 정보 중 key에 해당하는 정보를 제거한다.
- setMaxInactiveInterval(int interval) : 세션을 유지하기 위한 세션 유지시간을 초 단위로 설정한다.
- getMaxInactiveInterval() : 현재 생성된 세션을 유지하기 위해 설정된 세션 유지 시간을 int 형으로 반환하다.
- invalidate() : 현재 생성된 세션을 무효화시킨다.

<hr>

#### 구조

![20210106_111144](https://user-images.githubusercontent.com/59816811/103721011-02b44700-5010-11eb-9ed3-75cba7766454.png)

