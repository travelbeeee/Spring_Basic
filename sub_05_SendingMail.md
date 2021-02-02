# 메일보내기

### 1) SMTP (Simple Mail Transfer Protocol)

`SMTP`는 `간이 전자 우편 전송 프로토콜` 로 인터넷에서 이메일을 보내기 위해 이용되는 프로토콜을 의미합니다. SMTP 서버를 직접 구축할 수도 있고, 네이버나 구글 등에서 제공해주는 SMTP 서버를 사용할 수도 있습니다.

**[구글SMTP]**

<img src="https://user-images.githubusercontent.com/59816811/106543562-4e8fd880-6549-11eb-8fcb-adafeaa940df.png" alt="gmail1" width="700" />

<br>

**[네이버SMTP]**

<img src="https://user-images.githubusercontent.com/59816811/106543602-62d3d580-6549-11eb-8f3f-eba10ad611e4.png" alt="nmail"  width="700" />

<br>

### 2) MailSender / JavaMailSender

스프링에서는 메일 발송 기능을 위해 MailSender, JavaMailSender 인터페이스를 지원해주고 있습니다.

```java
public interface MailSender {
	void send(SimpleMailMessage simpleMessage) throws MailException;
	void send(SimpleMailMessage... simpleMessages) throws MailException;
}
```

```java
public interface JavaMailSender extends MailSender {
	MimeMessage createMimeMessage();
	MimeMessage createMimeMessage(InputStream contentStream) throws MailException;
	void send(MimeMessage mimeMessage) throws MailException;
	void send(MimeMessage... mimeMessages) throws MailException;
	void send(MimeMessagePreparator mimeMessagePreparator) throws MailException;
	void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException;
}
```

메일 제목, 단순 텍스트 내용을 전달받아 메일을 발송하는 MailSender 인터페이스와 이를 상속하면서 MimeMessage를 이용해서 메일을 발송하는 기능이 추가된 JavaMailSender 인터페이스가 있습니다.

<br>

### 3) JavaMailSenderImpl

위의 인터페이스를 구현한 구현체를 스프링 빈으로 등록해줘야 우리는 최종적으로 메일을 보낼 수 있습니다.

네이버 / 구글 SMTP를 이용하려면 다음과 같은 설정을 해줘야합니다.

**그 전에 구글은 "보안수준이 낮은 앱의 액세스"를 설정해줘야 사용할 수 있습니다.**

```properties
mail.smtp.auth=true
mail.smtp.starttls.required=true
mail.smtp.starttls.enable=true
mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
mail.smtp.socketFactory.fallback=false
mail.smtp.port=465
mail.smtp.socketFactory.port=465

#admin 구글 아이디 계정 id,password
AdminGMail.id = 구글 계정아이디
AdminGMail.password = 구글 계정비밀번호

#admin 네이버 아이디 계정 id,password
AdminNMail.id = 네이버 계정아이디
AdminNMail.password = 네이버 계정비밀번호
```

mail.properties 를 따로 만들어서 관리하면된다.

**[Gmail JavaMailSender 구현체]**

```java
@Configuration
@PropertySource("classpath:mail.properties")
public class GMailConfiguration {

    @Value("${mail.smtp.port}")
    private int port;
    @Value("${mail.smtp.socketFactory.port}")
    private int socketPort;
    @Value("${mail.smtp.auth}")
    private boolean auth;
    @Value("${mail.smtp.starttls.enable}")
    private boolean starttls;
    @Value("${mail.smtp.starttls.required}")
    private boolean startlls_required;
    @Value("${mail.smtp.socketFactory.fallback}")
    private boolean fallback;
    @Value("${AdminGMail.id}")
    private String id;
    @Value("${AdminGMail.password}")
    private String password;

    @Bean
    public JavaMailSender javaGmailSender() {

        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setUsername(id);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(port);
        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");
        return javaMailSender;
    }
    private Properties getMailProperties()
    {
        Properties pt = new Properties();
        pt.put("mail.smtp.socketFactory.port", socketPort);
        pt.put("mail.smtp.auth", auth);
        pt.put("mail.smtp.starttls.enable", starttls);
        pt.put("mail.smtp.starttls.required", startlls_required);
        pt.put("mail.smtp.socketFactory.fallback",fallback);
        pt.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        return pt;
    }
}
```

```java
 @PostMapping("GmailSending")
    public String sendingGMail(HttpServletRequest httpServletRequest){
        String fromEmail = "sochun2528@gmail.com";
        String toEmail = httpServletRequest.getParameter("email");
        String title = "회원가입인증메일입니다.";
        Random r = new Random();
        int code = r.nextInt(4589362) + 49311;

        String content = System.getProperty("line.separator")+ //한줄씩 줄간격을 두기위해 작성
                System.getProperty("line.separator")+
                "안녕하세요 회원님 저희 홈페이지를 찾아주셔서 감사합니다"
                +System.getProperty("line.separator")+
                System.getProperty("line.separator")+
                " 인증번호는 " + code + " 입니다. "
                +System.getProperty("line.separator")+
                System.getProperty("line.separator")+
                "받으신 인증번호를 홈페이지에 입력해 주시면 다음으로 넘어갑니다."; // 내용

        try {
            MimeMessage message = javaGmailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message,
                    true, "UTF-8");

            messageHelper.setFrom(fromEmail); // 보내는사람 생략하면 정상작동을 안함
            messageHelper.setTo(toEmail); // 받는사람 이메일
            messageHelper.setSubject(title); // 메일제목은 생략이 가능하다
            messageHelper.setText(content); // 메일 내용

            javaGmailSender.send(message);
        } catch (Exception e) {
            System.out.println(e);
        }
        return "redirect:/";
    }
```

컨트롤러에서 위에서 구현한 구현체를 주입받아서 사용할 수 있습니다.

<br>

<br>

### 에러

네이버도 똑같이 하면 되야하는데... Username and Password unaccepted 에러가 계속 발생....

흠.........

구글 보안설정처럼 네이버도 추가 조치를 해줘야하나...?!

네이버메일 POP3/SMTP 설정 사용으로 변경해도 안된다...