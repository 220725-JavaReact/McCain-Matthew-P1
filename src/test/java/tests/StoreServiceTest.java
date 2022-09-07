package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import services.StoreService;

public class StoreServiceTest {

    @DisplayName(value = "Test StoreService.addCustomer()")
    @Test
    void testAddCustomer() {
        Assertions.assertNotEquals(-1, StoreService.addCustomer("Jimmy Test", "jimmy@test.com", "8675309"));
    }
}
