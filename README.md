# lazy-mock-bean

`@SpringBootTest`수행중에 `@MockBean`사용시 `Mock-Bean` 생성을 위해 SpringContext reload가 발생합니다. 대규모 시스템에서는 Springcontext reload는
테스트수행 지연을 유발하기 context reload를 유발시키지않는 mockBean 설정을 제공합니다.

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

### Examples

As-Is

```kotlin
class SomethingServiceTest {

    @LazyMockBean(targets = [SampleController])
    private lateinit var sampleService1: SampleService1

    @Autowired
    private lateinit var sut: SampleController

    @Test
    fun doSomething() {
        // given
        Mockito.`when`(sampleService1().getSample()).thenReturn(...);

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