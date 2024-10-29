package com.monitor;

import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.monitor.db.DBManager;

public class AppTest {

    private static DBManager dbm;

    @BeforeAll
    public static void setupDB() {
        dbm = new DBManager();
    }

    @AfterAll
    public static void closeDB() {
        if (dbm != null) {
            dbm.close();
        }
    }

    /**
     * Testet die Anwendung.
     */
    @Test
    public void testApp() {
        assertFalse(dbm.getConnection() == null, () -> "Connection must not be null");
    }

}
