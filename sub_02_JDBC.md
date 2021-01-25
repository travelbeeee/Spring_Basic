## JDBC ( Java Databse Connectivity )

자바에서 데이터베이스에 접속할 수 있도록 하는 자바 API 다.

아래 그림과 같이 드라이버 로딩, DB 연결, SQL 쿼리 작성 및 전송, 자원 해제 순으로 이용해야한다.

![20210107_100123](https://user-images.githubusercontent.com/59816811/103838019-5cc81180-50cf-11eb-87aa-c89e5d1ed039.png)

#### 1) 드라이버 로딩

JDBC 드라이버란 DBMS와 통신을 담당하는 자바 클래스로 DBMS 별로 JDBC 드라이버가 존재하고 JDBC를 이용하기 위해서는 먼저 드라이버를 로딩해줘야한다.

```java
Class.forName("JDBC드라이버 이름");
```

- MySQL : com.mysql.jdbc.Driver
- Oracle : oracle.jdbc.driver.OracleDriver

<br>

#### 2) DB 연결

DBMS와 연결을 위해서는 식별 값으로 JDBC URL 을 이용해야한다. JDBC 드라이버에 따라 형식이 다르다.

- MySQL : jdbc:mysql://HOST[:PORT]/DBNAME[?param=value&param1=value1&..]
- Oracle : jdbc:oracle:thin:@HOST:PORT:SID

DriverManager 객체의 getConnection 메소드를 통해 JDBC URL, ID, PWD 를 파라미터로 넘겨주면 DB 와 연결 할 수 있다.

```java
private Connection conn = null;

conn = DriverManager.getConnection(url, userid, userpw);
```

 <br>	

#### 3) SQL 작성 및 전송

SQL 구문을 전송하고 실행하기 위해서는 Statement 객체를 이용해야한다.

Statement 와 PreparedStatement 2개의 객체를 주로 이용하고 PreparedStatement 은 처음 컴파일 후 따로 컴파일이 진행되지않아서 여러 번 수행될 때 Statement 객체보다 빠른 속도를 가지고 있다.

--> PreparedStatement 를 사용하자.

<br>

Statement 객체들은 다음과 같은 3개의 메소드를 가지고 있다.

<br>

##### execute 메소드

execute 메소드는 모든 유형의 SQL 문장과 함께 사용할 수 있으며 boolean 값을 반환합니다. 반환값이 true이면 getResultSet 메소드를 사용해 결과 집합을 얻을 수 있고, false 라면 업데이트 개수 또는 결과	ㄱ 없는 경우입니다.

```java
public boolean execute(String sql) throws SQLException;
```

<br>

##### executeUpdate 메소드

Insert, Delete, Update 쿼리를 날릴 때 사용하는 메소드로 반환 값은 해당 SQL 문 실행에 영향을 받는 행 수를 반환합니다.

```java
public int executeUpdate(String sql) throws SQLException;
```

<br>

##### executeQuery 메소드

Select 쿼리를 날릴 때 사용하는 메소드로 데이터 결과 집합을 반환합니다.

```java
public ResultSet executeQuery(String sql) throws SQLException;
```



#### 4) 자원해제

JDBC 를 이용해 DB 와 연결하고 쿼리를 날리려면 1) 2) 3) 에 의해 다음과 같은 객체들을 이용해야합니다.

```java
Connection conn = null;
PreparedStatement pstmt = null;
```

따라서 사용 후에는 자원 해제를 꼭 꼭 해줘야합니다.

```java
pstmt.close();
conn.close();
```



#### 5) 예제

##### INSERT 예제

```java
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String userid = "travelbeeee";
	private String userpw = "1234";
	
	private Connection conn = null;
	
	private PreparedStatement pstmt = null;
	
	private ResultSet rs = null;
	
	@Override	
	public int insertMember(Member member) {		
		int result = 0;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userid, userpw);
			String sql = "INSERT INTO member (id, username, email, pwd) values (member_seq.nextval, ?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getUsername());
			pstmt.setString(2, member.getEmail());
			pstmt.setString(3, member.getPwd());
			result = pstmt.executeUpdate();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
```

회원정보를 입력받아서 추가하는 쿼리를 PreparedStatement 객체와 executeUpdate 함수를 이용하고 있다.

<br>

##### DELETE 예제

```java
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String userid = "travelbeeee";
	private String userpw = "1234";
	
	private Connection conn = null;
	
	private PreparedStatement pstmt = null;
	
	private ResultSet rs = null;
	
    @Override
	public int deleteMember(int id) {
		int result = 0;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userid, userpw);
			String sql = "DELETE member WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			result = pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(pstmt != null) pstmt.close();
				if(conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
```

회원에 해당하는 primary_key 인 id 값을 받아 삭제하는 쿼리를 수행하고 있다.

<br>

