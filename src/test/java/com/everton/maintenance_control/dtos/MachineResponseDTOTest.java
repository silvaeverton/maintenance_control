package com.everton.maintenance_control.dtos;

import com.everton.maintenance_control.model.Machine;
import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;


class MachineResponseDTOTest {

    @Test
    void testPojo() {
        assertPojoMethodsFor(Machine.class).testing(Method.GETTER, Method.SETTER)
                .areWellImplemented();

    }
}