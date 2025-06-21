package com.everton.maintenance_control.dtos;

import com.everton.maintenance_control.model.ServiceOrder;
import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

class OrderServiceStatusDTOTest {

    @Test
    void testPojo() {
        assertPojoMethodsFor(ServiceOrder.class).testing(Method.GETTER, Method.SETTER)
                .areWellImplemented();

    }

}