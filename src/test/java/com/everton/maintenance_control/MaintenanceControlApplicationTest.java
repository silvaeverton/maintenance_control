package com.everton.maintenance_control;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceControlApplicationTest {

    @Test
        void mainMethodRunsWithoutExceptions() {
            assertDoesNotThrow(() -> MaintenanceControlApplication.main(new String[] {}));
        }
    }
