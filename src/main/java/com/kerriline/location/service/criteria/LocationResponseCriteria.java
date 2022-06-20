package com.kerriline.location.service.criteria;

import java.io.Serializable;
import java.util.Objects;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.kerriline.location.domain.LocationResponse} entity. This class is used
 * in {@link com.kerriline.location.web.rest.LocationResponseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /location-responses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class LocationResponseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ZonedDateTimeFilter responseDatetime;

    private StringFilter tankNumber;

    private StringFilter tankType;

    private StringFilter cargoId;

    private StringFilter cargoName;

    private StringFilter weight;

    private StringFilter receiverId;

    private StringFilter tankIndex;

    private StringFilter locationStationId;

    private StringFilter locationStationName;

    private StringFilter locationDatetime;

    private StringFilter locationOperation;

    private StringFilter stateFromStationId;

    private StringFilter stateFromStationName;

    private StringFilter stateToStationId;

    private StringFilter stateToStationName;

    private StringFilter stateSendDatetime;

    private StringFilter stateSenderId;

    private StringFilter planedServiceDatetime;

    private StringFilter tankOwner;

    private StringFilter tankModel;

    private StringFilter defectRegion;

    private StringFilter defectStation;

    private StringFilter defectDatetime;

    private StringFilter defectDetails;

    private StringFilter repairRegion;

    private StringFilter repairStation;

    private StringFilter repairDatetime;

    private StringFilter updateDatetime;

    private LongFilter locationRequestId;

    public LocationResponseCriteria() {}

    public LocationResponseCriteria(LocationResponseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.responseDatetime = other.responseDatetime == null ? null : other.responseDatetime.copy();
        this.tankNumber = other.tankNumber == null ? null : other.tankNumber.copy();
        this.tankType = other.tankType == null ? null : other.tankType.copy();
        this.cargoId = other.cargoId == null ? null : other.cargoId.copy();
        this.cargoName = other.cargoName == null ? null : other.cargoName.copy();
        this.weight = other.weight == null ? null : other.weight.copy();
        this.receiverId = other.receiverId == null ? null : other.receiverId.copy();
        this.tankIndex = other.tankIndex == null ? null : other.tankIndex.copy();
        this.locationStationId = other.locationStationId == null ? null : other.locationStationId.copy();
        this.locationStationName = other.locationStationName == null ? null : other.locationStationName.copy();
        this.locationDatetime = other.locationDatetime == null ? null : other.locationDatetime.copy();
        this.locationOperation = other.locationOperation == null ? null : other.locationOperation.copy();
        this.stateFromStationId = other.stateFromStationId == null ? null : other.stateFromStationId.copy();
        this.stateFromStationName = other.stateFromStationName == null ? null : other.stateFromStationName.copy();
        this.stateToStationId = other.stateToStationId == null ? null : other.stateToStationId.copy();
        this.stateToStationName = other.stateToStationName == null ? null : other.stateToStationName.copy();
        this.stateSendDatetime = other.stateSendDatetime == null ? null : other.stateSendDatetime.copy();
        this.stateSenderId = other.stateSenderId == null ? null : other.stateSenderId.copy();
        this.planedServiceDatetime = other.planedServiceDatetime == null ? null : other.planedServiceDatetime.copy();
        this.tankOwner = other.tankOwner == null ? null : other.tankOwner.copy();
        this.tankModel = other.tankModel == null ? null : other.tankModel.copy();
        this.defectRegion = other.defectRegion == null ? null : other.defectRegion.copy();
        this.defectStation = other.defectStation == null ? null : other.defectStation.copy();
        this.defectDatetime = other.defectDatetime == null ? null : other.defectDatetime.copy();
        this.defectDetails = other.defectDetails == null ? null : other.defectDetails.copy();
        this.repairRegion = other.repairRegion == null ? null : other.repairRegion.copy();
        this.repairStation = other.repairStation == null ? null : other.repairStation.copy();
        this.repairDatetime = other.repairDatetime == null ? null : other.repairDatetime.copy();
        this.updateDatetime = other.updateDatetime == null ? null : other.updateDatetime.copy();
        this.locationRequestId = other.locationRequestId == null ? null : other.locationRequestId.copy();
    }

    @Override
    public LocationResponseCriteria copy() {
        return new LocationResponseCriteria(this);
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

    public StringFilter getTankType() {
        return tankType;
    }

    public StringFilter tankType() {
        if (tankType == null) {
            tankType = new StringFilter();
        }
        return tankType;
    }

    public void setTankType(StringFilter tankType) {
        this.tankType = tankType;
    }

    public StringFilter getCargoId() {
        return cargoId;
    }

    public StringFilter cargoId() {
        if (cargoId == null) {
            cargoId = new StringFilter();
        }
        return cargoId;
    }

    public void setCargoId(StringFilter cargoId) {
        this.cargoId = cargoId;
    }

    public StringFilter getCargoName() {
        return cargoName;
    }

    public StringFilter cargoName() {
        if (cargoName == null) {
            cargoName = new StringFilter();
        }
        return cargoName;
    }

    public void setCargoName(StringFilter cargoName) {
        this.cargoName = cargoName;
    }

    public StringFilter getWeight() {
        return weight;
    }

    public StringFilter weight() {
        if (weight == null) {
            weight = new StringFilter();
        }
        return weight;
    }

    public void setWeight(StringFilter weight) {
        this.weight = weight;
    }

    public StringFilter getReceiverId() {
        return receiverId;
    }

    public StringFilter receiverId() {
        if (receiverId == null) {
            receiverId = new StringFilter();
        }
        return receiverId;
    }

    public void setReceiverId(StringFilter receiverId) {
        this.receiverId = receiverId;
    }

    public StringFilter getTankIndex() {
        return tankIndex;
    }

    public StringFilter tankIndex() {
        if (tankIndex == null) {
            tankIndex = new StringFilter();
        }
        return tankIndex;
    }

    public void setTankIndex(StringFilter tankIndex) {
        this.tankIndex = tankIndex;
    }

    public StringFilter getLocationStationId() {
        return locationStationId;
    }

    public StringFilter locationStationId() {
        if (locationStationId == null) {
            locationStationId = new StringFilter();
        }
        return locationStationId;
    }

    public void setLocationStationId(StringFilter locationStationId) {
        this.locationStationId = locationStationId;
    }

    public StringFilter getLocationStationName() {
        return locationStationName;
    }

    public StringFilter locationStationName() {
        if (locationStationName == null) {
            locationStationName = new StringFilter();
        }
        return locationStationName;
    }

    public void setLocationStationName(StringFilter locationStationName) {
        this.locationStationName = locationStationName;
    }

    public StringFilter getLocationDatetime() {
        return locationDatetime;
    }

    public StringFilter locationDatetime() {
        if (locationDatetime == null) {
            locationDatetime = new StringFilter();
        }
        return locationDatetime;
    }

    public void setLocationDatetime(StringFilter locationDatetime) {
        this.locationDatetime = locationDatetime;
    }

    public StringFilter getLocationOperation() {
        return locationOperation;
    }

    public StringFilter locationOperation() {
        if (locationOperation == null) {
            locationOperation = new StringFilter();
        }
        return locationOperation;
    }

    public void setLocationOperation(StringFilter locationOperation) {
        this.locationOperation = locationOperation;
    }

    public StringFilter getStateFromStationId() {
        return stateFromStationId;
    }

    public StringFilter stateFromStationId() {
        if (stateFromStationId == null) {
            stateFromStationId = new StringFilter();
        }
        return stateFromStationId;
    }

    public void setStateFromStationId(StringFilter stateFromStationId) {
        this.stateFromStationId = stateFromStationId;
    }

    public StringFilter getStateFromStationName() {
        return stateFromStationName;
    }

    public StringFilter stateFromStationName() {
        if (stateFromStationName == null) {
            stateFromStationName = new StringFilter();
        }
        return stateFromStationName;
    }

    public void setStateFromStationName(StringFilter stateFromStationName) {
        this.stateFromStationName = stateFromStationName;
    }

    public StringFilter getStateToStationId() {
        return stateToStationId;
    }

    public StringFilter stateToStationId() {
        if (stateToStationId == null) {
            stateToStationId = new StringFilter();
        }
        return stateToStationId;
    }

    public void setStateToStationId(StringFilter stateToStationId) {
        this.stateToStationId = stateToStationId;
    }

    public StringFilter getStateToStationName() {
        return stateToStationName;
    }

    public StringFilter stateToStationName() {
        if (stateToStationName == null) {
            stateToStationName = new StringFilter();
        }
        return stateToStationName;
    }

    public void setStateToStationName(StringFilter stateToStationName) {
        this.stateToStationName = stateToStationName;
    }

    public StringFilter getStateSendDatetime() {
        return stateSendDatetime;
    }

    public StringFilter stateSendDatetime() {
        if (stateSendDatetime == null) {
            stateSendDatetime = new StringFilter();
        }
        return stateSendDatetime;
    }

    public void setStateSendDatetime(StringFilter stateSendDatetime) {
        this.stateSendDatetime = stateSendDatetime;
    }

    public StringFilter getStateSenderId() {
        return stateSenderId;
    }

    public StringFilter stateSenderId() {
        if (stateSenderId == null) {
            stateSenderId = new StringFilter();
        }
        return stateSenderId;
    }

    public void setStateSenderId(StringFilter stateSenderId) {
        this.stateSenderId = stateSenderId;
    }

    public StringFilter getPlanedServiceDatetime() {
        return planedServiceDatetime;
    }

    public StringFilter planedServiceDatetime() {
        if (planedServiceDatetime == null) {
            planedServiceDatetime = new StringFilter();
        }
        return planedServiceDatetime;
    }

    public void setPlanedServiceDatetime(StringFilter planedServiceDatetime) {
        this.planedServiceDatetime = planedServiceDatetime;
    }

    public StringFilter getTankOwner() {
        return tankOwner;
    }

    public StringFilter tankOwner() {
        if (tankOwner == null) {
            tankOwner = new StringFilter();
        }
        return tankOwner;
    }

    public void setTankOwner(StringFilter tankOwner) {
        this.tankOwner = tankOwner;
    }

    public StringFilter getTankModel() {
        return tankModel;
    }

    public StringFilter tankModel() {
        if (tankModel == null) {
            tankModel = new StringFilter();
        }
        return tankModel;
    }

    public void setTankModel(StringFilter tankModel) {
        this.tankModel = tankModel;
    }

    public StringFilter getDefectRegion() {
        return defectRegion;
    }

    public StringFilter defectRegion() {
        if (defectRegion == null) {
            defectRegion = new StringFilter();
        }
        return defectRegion;
    }

    public void setDefectRegion(StringFilter defectRegion) {
        this.defectRegion = defectRegion;
    }

    public StringFilter getDefectStation() {
        return defectStation;
    }

    public StringFilter defectStation() {
        if (defectStation == null) {
            defectStation = new StringFilter();
        }
        return defectStation;
    }

    public void setDefectStation(StringFilter defectStation) {
        this.defectStation = defectStation;
    }

    public StringFilter getDefectDatetime() {
        return defectDatetime;
    }

    public StringFilter defectDatetime() {
        if (defectDatetime == null) {
            defectDatetime = new StringFilter();
        }
        return defectDatetime;
    }

    public void setDefectDatetime(StringFilter defectDatetime) {
        this.defectDatetime = defectDatetime;
    }

    public StringFilter getDefectDetails() {
        return defectDetails;
    }

    public StringFilter defectDetails() {
        if (defectDetails == null) {
            defectDetails = new StringFilter();
        }
        return defectDetails;
    }

    public void setDefectDetails(StringFilter defectDetails) {
        this.defectDetails = defectDetails;
    }

    public StringFilter getRepairRegion() {
        return repairRegion;
    }

    public StringFilter repairRegion() {
        if (repairRegion == null) {
            repairRegion = new StringFilter();
        }
        return repairRegion;
    }

    public void setRepairRegion(StringFilter repairRegion) {
        this.repairRegion = repairRegion;
    }

    public StringFilter getRepairStation() {
        return repairStation;
    }

    public StringFilter repairStation() {
        if (repairStation == null) {
            repairStation = new StringFilter();
        }
        return repairStation;
    }

    public void setRepairStation(StringFilter repairStation) {
        this.repairStation = repairStation;
    }

    public StringFilter getRepairDatetime() {
        return repairDatetime;
    }

    public StringFilter repairDatetime() {
        if (repairDatetime == null) {
            repairDatetime = new StringFilter();
        }
        return repairDatetime;
    }

    public void setRepairDatetime(StringFilter repairDatetime) {
        this.repairDatetime = repairDatetime;
    }

    public StringFilter getUpdateDatetime() {
        return updateDatetime;
    }

    public StringFilter updateDatetime() {
        if (updateDatetime == null) {
            updateDatetime = new StringFilter();
        }
        return updateDatetime;
    }

    public void setUpdateDatetime(StringFilter updateDatetime) {
        this.updateDatetime = updateDatetime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LocationResponseCriteria that = (LocationResponseCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(responseDatetime, that.responseDatetime) &&
            Objects.equals(tankNumber, that.tankNumber) &&
            Objects.equals(tankType, that.tankType) &&
            Objects.equals(cargoId, that.cargoId) &&
            Objects.equals(cargoName, that.cargoName) &&
            Objects.equals(weight, that.weight) &&
            Objects.equals(receiverId, that.receiverId) &&
            Objects.equals(tankIndex, that.tankIndex) &&
            Objects.equals(locationStationId, that.locationStationId) &&
            Objects.equals(locationStationName, that.locationStationName) &&
            Objects.equals(locationDatetime, that.locationDatetime) &&
            Objects.equals(locationOperation, that.locationOperation) &&
            Objects.equals(stateFromStationId, that.stateFromStationId) &&
            Objects.equals(stateFromStationName, that.stateFromStationName) &&
            Objects.equals(stateToStationId, that.stateToStationId) &&
            Objects.equals(stateToStationName, that.stateToStationName) &&
            Objects.equals(stateSendDatetime, that.stateSendDatetime) &&
            Objects.equals(stateSenderId, that.stateSenderId) &&
            Objects.equals(planedServiceDatetime, that.planedServiceDatetime) &&
            Objects.equals(tankOwner, that.tankOwner) &&
            Objects.equals(tankModel, that.tankModel) &&
            Objects.equals(defectRegion, that.defectRegion) &&
            Objects.equals(defectStation, that.defectStation) &&
            Objects.equals(defectDatetime, that.defectDatetime) &&
            Objects.equals(defectDetails, that.defectDetails) &&
            Objects.equals(repairRegion, that.repairRegion) &&
            Objects.equals(repairStation, that.repairStation) &&
            Objects.equals(repairDatetime, that.repairDatetime) &&
            Objects.equals(updateDatetime, that.updateDatetime) &&
            Objects.equals(locationRequestId, that.locationRequestId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            responseDatetime,
            tankNumber,
            tankType,
            cargoId,
            cargoName,
            weight,
            receiverId,
            tankIndex,
            locationStationId,
            locationStationName,
            locationDatetime,
            locationOperation,
            stateFromStationId,
            stateFromStationName,
            stateToStationId,
            stateToStationName,
            stateSendDatetime,
            stateSenderId,
            planedServiceDatetime,
            tankOwner,
            tankModel,
            defectRegion,
            defectStation,
            defectDatetime,
            defectDetails,
            repairRegion,
            repairStation,
            repairDatetime,
            updateDatetime,
            locationRequestId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationResponseCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (responseDatetime != null ? "responseDatetime=" + responseDatetime + ", " : "") +
            (tankNumber != null ? "tankNumber=" + tankNumber + ", " : "") +
            (tankType != null ? "tankType=" + tankType + ", " : "") +
            (cargoId != null ? "cargoId=" + cargoId + ", " : "") +
            (cargoName != null ? "cargoName=" + cargoName + ", " : "") +
            (weight != null ? "weight=" + weight + ", " : "") +
            (receiverId != null ? "receiverId=" + receiverId + ", " : "") +
            (tankIndex != null ? "tankIndex=" + tankIndex + ", " : "") +
            (locationStationId != null ? "locationStationId=" + locationStationId + ", " : "") +
            (locationStationName != null ? "locationStationName=" + locationStationName + ", " : "") +
            (locationDatetime != null ? "locationDatetime=" + locationDatetime + ", " : "") +
            (locationOperation != null ? "locationOperation=" + locationOperation + ", " : "") +
            (stateFromStationId != null ? "stateFromStationId=" + stateFromStationId + ", " : "") +
            (stateFromStationName != null ? "stateFromStationName=" + stateFromStationName + ", " : "") +
            (stateToStationId != null ? "stateToStationId=" + stateToStationId + ", " : "") +
            (stateToStationName != null ? "stateToStationName=" + stateToStationName + ", " : "") +
            (stateSendDatetime != null ? "stateSendDatetime=" + stateSendDatetime + ", " : "") +
            (stateSenderId != null ? "stateSenderId=" + stateSenderId + ", " : "") +
            (planedServiceDatetime != null ? "planedServiceDatetime=" + planedServiceDatetime + ", " : "") +
            (tankOwner != null ? "tankOwner=" + tankOwner + ", " : "") +
            (tankModel != null ? "tankModel=" + tankModel + ", " : "") +
            (defectRegion != null ? "defectRegion=" + defectRegion + ", " : "") +
            (defectStation != null ? "defectStation=" + defectStation + ", " : "") +
            (defectDatetime != null ? "defectDatetime=" + defectDatetime + ", " : "") +
            (defectDetails != null ? "defectDetails=" + defectDetails + ", " : "") +
            (repairRegion != null ? "repairRegion=" + repairRegion + ", " : "") +
            (repairStation != null ? "repairStation=" + repairStation + ", " : "") +
            (repairDatetime != null ? "repairDatetime=" + repairDatetime + ", " : "") +
            (updateDatetime != null ? "updateDatetime=" + updateDatetime + ", " : "") +
            (locationRequestId != null ? "locationRequestId=" + locationRequestId + ", " : "") +
            "}";
    }
}
