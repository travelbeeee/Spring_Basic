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

```java
@PostMapping("/add")
public String addItem(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

    //검증 로직
    if (!StringUtils.hasText(item.getItemName())) {
        bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
    }
    if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
        bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."));
    }
    if (item.getQuantity() == null || item.getQuantity() >= 9999) {
        bindingResult.addError(new FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."));
    }

    //특정 필드가 아닌 복합 룰 검증
    if (item.getPrice() != null && item.getQuantity() != null) {
        int resultPrice = item.getPrice() * item.getQuantity();
        if (resultPrice < 10000) {
            bindingResult.addError(new ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = " + resultPrice));
        }
    }

    //검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
        log.info("errors={} ", bindingResult);
        return "validation/v2/addForm";
    }

    //성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
}
```

- BindingResult에 담겨있는 값들은 Spring이 자동으로 Model에 추가해준다.
- BindingResult 객체는 @ModelAttribute 객체 다음에 와야한다. BindingResult는 해당 객체를 검증한 결과를 의미하는 객체다.

<br>

FieldError는 이름 그대로 필드에 오류가 있을 때 사용하는 객체로 BindingResult 객체 안에 담아두면 된다. ObjectError를 상속하고 있습니다.

```java
// objectName : @ModelAttribute 객체 이름
// field : 오류가 발생한 필드 이름
// defaultMessage : 오류 기본 메시지
public FieldError(String objectname, String field, Strign defaultMessage) {}
```

ObjectError는 특정 필드를 넘어서는 오류가 있을 때 사용하는 객체입니다.

```java
// objectName : @ModelAttribute 객체 이름
// defaultMessage : 오류 기본 메시지
public ObjectError(String objectname, Strign defaultMessage) {}
```

