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
    @JsonIgnoreProperties(value = { "tankNumber", "tank" }, allowSetters = true)
    private Set<LocationRequest> tankNumbers = new HashSet<>();

    @OneToMany(mappedBy = "tank")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tankNumber", "tank" }, allowSetters = true)
    private Set<MileageRequest> tankNumbers = new HashSet<>();

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

    public Set<LocationRequest> getTankNumbers() {
        return this.tankNumbers;
    }

    public Tank tankNumbers(Set<LocationRequest> locationRequests) {
        this.setTankNumbers(locationRequests);
        return this;
    }

    public Tank addTankNumber(LocationRequest locationRequest) {
        this.tankNumbers.add(locationRequest);
        locationRequest.setTank(this);
        return this;
    }

    public Tank removeTankNumber(LocationRequest locationRequest) {
        this.tankNumbers.remove(locationRequest);
        locationRequest.setTank(null);
        return this;
    }

    public void setTankNumbers(Set<LocationRequest> locationRequests) {
        if (this.tankNumbers != null) {
            this.tankNumbers.forEach(i -> i.setTank(null));
        }
        if (locationRequests != null) {
            locationRequests.forEach(i -> i.setTank(this));
        }
        this.tankNumbers = locationRequests;
    }

    public Set<MileageRequest> getTankNumbers() {
        return this.tankNumbers;
    }

    public Tank tankNumbers(Set<MileageRequest> mileageRequests) {
        this.setTankNumbers(mileageRequests);
        return this;
    }

    public Tank addTankNumber(MileageRequest mileageRequest) {
        this.tankNumbers.add(mileageRequest);
        mileageRequest.setTank(this);
        return this;
    }

    public Tank removeTankNumber(MileageRequest mileageRequest) {
        this.tankNumbers.remove(mileageRequest);
        mileageRequest.setTank(null);
        return this;
    }

    public void setTankNumbers(Set<MileageRequest> mileageRequests) {
        if (this.tankNumbers != null) {
            this.tankNumbers.forEach(i -> i.setTank(null));
        }
        if (mileageRequests != null) {
            mileageRequests.forEach(i -> i.setTank(this));
        }
        this.tankNumbers = mileageRequests;
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
