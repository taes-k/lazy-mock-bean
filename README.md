# lazy-mock-bean

LazyMockBean allows you to create a `MockingBean` without reloading spring context during `@SpringBootTest`

If you use `@MockBean` then the Spring Context will be reloaded to configure the `Mocking Bean`. This negatively affects
the test code execution performance. SpringContext reload on large systems takes a long time.

### Getting started

build.gradle

```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}

dependencies {
    testImplementation 'com.github.taes-k:lazy-mock-bean:$release-version'
}
```

build.gradle.kts

```kotlin
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

java

```java
class SomethingServiceTest {

    // If target fields are not defined, autoscan mode is used. 
    // When using autoscan mode, at least one `@LazyInjectMockBeans` field must be included. 
    // Autoscan mode automatically scans the bean's dependency tree.
    @LazySpyBean
    private SampleService1 sampleService1;

    // If target fields are defined, the bean of the selected type is replaced with a mock field.
    // This can make the mocking setup faster than autoscan mode.
    @LazyMockBean({SampleController.class})
    private SampleService2 sampleService2;

    @LazySpyBean({SampleController.class})
    private SampleService3 sampleService3;

    @LazyInjectMockBeans
    @Autowired
    private SampleController sut;

    @Test
    void doSomething_autoFindBeanMocking() {
        // given
        Mockito.when(sampleService3.getSample()).thenReturn(...);

        // when
        var result = sut.doSomething(...);

        // then
        then()...
    }

    @Test
    void doSomething_withMock() {
        // given
        Mockito.when(sampleService1.getSample()).thenReturn(...);

        // when
        var result = sut.doSomething(...);

        // then
        then()...
    }

    @Test
    void doSomething_withSpy() {
        // given
        Mockito.when(sampleService2.getSample()).thenReturn(...);

        // when
        var result = sut.doSomething(...);

        // then
        then()...
    }
}
```

kotlin

```kotlin
class SomethingServiceTest {

    // If target fields are not defined, autoscan mode is used. 
    // When using autoscan mode, at least one `@LazyInjectMockBeans` field must be included. 
    // Autoscan mode automatically scans the bean's dependency tree.
    @LazySpyBean
    private lateinit var sampleService1: SampleService1

    // If target fields are defined, the bean of the selected type is replaced with a mock field.
    // This can make the mocking setup faster than autoscan mode.
    @LazyMockBean(value = [SampleController::class])
    private lateinit var sampleService2: SampleService2

    @LazySpyBean(value = [SampleController::class])
    private lateinit var sampleService3: SampleService3

    @LazyInjectMockBeans
    @Autowired
    private lateinit var sut: SampleController

    @Test
    fun doSomething_autoFindBeanMocking() {
        // given
        Mockito.`when`(sampleService3.getSample()).thenReturn(...);

        // when
        val result = sut.doSomething(...);

        // then
        then()...
    }

    @Test
    fun doSomething_withMock() {
        // given
        Mockito.`when`(sampleService1.getSample()).thenReturn(...);

        // when
        val result = sut.doSomething(...);

        // then
        then()...
    }

    @Test
    fun doSomething_withSpy() {
        // given
        Mockito.`when`(sampleService2.getSample()).thenReturn(...);

        // when
        val result = sut.doSomething(...);

        // then
        then()...
    }
}
```

---

### Features

- `@LazyMockBean`
- `@LazySpyBean`
- `@LazyInjectMockBeans`
