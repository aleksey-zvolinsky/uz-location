package com.kerriline.location.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kerriline.location.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationResponseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationResponse.class);
        LocationResponse locationResponse1 = new LocationResponse();
        locationResponse1.setId(1L);
        LocationResponse locationResponse2 = new LocationResponse();
        locationResponse2.setId(locationResponse1.getId());
        assertThat(locationResponse1).isEqualTo(locationResponse2);
        locationResponse2.setId(2L);
        assertThat(locationResponse1).isNotEqualTo(locationResponse2);
        locationResponse1.setId(null);
        assertThat(locationResponse1).isNotEqualTo(locationResponse2);
    }
}
