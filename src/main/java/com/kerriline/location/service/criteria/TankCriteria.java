package com.kerriline.location.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.kerriline.location.domain.Tank} entity. This class is used
 * in {@link com.kerriline.location.web.rest.TankResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /tanks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TankCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter tankNumber;

    private StringFilter ownerName;

    private StringFilter clientName;

    private LongFilter locationRequestId;

    private LongFilter mileageRequestId;

    public TankCriteria() {}

    public TankCriteria(TankCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.tankNumber = other.tankNumber == null ? null : other.tankNumber.copy();
        this.ownerName = other.ownerName == null ? null : other.ownerName.copy();
        this.clientName = other.clientName == null ? null : other.clientName.copy();
        this.locationRequestId = other.locationRequestId == null ? null : other.locationRequestId.copy();
        this.mileageRequestId = other.mileageRequestId == null ? null : other.mileageRequestId.copy();
    }

    @Override
    public TankCriteria copy() {
        return new TankCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTankNumber() {
        return tankNumber;
    }

    public StringFilter tankNumber() {
        if (tankNumber == null) {
            tankNumber = new StringFilter();
        }
        return tankNumber;
    }

    public void setTankNumber(StringFilter tankNumber) {
        this.tankNumber = tankNumber;
    }

    public StringFilter getOwnerName() {
        return ownerName;
    }

    public StringFilter ownerName() {
        if (ownerName == null) {
            ownerName = new StringFilter();
        }
        return ownerName;
    }

    public void setOwnerName(StringFilter ownerName) {
        this.ownerName = ownerName;
    }

    public StringFilter getClientName() {
        return clientName;
    }

    public StringFilter clientName() {
        if (clientName == null) {
            clientName = new StringFilter();
        }
        return clientName;
    }

    public void setClientName(StringFilter clientName) {
        this.clientName = clientName;
    }

    public LongFilter getLocationRequestId() {
        return locationRequestId;
    }

    public LongFilter locationRequestId() {
        if (locationRequestId == null) {
            locationRequestId = new LongFilter();
        }
        return locationRequestId;
    }

    public void setLocationRequestId(LongFilter locationRequestId) {
        this.locationRequestId = locationRequestId;
    }

    public LongFilter getMileageRequestId() {
        return mileageRequestId;
    }

    public LongFilter mileageRequestId() {
        if (mileageRequestId == null) {
            mileageRequestId = new LongFilter();
        }
        return mileageRequestId;
    }

    public void setMileageRequestId(LongFilter mileageRequestId) {
        this.mileageRequestId = mileageRequestId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TankCriteria that = (TankCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(tankNumber, that.tankNumber) &&
            Objects.equals(ownerName, that.ownerName) &&
            Objects.equals(clientName, that.clientName) &&
            Objects.equals(locationRequestId, that.locationRequestId) &&
            Objects.equals(mileageRequestId, that.mileageRequestId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tankNumber, ownerName, clientName, locationRequestId, mileageRequestId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TankCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (tankNumber != null ? "tankNumber=" + tankNumber + ", " : "") +
            (ownerName != null ? "ownerName=" + ownerName + ", " : "") +
            (clientName != null ? "clientName=" + clientName + ", " : "") +
            (locationRequestId != null ? "locationRequestId=" + locationRequestId + ", " : "") +
            (mileageRequestId != null ? "mileageRequestId=" + mileageRequestId + ", " : "") +
            "}";
    }
}
