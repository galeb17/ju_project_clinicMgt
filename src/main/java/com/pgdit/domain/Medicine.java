package com.pgdit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Medicine.
 */
@Entity
@Table(name = "expense_edit_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Medicine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "medicine_type")
    private String medicineType;

    @Column(name = "unit_of_measurement")
    private String unitOfMeasurement;

    @Column(name = "generic")
    private String generic;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Medicine name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMedicineType() {
        return medicineType;
    }

    public Medicine medicineType(String medicineType) {
        this.medicineType = medicineType;
        return this;
    }

    public void setMedicineType(String medicineType) {
        this.medicineType = medicineType;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public Medicine unitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
        return this;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
    }

    public String getGeneric() {
        return generic;
    }

    public Medicine generic(String generic) {
        this.generic = generic;
        return this;
    }

    public void setGeneric(String generic) {
        this.generic = generic;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Medicine medicine = (Medicine) o;
        if (medicine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), medicine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Medicine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", medicineType='" + getMedicineType() + "'" +
            ", unitOfMeasurement='" + getUnitOfMeasurement() + "'" +
            ", generic='" + getGeneric() + "'" +
            "}";
    }
}
