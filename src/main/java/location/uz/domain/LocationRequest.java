package location.uz.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LocationRequest.
 */
@Entity
@Table(name = "location_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LocationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_datetime")
    private LocalDate requestDatetime;

    @Column(name = "tank_numbers")
    private String tankNumbers;

    @OneToOne
    @JoinColumn(unique = true)
    private LocationResponse tankNumber;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tankNumbers", "tankNumbers" }, allowSetters = true)
    private Tank tank;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationRequest id(Long id) {
        this.id = id;
        return this;
    }

    public LocalDate getRequestDatetime() {
        return this.requestDatetime;
    }

    public LocationRequest requestDatetime(LocalDate requestDatetime) {
        this.requestDatetime = requestDatetime;
        return this;
    }

    public void setRequestDatetime(LocalDate requestDatetime) {
        this.requestDatetime = requestDatetime;
    }

    public String getTankNumbers() {
        return this.tankNumbers;
    }

    public LocationRequest tankNumbers(String tankNumbers) {
        this.tankNumbers = tankNumbers;
        return this;
    }

    public void setTankNumbers(String tankNumbers) {
        this.tankNumbers = tankNumbers;
    }

    public LocationResponse getTankNumber() {
        return this.tankNumber;
    }

    public LocationRequest tankNumber(LocationResponse locationResponse) {
        this.setTankNumber(locationResponse);
        return this;
    }

    public void setTankNumber(LocationResponse locationResponse) {
        this.tankNumber = locationResponse;
    }

    public Tank getTank() {
        return this.tank;
    }

    public LocationRequest tank(Tank tank) {
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
        if (!(o instanceof LocationRequest)) {
            return false;
        }
        return id != null && id.equals(((LocationRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationRequest{" +
            "id=" + getId() +
            ", requestDatetime='" + getRequestDatetime() + "'" +
            ", tankNumbers='" + getTankNumbers() + "'" +
            "}";
    }
}
