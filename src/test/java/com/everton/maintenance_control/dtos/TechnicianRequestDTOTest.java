package com.everton.maintenance_control.dtos;

import com.everton.maintenance_control.model.Technician;
import org.junit.jupiter.api.Test;
import pl.pojo.tester.api.assertion.Method;

import static pl.pojo.tester.api.assertion.Assertions.assertPojoMethodsFor;

class TechnicianRequestDTOTest {

    @Test
    void testPojo() {
        assertPojoMethodsFor(Technician.class).testing(Method.GETTER, Method.SETTER)
                .areWellImplemented();

    }

}