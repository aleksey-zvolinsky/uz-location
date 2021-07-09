package com.kerriline.location.mail;

import com.kerriline.location.IntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;

@IntegrationTest
public class MailManagerIT {
    @Autowired
    MailManager mailManager;

    @Test
    public void testITMailManager() {
        assertThat("Should be non-zero", mailManager.getAll1392MessageCount(), Matchers.greaterThan(0));
    }

}
