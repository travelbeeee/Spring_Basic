# Validation

입력폼에서 넘어온 값들을 검증하는 작업.

컨트롤러의 중요한 역할 중 하나는 HTTP 요청이 정상인지 검증하는 것!

`Item` 객체의 값을 받아오는 상황에서 값들을 검증해보자.

```java
public class Item {
    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;
}
```

<br>

#### 1) 직접 검증하기

```java
@PostMapping("/add")
public String addItem(@ModelAttribute Item item, Model model, RedirectAttributes redirectAttributes) {

    // 검증 오류 결과를 보관
    Map<String, String> errors = new HashMap<>();

    // 검증 로직
    if(!StringUtils.hasText(item.getItemName())){
        errors.put("itemName", "상품 이름은 필수입니다.");
    }
    if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000){
        errors.put("price", "가격은 1,000원 ~ 1,000,000원 까지 허용합니다.");
    }
    if (item.getQuantity() == null || item.getQuantity() >= 9999) {
        errors.put("quantity", "수량은 최대 9,999까지 허용합니다.");
    }

    // 특정 필드가 아닌 복합 룰 검증
    if (item.getPrice() != null  && item.getQuantity() != null){
        int resultPrice = item.getPrice() * item.getQuantity();
        if(resultPrice < 10000){
            errors.put("globalError", "가격 * 수량의 합은 10,000원 이상이어야 합니다.");
        }
    }

    if (!errors.isEmpty()) {
        model.addAttribute("errors", errors);
        return "validation/v1/addForm";
    }

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v1/items/{itemId}";
}
```

컨트롤러에서 모든 Item 객체의 값들을 확인하고 error 메시지를 담을 객체를 만들어서 Model에 담아 View로 보내줘야한다.

직접 다 해야하다보니 번거롭고 컨트롤러에 검증 로직이 굉장히 많아진다.

또, Item 객체의 필드 타입에 맞지 않는 입력이 넘어온 경우는 다룰 수가 없다. (예를 들어, 나이 입력 창에 qqq 등을 입력 )

<br>

#### 2) Spring BindingResult 이용하기

##### -BindingResult

```java
@PostMapping("/add")
public String addItem(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
	// ~~ 로직 ~~ //
}
```

- BindingResult에 담겨있는 값들은 Spring이 자동으로 Model에 추가해준다.
- BindingResult 객체는 @ModelAttribute 객체 다음에 와야한다. BindingResult는 해당 객체를 검증한 결과를 의미하는 객체다.
- @ModelAttribute에 바인딩 시 타입 오류가 발생하면 BindingResult 에 오류 정보( FieldError) 가 담기고 컨트롤러를 정상 호출합니다. BindingResult가 없다면, 400 오류가 발생하면서 컨트롤러가 호출되지 않습니다.

<br>

##### -FieldError

FieldError는 이름 그대로 필드에 오류가 있을 때 사용하는 객체로 BindingResult 객체 안에 담아두면 된다. ObjectError를 상속하고 있습니다.

rejectedValue 를 넘겨주는 생성자를 이용하면 사용자가 입력한 잘못된 값을 유지할 수 있다.

> 사용자의 입력 데이터가 @ModelAttribute에 바인딩되는 시점에 오류가 발생하면 모델 객체에 사용자 입력 값을 유지하기가 힘들다. **이때, 스프링에서는 사용자의 입력 값을 파라미터로 넘겨주면서 FieldError에서 사용자가 입력한 값을 저장해준다.**

```java
// objectName : @ModelAttribute 객체 이름
// field : 오류가 발생한 필드 이름
// defaultMessage : 오류 기본 메시지
public FieldError(String objectname, String field, Strign defaultMessage) {}

// objectName : @ModelAttribute 객체 이름
// field : 오류가 발생한 필드 이름
// rejectedValue : 사용자가 입력한 값(거절된 값)
// bindingFailure : 타입 오류 같은 바인딩 실패인지 구분하는 값
// codes 메시지 코드
// arguments : 메시지에서 사용하는 인자
// defaultMessage : 오류 기본 메시지
public FieldError(String objectName, String field, @Nullable Object
rejectedValue, boolean bindingFailure, @Nullable String[] codes, @Nullable
Object[] arguments, @Nullable String defaultMessage)
```

<br>

##### -ObjectError

ObjectError는 특정 필드를 넘어서는 오류가 있을 때 사용하는 객체입니다.

```java
// objectName : @ModelAttribute 객체 이름
// defaultMessage : 오류 기본 메시지
public ObjectError(String objectname, Strign defaultMessage) {}
```

<br>

##### -BindingResult와 Errors

- `org.springframework.validation.Errors`
- `org.springframework.validation.BindingResult`

`BindingResult`는 인터페이스고, `Errors`인터페이스를 상속받고 있다. 실제 넘어오는 구현체는 `BeanPropertyBindingResult`로 두 인터페이스 모두 구현하고 있으므로 `BindingResult` 대신에 `Errors`를 사용해도 된다.

`Errors` 인터페이스는 단순한 오류 저장과 조회 기능만 제공하고, `BindingResult`는 여기에 기능이 추가된 객체다.

<br>

#### 3) 오류 코드 처리

