package com.kerriline.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MileageRequest.
 */
@Entity
@Table(name = "mileage_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MileageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_datetime")
    private ZonedDateTime requestDatetime;

    @Column(name = "tank_numbers")
    private String tankNumbers;

    @JsonIgnoreProperties(value = { "mileageRequest" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private MileageResponse mileageResponse;

    @ManyToOne
    @JsonIgnoreProperties(value = { "locationRequests", "mileageRequests" }, allowSetters = true)
    private Tank tank;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MileageRequest id(Long id) {
        this.id = id;
        return this;
    }

    public ZonedDateTime getRequestDatetime() {
        return this.requestDatetime;
    }

    public MileageRequest requestDatetime(ZonedDateTime requestDatetime) {
        this.requestDatetime = requestDatetime;
        return this;
    }

    public void setRequestDatetime(ZonedDateTime requestDatetime) {
        this.requestDatetime = requestDatetime;
    }

    public String getTankNumbers() {
        return this.tankNumbers;
    }

    public MileageRequest tankNumbers(String tankNumbers) {
        this.tankNumbers = tankNumbers;
        return this;
    }

    public void setTankNumbers(String tankNumbers) {
        this.tankNumbers = tankNumbers;
    }

    public MileageResponse getMileageResponse() {
        return this.mileageResponse;
    }

    public MileageRequest mileageResponse(MileageResponse mileageResponse) {
        this.setMileageResponse(mileageResponse);
        return this;
    }

    public void setMileageResponse(MileageResponse mileageResponse) {
        this.mileageResponse = mileageResponse;
    }

    public Tank getTank() {
        return this.tank;
    }

    public MileageRequest tank(Tank tank) {
        this.setTank(tank);
        return this;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MileageRequest)) {
            return false;
        }
        return id != null && id.equals(((MileageRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MileageRequest{" +
            "id=" + getId() +
            ", requestDatetime='" + getRequestDatetime() + "'" +
            ", tankNumbers='" + getTankNumbers() + "'" +
            "}";
    }
}
