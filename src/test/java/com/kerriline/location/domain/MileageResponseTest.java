package com.kerriline.location.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.kerriline.location.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MileageResponseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MileageResponse.class);
        MileageResponse mileageResponse1 = new MileageResponse();
        mileageResponse1.setId(1L);
        MileageResponse mileageResponse2 = new MileageResponse();
        mileageResponse2.setId(mileageResponse1.getId());
        assertThat(mileageResponse1).isEqualTo(mileageResponse2);
        mileageResponse2.setId(2L);
        assertThat(mileageResponse1).isNotEqualTo(mileageResponse2);
        mileageResponse1.setId(null);
        assertThat(mileageResponse1).isNotEqualTo(mileageResponse2);
    }
}
