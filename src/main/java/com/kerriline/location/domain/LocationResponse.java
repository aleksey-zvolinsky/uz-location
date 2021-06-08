package com.kerriline.location.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LocationResponse.
 */
@Entity
@Table(name = "location_response")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LocationResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "response_datetime")
    private LocalDate responseDatetime;

    @Column(name = "tank_number")
    private String tankNumber;

    @Column(name = "tank_type")
    private String tankType;

    @Column(name = "cargo_id")
    private String cargoId;

    @Column(name = "cargo_name")
    private String cargoName;

    @Column(name = "weight")
    private String weight;

    @Column(name = "receiver_id")
    private String receiverId;

    @Column(name = "tank_index")
    private String tankIndex;

    @Column(name = "location_station_id")
    private String locationStationId;

    @Column(name = "location_station_name")
    private String locationStationName;

    @Column(name = "location_datetime")
    private String locationDatetime;

    @Column(name = "location_operation")
    private String locationOperation;

    @Column(name = "state_from_station_id")
    private String stateFromStationId;

    @Column(name = "state_from_station_name")
    private String stateFromStationName;

    @Column(name = "state_to_station_id")
    private String stateToStationId;

    @Column(name = "state_to_station_name")
    private String stateToStationName;

    @Column(name = "state_send_datetime")
    private String stateSendDatetime;

    @Column(name = "state_sender_id")
    private String stateSenderId;

    @Column(name = "planed_service_datetime")
    private String planedServiceDatetime;

    @Column(name = "tank_owner")
    private String tankOwner;

    @Column(name = "tank_model")
    private String tankModel;

    @Column(name = "defect_region")
    private String defectRegion;

    @Column(name = "defect_station")
    private String defectStation;

    @Column(name = "defect_datetime")
    private String defectDatetime;

    @Column(name = "defect_details")
    private String defectDetails;

    @Column(name = "repair_region")
    private String repairRegion;

    @Column(name = "repair_station")
    private String repairStation;

    @Column(name = "repair_datetime")
    private String repairDatetime;

    @Column(name = "update_datetime")
    private String updateDatetime;

    @JsonIgnoreProperties(value = { "locationResponse", "tank" }, allowSetters = true)
    @OneToOne(mappedBy = "locationResponse")
    private LocationRequest locationRequest;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationResponse id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getResponseDatetime() {
        return this.responseDatetime;
    }

    public LocationResponse responseDatetime(LocalDate responseDatetime) {
        this.responseDatetime = responseDatetime;
        return this;
    }

    public void setResponseDatetime(LocalDate responseDatetime) {
        this.responseDatetime = responseDatetime;
    }

    public String getTankNumber() {
        return this.tankNumber;
    }

    public LocationResponse tankNumber(String tankNumber) {
        this.tankNumber = tankNumber;
        return this;
    }

    public void setTankNumber(String tankNumber) {
        this.tankNumber = tankNumber;
    }

    public String getTankType() {
        return this.tankType;
    }

    public LocationResponse tankType(String tankType) {
        this.tankType = tankType;
        return this;
    }

    public void setTankType(String tankType) {
        this.tankType = tankType;
    }

    public String getCargoId() {
        return this.cargoId;
    }

    public LocationResponse cargoId(String cargoId) {
        this.cargoId = cargoId;
        return this;
    }

    public void setCargoId(String cargoId) {
        this.cargoId = cargoId;
    }

    public String getCargoName() {
        return this.cargoName;
    }

    public LocationResponse cargoName(String cargoName) {
        this.cargoName = cargoName;
        return this;
    }

    public void setCargoName(String cargoName) {
        this.cargoName = cargoName;
    }

    public String getWeight() {
        return this.weight;
    }

    public LocationResponse weight(String weight) {
        this.weight = weight;
        return this;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getReceiverId() {
        return this.receiverId;
    }

    public LocationResponse receiverId(String receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTankIndex() {
        return this.tankIndex;
    }

    public LocationResponse tankIndex(String tankIndex) {
        this.tankIndex = tankIndex;
        return this;
    }

    public void setTankIndex(String tankIndex) {
        this.tankIndex = tankIndex;
    }

    public String getLocationStationId() {
        return this.locationStationId;
    }

    public LocationResponse locationStationId(String locationStationId) {
        this.locationStationId = locationStationId;
        return this;
    }

    public void setLocationStationId(String locationStationId) {
        this.locationStationId = locationStationId;
    }

    public String getLocationStationName() {
        return this.locationStationName;
    }

    public LocationResponse locationStationName(String locationStationName) {
        this.locationStationName = locationStationName;
        return this;
    }

    public void setLocationStationName(String locationStationName) {
        this.locationStationName = locationStationName;
    }

    public String getLocationDatetime() {
        return this.locationDatetime;
    }

    public LocationResponse locationDatetime(String locationDatetime) {
        this.locationDatetime = locationDatetime;
        return this;
    }

    public void setLocationDatetime(String locationDatetime) {
        this.locationDatetime = locationDatetime;
    }

    public String getLocationOperation() {
        return this.locationOperation;
    }

    public LocationResponse locationOperation(String locationOperation) {
        this.locationOperation = locationOperation;
        return this;
    }

    public void setLocationOperation(String locationOperation) {
        this.locationOperation = locationOperation;
    }

    public String getStateFromStationId() {
        return this.stateFromStationId;
    }

    public LocationResponse stateFromStationId(String stateFromStationId) {
        this.stateFromStationId = stateFromStationId;
        return this;
    }

    public void setStateFromStationId(String stateFromStationId) {
        this.stateFromStationId = stateFromStationId;
    }

    public String getStateFromStationName() {
        return this.stateFromStationName;
    }

    public LocationResponse stateFromStationName(String stateFromStationName) {
        this.stateFromStationName = stateFromStationName;
        return this;
    }

    public void setStateFromStationName(String stateFromStationName) {
        this.stateFromStationName = stateFromStationName;
    }

    public String getStateToStationId() {
        return this.stateToStationId;
    }

    public LocationResponse stateToStationId(String stateToStationId) {
        this.stateToStationId = stateToStationId;
        return this;
    }

    public void setStateToStationId(String stateToStationId) {
        this.stateToStationId = stateToStationId;
    }

    public String getStateToStationName() {
        return this.stateToStationName;
    }

    public LocationResponse stateToStationName(String stateToStationName) {
        this.stateToStationName = stateToStationName;
        return this;
    }

    public void setStateToStationName(String stateToStationName) {
        this.stateToStationName = stateToStationName;
    }

    public String getStateSendDatetime() {
        return this.stateSendDatetime;
    }

    public LocationResponse stateSendDatetime(String stateSendDatetime) {
        this.stateSendDatetime = stateSendDatetime;
        return this;
    }

    public void setStateSendDatetime(String stateSendDatetime) {
        this.stateSendDatetime = stateSendDatetime;
    }

    public String getStateSenderId() {
        return this.stateSenderId;
    }

    public LocationResponse stateSenderId(String stateSenderId) {
        this.stateSenderId = stateSenderId;
        return this;
    }

    public void setStateSenderId(String stateSenderId) {
        this.stateSenderId = stateSenderId;
    }

    public String getPlanedServiceDatetime() {
        return this.planedServiceDatetime;
    }

    public LocationResponse planedServiceDatetime(String planedServiceDatetime) {
        this.planedServiceDatetime = planedServiceDatetime;
        return this;
    }

    public void setPlanedServiceDatetime(String planedServiceDatetime) {
        this.planedServiceDatetime = planedServiceDatetime;
    }

    public String getTankOwner() {
        return this.tankOwner;
    }

    public LocationResponse tankOwner(String tankOwner) {
        this.tankOwner = tankOwner;
        return this;
    }

    public void setTankOwner(String tankOwner) {
        this.tankOwner = tankOwner;
    }

    public String getTankModel() {
        return this.tankModel;
    }

    public LocationResponse tankModel(String tankModel) {
        this.tankModel = tankModel;
        return this;
    }

    public void setTankModel(String tankModel) {
        this.tankModel = tankModel;
    }

    public String getDefectRegion() {
        return this.defectRegion;
    }

    public LocationResponse defectRegion(String defectRegion) {
        this.defectRegion = defectRegion;
        return this;
    }

    public void setDefectRegion(String defectRegion) {
        this.defectRegion = defectRegion;
    }

    public String getDefectStation() {
        return this.defectStation;
    }

    public LocationResponse defectStation(String defectStation) {
        this.defectStation = defectStation;
        return this;
    }

    public void setDefectStation(String defectStation) {
        this.defectStation = defectStation;
    }

    public String getDefectDatetime() {
        return this.defectDatetime;
    }

    public LocationResponse defectDatetime(String defectDatetime) {
        this.defectDatetime = defectDatetime;
        return this;
    }

    public void setDefectDatetime(String defectDatetime) {
        this.defectDatetime = defectDatetime;
    }

    public String getDefectDetails() {
        return this.defectDetails;
    }

    public LocationResponse defectDetails(String defectDetails) {
        this.defectDetails = defectDetails;
        return this;
    }

    public void setDefectDetails(String defectDetails) {
        this.defectDetails = defectDetails;
    }

    public String getRepairRegion() {
        return this.repairRegion;
    }

    public LocationResponse repairRegion(String repairRegion) {
        this.repairRegion = repairRegion;
        return this;
    }

    public void setRepairRegion(String repairRegion) {
        this.repairRegion = repairRegion;
    }

    public String getRepairStation() {
        return this.repairStation;
    }

    public LocationResponse repairStation(String repairStation) {
        this.repairStation = repairStation;
        return this;
    }

    public void setRepairStation(String repairStation) {
        this.repairStation = repairStation;
    }

    public String getRepairDatetime() {
        return this.repairDatetime;
    }

    public LocationResponse repairDatetime(String repairDatetime) {
        this.repairDatetime = repairDatetime;
        return this;
    }

    public void setRepairDatetime(String repairDatetime) {
        this.repairDatetime = repairDatetime;
    }

    public String getUpdateDatetime() {
        return this.updateDatetime;
    }

    public LocationResponse updateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
        return this;
    }

    public void setUpdateDatetime(String updateDatetime) {
        this.updateDatetime = updateDatetime;
    }

    public LocationRequest getLocationRequest() {
        return this.locationRequest;
    }

    public LocationResponse locationRequest(LocationRequest locationRequest) {
        this.setLocationRequest(locationRequest);
        return this;
    }

    public void setLocationRequest(LocationRequest locationRequest) {
        if (this.locationRequest != null) {
            this.locationRequest.setLocationResponse(null);
        }
        if (locationRequest != null) {
            locationRequest.setLocationResponse(this);
        }
        this.locationRequest = locationRequest;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationResponse)) {
            return false;
        }
        return id != null && id.equals(((LocationResponse) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationResponse{" +
            "id=" + getId() +
            ", responseDatetime='" + getResponseDatetime() + "'" +
            ", tankNumber='" + getTankNumber() + "'" +
            ", tankType='" + getTankType() + "'" +
            ", cargoId='" + getCargoId() + "'" +
            ", cargoName='" + getCargoName() + "'" +
            ", weight='" + getWeight() + "'" +
            ", receiverId='" + getReceiverId() + "'" +
            ", tankIndex='" + getTankIndex() + "'" +
            ", locationStationId='" + getLocationStationId() + "'" +
            ", locationStationName='" + getLocationStationName() + "'" +
            ", locationDatetime='" + getLocationDatetime() + "'" +
            ", locationOperation='" + getLocationOperation() + "'" +
            ", stateFromStationId='" + getStateFromStationId() + "'" +
            ", stateFromStationName='" + getStateFromStationName() + "'" +
            ", stateToStationId='" + getStateToStationId() + "'" +
            ", stateToStationName='" + getStateToStationName() + "'" +
            ", stateSendDatetime='" + getStateSendDatetime() + "'" +
            ", stateSenderId='" + getStateSenderId() + "'" +
            ", planedServiceDatetime='" + getPlanedServiceDatetime() + "'" +
            ", tankOwner='" + getTankOwner() + "'" +
            ", tankModel='" + getTankModel() + "'" +
            ", defectRegion='" + getDefectRegion() + "'" +
            ", defectStation='" + getDefectStation() + "'" +
            ", defectDatetime='" + getDefectDatetime() + "'" +
            ", defectDetails='" + getDefectDetails() + "'" +
            ", repairRegion='" + getRepairRegion() + "'" +
            ", repairStation='" + getRepairStation() + "'" +
            ", repairDatetime='" + getRepairDatetime() + "'" +
            ", updateDatetime='" + getUpdateDatetime() + "'" +
            "}";
    }
}
