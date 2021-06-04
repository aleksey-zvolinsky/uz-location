package location.uz.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MileageResponse.
 */
@Entity
@Table(name = "mileage_response")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MileageResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "response_datetime")
    private LocalDate responseDatetime;

    @Column(name = "tank_number")
    private String tankNumber;

    @Column(name = "mileage_current")
    private String mileageCurrent;

    @Column(name = "mileage_datetime")
    private String mileageDatetime;

    @Column(name = "mileage_remain")
    private String mileageRemain;

    @Column(name = "mileage_update_datetime")
    private String mileageUpdateDatetime;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MileageResponse id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getResponseDatetime() {
        return this.responseDatetime;
    }

    public MileageResponse responseDatetime(LocalDate responseDatetime) {
        this.responseDatetime = responseDatetime;
        return this;
    }

    public void setResponseDatetime(LocalDate responseDatetime) {
        this.responseDatetime = responseDatetime;
    }

    public String getTankNumber() {
        return this.tankNumber;
    }

    public MileageResponse tankNumber(String tankNumber) {
        this.tankNumber = tankNumber;
        return this;
    }

    public void setTankNumber(String tankNumber) {
        this.tankNumber = tankNumber;
    }

    public String getMileageCurrent() {
        return this.mileageCurrent;
    }

    public MileageResponse mileageCurrent(String mileageCurrent) {
        this.mileageCurrent = mileageCurrent;
        return this;
    }

    public void setMileageCurrent(String mileageCurrent) {
        this.mileageCurrent = mileageCurrent;
    }

    public String getMileageDatetime() {
        return this.mileageDatetime;
    }

    public MileageResponse mileageDatetime(String mileageDatetime) {
        this.mileageDatetime = mileageDatetime;
        return this;
    }

    public void setMileageDatetime(String mileageDatetime) {
        this.mileageDatetime = mileageDatetime;
    }

    public String getMileageRemain() {
        return this.mileageRemain;
    }

    public MileageResponse mileageRemain(String mileageRemain) {
        this.mileageRemain = mileageRemain;
        return this;
    }

    public void setMileageRemain(String mileageRemain) {
        this.mileageRemain = mileageRemain;
    }

    public String getMileageUpdateDatetime() {
        return this.mileageUpdateDatetime;
    }

    public MileageResponse mileageUpdateDatetime(String mileageUpdateDatetime) {
        this.mileageUpdateDatetime = mileageUpdateDatetime;
        return this;
    }

    public void setMileageUpdateDatetime(String mileageUpdateDatetime) {
        this.mileageUpdateDatetime = mileageUpdateDatetime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MileageResponse)) {
            return false;
        }
        return id != null && id.equals(((MileageResponse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MileageResponse{" +
            "id=" + getId() +
            ", responseDatetime='" + getResponseDatetime() + "'" +
            ", tankNumber='" + getTankNumber() + "'" +
            ", mileageCurrent='" + getMileageCurrent() + "'" +
            ", mileageDatetime='" + getMileageDatetime() + "'" +
            ", mileageRemain='" + getMileageRemain() + "'" +
            ", mileageUpdateDatetime='" + getMileageUpdateDatetime() + "'" +
            "}";
    }
}
