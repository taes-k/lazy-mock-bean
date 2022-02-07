# lazy-mock-bean

`@SpringBootTest`수행중 Context reload 과정없이 `@MockBean`, `@MockSpy`를 설정해줍니다.  

`@MockBean`, `@SpyBean` 사용시 신규 `Bean` 설정을 위해 SpringContext reload가 발생합니다.  
대규모 시스템에서의 잦은 SpringContext reload는 테스트 수행 속도에 영향을 주기때문에 `lazy-mock-bean`을 통해 테스트 속도 저하를 방지 할 수 있습니다.

### Getting started

```gradle
repositories {
    ...
    maven(url = "https://jitpack.io")
}

dependencies {
    testImplementation("com.github.taes-k:lazy-mock-bean:$release-version")
    ...
}
```

- [sample](https://github.com/taes-k/lazy-mock-bean/tree/main/sample-app)

### Examples

```kotlin
class SomethingServiceTest {

    // If target field is not defined, autoscan mode is used.
    // When using autoscan mode, at least one autowired field must be included. 
    // Autoscan mode automatically scans the bean's dependency tree.
    @LazySpyBean 
    private lateinit var sampleService1: SampleService1
    
    // If target field is defined, only the field of the selected bean is replaced with a mock field.
    // This can make the mocking setup faster.
    @LazyMockBean(targets = [SampleController::class])
    private lateinit var sampleService2: SampleService2

    @LazySpyBean(targets = [SampleController::class])
    private lateinit var sampleService3: SampleService3
    
    @Autowired
    private lateinit var sut: SampleController
    
    @Test
    fun doSomething_autoFindBeanMocking() {
        // given
        Mockito.`when`(sampleService3().getSample()).thenReturn(...);

        // when
        var result = sut.doSomething(...);

        // then
        then()...
    }

    @Test
    fun doSomething_withMock() {
        // given
        Mockito.`when`(sampleService1().getSample()).thenReturn(...);

        // when
        var result = sut.doSomething(...);

        // then
        then()...
    }
    
    @Test
    fun doSomething_withSpy() {
        // given
        Mockito.`when`(sampleService2().getSample()).thenReturn(...);

        // when
        var result = sut.doSomething(...);

        // then
        then()...
    }
}
```

---

### Features

- `@LazyMockBean`
- `@LazySpyBean`
