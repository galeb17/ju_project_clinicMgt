package com.pgdit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A TestDescription.
 */
@Entity
@Table(name = "test_description")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TestDescription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department")
    private String department;

    @Column(name = "treatment")
    private String treatment;

    @Column(name = "charge")
    private Double charge;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public TestDescription department(String department) {
        this.department = department;
        return this;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTreatment() {
        return treatment;
    }

    public TestDescription treatment(String treatment) {
        this.treatment = treatment;
        return this;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Double getCharge() {
        return charge;
    }

    public TestDescription charge(Double charge) {
        this.charge = charge;
        return this;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
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
        TestDescription testDescription = (TestDescription) o;
        if (testDescription.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testDescription.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestDescription{" +
            "id=" + getId() +
            ", department='" + getDepartment() + "'" +
            ", treatment='" + getTreatment() + "'" +
            ", charge='" + getCharge() + "'" +
            "}";
    }
}
