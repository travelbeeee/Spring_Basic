# @NotNull, @NotEmpty, @NotBlank 정리

### 1) @NotNull

```java
@Target(value={METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER,TYPE_USE})
 @Retention(value=RUNTIME)
 @Repeatable(value=NotNull.List.class)
 @Documented
 @Constraint(validatedBy={})
public @interface NotNull
```

`The annotated element must not be null. Accepts any type.`

모든 타입에 대해서 지원해주고, null이면 안된다.

<br>

### 2) @NotEmpty

```java
@Documented
 @Constraint(validatedBy={})
 @Target(value={METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER,TYPE_USE})
 @Retention(value=RUNTIME)
 @Repeatable(value=NotEmpty.List.class)
public @interface NotEmpty
```

`The annotated element must not be null nor empty.` 

Supported types are:

- `CharSequence` (length of character sequence is evaluated)
- `Collection` (collection size is evaluated)
- `Map` (map size is evaluated)
- `Array` (array length is evaluated)

위의 4개의 타입에 대해서만 지원하고, null 이거나 empty 면 안된다.

<br>

### 3) @NotBlank

```java
@Documented
 @Constraint(validatedBy={})
 @Target(value={METHOD,FIELD,ANNOTATION_TYPE,CONSTRUCTOR,PARAMETER,TYPE_USE})
 @Retention(value=RUNTIME)
 @Repeatable(value=NotBlank.List.class)
public @interface NotBlank
```

`The annotated element must not be null and must contain at least one non-whitespace character. Accepts CharSequence.`

`CharSequence` 타입에 대해서만 지원하고, null 이거나 "" 이거나 " " 3가지 경우는 안된다.