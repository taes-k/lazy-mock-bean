# lazy-mock-bean

LazyMockBean allows you to create a `MockingBean` without reloading spring context during `@SpringBootTest`

If you use `@MockBean` then the Spring Context will be reloaded to configure the `Mocking Bean`.
This negatively affects the test code execution performance. SpringContext reload on large systems takes a long time.

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
