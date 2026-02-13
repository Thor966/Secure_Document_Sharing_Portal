package com.doc.util;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class EmailHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {

        try {
            // Test email logic here
            return Health.up()
                    .withDetail("Email Service", "Operational")
                    .build();

        } catch (Exception e) {

            return Health.down()
                    .withDetail("Email Service", "Failed")
                    .build();
        }
    }
}

