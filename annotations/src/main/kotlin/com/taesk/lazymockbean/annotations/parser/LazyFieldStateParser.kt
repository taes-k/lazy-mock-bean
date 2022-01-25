package com.taesk.lazymockbean.annotations.parser

import com.taesk.lazymockbean.annotations.data.LazyMockFieldState
import org.springframework.test.context.TestContext

interface LazyFieldStateParser {
    fun parse(testContext: TestContext): Set<LazyMockFieldState>
}
