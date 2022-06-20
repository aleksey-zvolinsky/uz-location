package com.kerriline.location.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.kerriline.location.domain.LocationRequest} entity. This class is used
 * in {@link com.kerriline.location.web.rest.LocationRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /location-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocationRequestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter requestDatetime;

    private StringFilter tankNumbers;

    private LongFilter locationResponseId;

    private LongFilter tankId;

    public LocationRequestCriteria() {}

    public LocationRequestCriteria(LocationRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.requestDatetime = other.requestDatetime == null ? null : other.requestDatetime.copy();
        this.tankNumbers = other.tankNumbers == null ? null : other.tankNumbers.copy();
        this.locationResponseId = other.locationResponseId == null ? null : other.locationResponseId.copy();
        this.tankId = other.tankId == null ? null : other.tankId.copy();
    }

    @Override
    public LocationRequestCriteria copy() {
        return new LocationRequestCriteria(this);
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

    public ZonedDateTimeFilter getRequestDatetime() {
        return requestDatetime;
    }

    public ZonedDateTimeFilter requestDatetime() {
        if (requestDatetime == null) {
            requestDatetime = new ZonedDateTimeFilter();
        }
        return requestDatetime;
    }

    public void setRequestDatetime(ZonedDateTimeFilter requestDatetime) {
        this.requestDatetime = requestDatetime;
    }

    public StringFilter getTankNumbers() {
        return tankNumbers;
    }

    public StringFilter tankNumbers() {
        if (tankNumbers == null) {
            tankNumbers = new StringFilter();
        }
        return tankNumbers;
    }

    public void setTankNumbers(StringFilter tankNumbers) {
        this.tankNumbers = tankNumbers;
    }

    public LongFilter getLocationResponseId() {
        return locationResponseId;
    }

    public LongFilter locationResponseId() {
        if (locationResponseId == null) {
            locationResponseId = new LongFilter();
        }
        return locationResponseId;
    }

    public void setLocationResponseId(LongFilter locationResponseId) {
        this.locationResponseId = locationResponseId;
    }

    public LongFilter getTankId() {
        return tankId;
    }

    public LongFilter tankId() {
        if (tankId == null) {
            tankId = new LongFilter();
        }
        return tankId;
    }

    public void setTankId(LongFilter tankId) {
        this.tankId = tankId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LocationRequestCriteria that = (LocationRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(requestDatetime, that.requestDatetime) &&
            Objects.equals(tankNumbers, that.tankNumbers) &&
            Objects.equals(locationResponseId, that.locationResponseId) &&
            Objects.equals(tankId, that.tankId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestDatetime, tankNumbers, locationResponseId, tankId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationRequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (requestDatetime != null ? "requestDatetime=" + requestDatetime + ", " : "") +
            (tankNumbers != null ? "tankNumbers=" + tankNumbers + ", " : "") +
            (locationResponseId != null ? "locationResponseId=" + locationResponseId + ", " : "") +
            (tankId != null ? "tankId=" + tankId + ", " : "") +
            "}";
    }
}
