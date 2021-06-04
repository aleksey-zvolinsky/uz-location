package location.uz.domain;

import static org.assertj.core.api.Assertions.assertThat;

import location.uz.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MileageRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MileageRequest.class);
        MileageRequest mileageRequest1 = new MileageRequest();
        mileageRequest1.setId(1L);
        MileageRequest mileageRequest2 = new MileageRequest();
        mileageRequest2.setId(mileageRequest1.getId());
        assertThat(mileageRequest1).isEqualTo(mileageRequest2);
        mileageRequest2.setId(2L);
        assertThat(mileageRequest1).isNotEqualTo(mileageRequest2);
        mileageRequest1.setId(null);
        assertThat(mileageRequest1).isNotEqualTo(mileageRequest2);
    }
}
