package com.kerriline.location.mail;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.kerriline.location.IntegrationTest;

@IntegrationTest
public class MailManagerIT {
    @Autowired
    MailManager mailManager;

    @Test
    public void testITMailManager() {
        assertThat(mailManager.getAll1392MessageCount()).isGreaterThan(0).as("Should be non-zero");
    }

}
