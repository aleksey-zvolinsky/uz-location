package com.kerriline.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tank.
 */
@Entity
@Table(name = "tank")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tank implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "tank_number", nullable = false)
    private String tankNumber;

    @NotNull
    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @NotNull
    @Column(name = "client_name", nullable = false)
    private String clientName;

    @OneToMany(mappedBy = "tank")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "locationResponse", "tank" }, allowSetters = true)
    private Set<LocationRequest> locationRequests = new HashSet<>();

    @OneToMany(mappedBy = "tank")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "mileageResponse", "tank" }, allowSetters = true)
    private Set<MileageRequest> mileageRequests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tank id(Long id) {
        this.id = id;
        return this;
    }

    public String getTankNumber() {
        return this.tankNumber;
    }

    public Tank tankNumber(String tankNumber) {
        this.tankNumber = tankNumber;
        return this;
    }

    public void setTankNumber(String tankNumber) {
        this.tankNumber = tankNumber;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public Tank ownerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getClientName() {
        return this.clientName;
    }

    public Tank clientName(String clientName) {
        this.clientName = clientName;
        return this;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Set<LocationRequest> getLocationRequests() {
        return this.locationRequests;
    }

    public Tank locationRequests(Set<LocationRequest> locationRequests) {
        this.setLocationRequests(locationRequests);
        return this;
    }

    public Tank addLocationRequest(LocationRequest locationRequest) {
        this.locationRequests.add(locationRequest);
        locationRequest.setTank(this);
        return this;
    }

    public Tank removeLocationRequest(LocationRequest locationRequest) {
        this.locationRequests.remove(locationRequest);
        locationRequest.setTank(null);
        return this;
    }

    public void setLocationRequests(Set<LocationRequest> locationRequests) {
        if (this.locationRequests != null) {
            this.locationRequests.forEach(i -> i.setTank(null));
        }
        if (locationRequests != null) {
            locationRequests.forEach(i -> i.setTank(this));
        }
        this.locationRequests = locationRequests;
    }

    public Set<MileageRequest> getMileageRequests() {
        return this.mileageRequests;
    }

    public Tank mileageRequests(Set<MileageRequest> mileageRequests) {
        this.setMileageRequests(mileageRequests);
        return this;
    }

    public Tank addMileageRequest(MileageRequest mileageRequest) {
        this.mileageRequests.add(mileageRequest);
        mileageRequest.setTank(this);
        return this;
    }

    public Tank removeMileageRequest(MileageRequest mileageRequest) {
        this.mileageRequests.remove(mileageRequest);
        mileageRequest.setTank(null);
        return this;
    }

    public void setMileageRequests(Set<MileageRequest> mileageRequests) {
        if (this.mileageRequests != null) {
            this.mileageRequests.forEach(i -> i.setTank(null));
        }
        if (mileageRequests != null) {
            mileageRequests.forEach(i -> i.setTank(this));
        }
        this.mileageRequests = mileageRequests;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tank)) {
            return false;
        }
        return id != null && id.equals(((Tank) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tank{" +
            "id=" + getId() +
            ", tankNumber='" + getTankNumber() + "'" +
            ", ownerName='" + getOwnerName() + "'" +
            ", clientName='" + getClientName() + "'" +
            "}";
    }
}
