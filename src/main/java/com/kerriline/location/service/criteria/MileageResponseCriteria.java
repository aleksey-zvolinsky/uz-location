package com.kerriline.location.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.kerriline.location.domain.MileageResponse} entity. This class is used
 * in {@link com.kerriline.location.web.rest.MileageResponseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mileage-responses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MileageResponseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter responseDatetime;

    private StringFilter tankNumber;

    private StringFilter mileageCurrent;

    private StringFilter mileageDatetime;

    private StringFilter mileageRemain;

    private StringFilter mileageUpdateDatetime;

    private LongFilter mileageRequestId;

    public MileageResponseCriteria() {}

    public MileageResponseCriteria(MileageResponseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.responseDatetime = other.responseDatetime == null ? null : other.responseDatetime.copy();
        this.tankNumber = other.tankNumber == null ? null : other.tankNumber.copy();
        this.mileageCurrent = other.mileageCurrent == null ? null : other.mileageCurrent.copy();
        this.mileageDatetime = other.mileageDatetime == null ? null : other.mileageDatetime.copy();
        this.mileageRemain = other.mileageRemain == null ? null : other.mileageRemain.copy();
        this.mileageUpdateDatetime = other.mileageUpdateDatetime == null ? null : other.mileageUpdateDatetime.copy();
        this.mileageRequestId = other.mileageRequestId == null ? null : other.mileageRequestId.copy();
    }

    @Override
    public MileageResponseCriteria copy() {
        return new MileageResponseCriteria(this);
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

    public ZonedDateTimeFilter getResponseDatetime() {
        return responseDatetime;
    }

    public ZonedDateTimeFilter responseDatetime() {
        if (responseDatetime == null) {
            responseDatetime = new ZonedDateTimeFilter();
        }
        return responseDatetime;
    }

    public void setResponseDatetime(ZonedDateTimeFilter responseDatetime) {
        this.responseDatetime = responseDatetime;
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

    public StringFilter getMileageCurrent() {
        return mileageCurrent;
    }

    public StringFilter mileageCurrent() {
        if (mileageCurrent == null) {
            mileageCurrent = new StringFilter();
        }
        return mileageCurrent;
    }

    public void setMileageCurrent(StringFilter mileageCurrent) {
        this.mileageCurrent = mileageCurrent;
    }

    public StringFilter getMileageDatetime() {
        return mileageDatetime;
    }

    public StringFilter mileageDatetime() {
        if (mileageDatetime == null) {
            mileageDatetime = new StringFilter();
        }
        return mileageDatetime;
    }

    public void setMileageDatetime(StringFilter mileageDatetime) {
        this.mileageDatetime = mileageDatetime;
    }

    public StringFilter getMileageRemain() {
        return mileageRemain;
    }

    public StringFilter mileageRemain() {
        if (mileageRemain == null) {
            mileageRemain = new StringFilter();
        }
        return mileageRemain;
    }

    public void setMileageRemain(StringFilter mileageRemain) {
        this.mileageRemain = mileageRemain;
    }

    public StringFilter getMileageUpdateDatetime() {
        return mileageUpdateDatetime;
    }

    public StringFilter mileageUpdateDatetime() {
        if (mileageUpdateDatetime == null) {
            mileageUpdateDatetime = new StringFilter();
        }
        return mileageUpdateDatetime;
    }

    public void setMileageUpdateDatetime(StringFilter mileageUpdateDatetime) {
        this.mileageUpdateDatetime = mileageUpdateDatetime;
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
        final MileageResponseCriteria that = (MileageResponseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(responseDatetime, that.responseDatetime) &&
            Objects.equals(tankNumber, that.tankNumber) &&
            Objects.equals(mileageCurrent, that.mileageCurrent) &&
            Objects.equals(mileageDatetime, that.mileageDatetime) &&
            Objects.equals(mileageRemain, that.mileageRemain) &&
            Objects.equals(mileageUpdateDatetime, that.mileageUpdateDatetime) &&
            Objects.equals(mileageRequestId, that.mileageRequestId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            responseDatetime,
            tankNumber,
            mileageCurrent,
            mileageDatetime,
            mileageRemain,
            mileageUpdateDatetime,
            mileageRequestId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MileageResponseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (responseDatetime != null ? "responseDatetime=" + responseDatetime + ", " : "") +
            (tankNumber != null ? "tankNumber=" + tankNumber + ", " : "") +
            (mileageCurrent != null ? "mileageCurrent=" + mileageCurrent + ", " : "") +
            (mileageDatetime != null ? "mileageDatetime=" + mileageDatetime + ", " : "") +
            (mileageRemain != null ? "mileageRemain=" + mileageRemain + ", " : "") +
            (mileageUpdateDatetime != null ? "mileageUpdateDatetime=" + mileageUpdateDatetime + ", " : "") +
            (mileageRequestId != null ? "mileageRequestId=" + mileageRequestId + ", " : "") +
            "}";
    }
}
