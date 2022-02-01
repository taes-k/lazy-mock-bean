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

    @LazyMockBean(targets = [SampleController::class])
    private lateinit var sampleService1: SampleService1


    @LazySpyBean(targets = [SampleController::class])
    private lateinit var sampleService2: SampleService2
    
    @Autowired
    private lateinit var sut: SampleController

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
