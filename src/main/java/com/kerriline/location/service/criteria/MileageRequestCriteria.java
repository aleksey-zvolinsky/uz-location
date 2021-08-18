package com.kerriline.location.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.kerriline.location.domain.MileageRequest} entity. This class is used
 * in {@link com.kerriline.location.web.rest.MileageRequestResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mileage-requests?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MileageRequestCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter requestDatetime;

    private StringFilter tankNumbers;

    private LongFilter mileageResponseId;

    private LongFilter tankId;

    public MileageRequestCriteria() {}

    public MileageRequestCriteria(MileageRequestCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.requestDatetime = other.requestDatetime == null ? null : other.requestDatetime.copy();
        this.tankNumbers = other.tankNumbers == null ? null : other.tankNumbers.copy();
        this.mileageResponseId = other.mileageResponseId == null ? null : other.mileageResponseId.copy();
        this.tankId = other.tankId == null ? null : other.tankId.copy();
    }

    @Override
    public MileageRequestCriteria copy() {
        return new MileageRequestCriteria(this);
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

    public LocalDateFilter getRequestDatetime() {
        return requestDatetime;
    }

    public LocalDateFilter requestDatetime() {
        if (requestDatetime == null) {
            requestDatetime = new LocalDateFilter();
        }
        return requestDatetime;
    }

    public void setRequestDatetime(LocalDateFilter requestDatetime) {
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

    public LongFilter getMileageResponseId() {
        return mileageResponseId;
    }

    public LongFilter mileageResponseId() {
        if (mileageResponseId == null) {
            mileageResponseId = new LongFilter();
        }
        return mileageResponseId;
    }

    public void setMileageResponseId(LongFilter mileageResponseId) {
        this.mileageResponseId = mileageResponseId;
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
        final MileageRequestCriteria that = (MileageRequestCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(requestDatetime, that.requestDatetime) &&
            Objects.equals(tankNumbers, that.tankNumbers) &&
            Objects.equals(mileageResponseId, that.mileageResponseId) &&
            Objects.equals(tankId, that.tankId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestDatetime, tankNumbers, mileageResponseId, tankId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MileageRequestCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (requestDatetime != null ? "requestDatetime=" + requestDatetime + ", " : "") +
            (tankNumbers != null ? "tankNumbers=" + tankNumbers + ", " : "") +
            (mileageResponseId != null ? "mileageResponseId=" + mileageResponseId + ", " : "") +
            (tankId != null ? "tankId=" + tankId + ", " : "") +
            "}";
    }
}
