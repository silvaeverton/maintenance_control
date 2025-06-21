package com.everton.maintenance_control.exceptions.response;

import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

class ErrorResponseTest {

    @Test
    void testPojo() {
        assertPojoMethodsFor(ErrorResponse.class).testing(Method.GETTER, Method.SETTER)
                .areWellImplemented();

    }


}