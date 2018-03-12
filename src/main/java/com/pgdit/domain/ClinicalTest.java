package com.pgdit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ClinicalTest.
 */
@Entity
@Table(name = "clinical_test")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClinicalTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name ;

    @Column(name = "test_type")
    private String testType;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "pre_requisite")
    private String preRequisite;

    @Column(name = "caution")
    private String caution;

    @Column(name = "is_active")
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName () {
        return name ;
    }

    public ClinicalTest name (String name ) {
        this.name  = name ;
        return this;
    }

    public void setName (String name ) {
        this.name  = name ;
    }

    public String getTestType() {
        return testType;
    }

    public ClinicalTest testType(String testType) {
        this.testType = testType;
        return this;
    }

    public void setTestType(String testType) {
        this.testType = testType;
    }

    public Double getRate() {
        return rate;
    }

    public ClinicalTest rate(Double rate) {
        this.rate = rate;
        return this;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getPreRequisite() {
        return preRequisite;
    }

    public ClinicalTest preRequisite(String preRequisite) {
        this.preRequisite = preRequisite;
        return this;
    }

    public void setPreRequisite(String preRequisite) {
        this.preRequisite = preRequisite;
    }

    public String getCaution() {
        return caution;
    }

    public ClinicalTest caution(String caution) {
        this.caution = caution;
        return this;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public ClinicalTest isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
        ClinicalTest clinicalTest = (ClinicalTest) o;
        if (clinicalTest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clinicalTest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClinicalTest{" +
            "id=" + getId() +
            ", name ='" + getName () + "'" +
            ", testType='" + getTestType() + "'" +
            ", rate='" + getRate() + "'" +
            ", preRequisite='" + getPreRequisite() + "'" +
            ", caution='" + getCaution() + "'" +
            ", isActive='" + isIsActive() + "'" +
            "}";
    }
}
