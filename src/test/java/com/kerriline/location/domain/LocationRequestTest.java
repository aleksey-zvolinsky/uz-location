package com.kerriline.location.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kerriline.location.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocationRequest.class);
        LocationRequest locationRequest1 = new LocationRequest();
        locationRequest1.setId(1L);
        LocationRequest locationRequest2 = new LocationRequest();
        locationRequest2.setId(locationRequest1.getId());
        assertThat(locationRequest1).isEqualTo(locationRequest2);
        locationRequest2.setId(2L);
        assertThat(locationRequest1).isNotEqualTo(locationRequest2);
        locationRequest1.setId(null);
        assertThat(locationRequest1).isNotEqualTo(locationRequest2);
    }
}
