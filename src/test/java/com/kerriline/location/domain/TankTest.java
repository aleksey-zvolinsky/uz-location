package com.kerriline.location.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kerriline.location.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TankTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tank.class);
        Tank tank1 = new Tank();
        tank1.setId(1L);
        Tank tank2 = new Tank();
        tank2.setId(tank1.getId());
        assertThat(tank1).isEqualTo(tank2);
        tank2.setId(2L);
        assertThat(tank1).isNotEqualTo(tank2);
        tank1.setId(null);
        assertThat(tank1).isNotEqualTo(tank2);
    }
}
