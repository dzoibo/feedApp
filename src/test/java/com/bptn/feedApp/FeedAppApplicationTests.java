package com.bptn.feedApp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FeedAppApplicationTests {

    // REMOVED: @MockitoBean Server server; 
    // Reason: Unless your main app creates an H2 Server bean explicitly, 
    // mocking it here is unnecessary and can cause context load failures.
    // If you need to test H2 connectivity, the configuration is already handled 
    // by the 'h2' dependency and 'application-test.properties'.

    @Test
    void contextLoads() {
        // Verifies that the Spring application context starts successfully
    }
}
