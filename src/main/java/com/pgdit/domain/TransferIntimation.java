package com.pgdit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A TransferIntimation.
 */
@Entity
@Table(name = "transferInformation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TransferIntimation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref_doctor")
    private String refDoctor;

    @Column(name = "from_bed_code")
    private Integer fromBedCode;

    @Column(name = "from_bed_no")
    private Integer fromBedNo;

    @Column(name = "to_bed_code")
    private Integer toBedCode;

    @Column(name = "to_bed_no")
    private Integer toBedNo;

    @Column(name = "charge")
    private Double charge;

    @Column(name = "jhi_date")
    private LocalDate date;

    @ManyToOne
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefDoctor() {
        return refDoctor;
    }

    public TransferIntimation refDoctor(String refDoctor) {
        this.refDoctor = refDoctor;
        return this;
    }

    public void setRefDoctor(String refDoctor) {
        this.refDoctor = refDoctor;
    }

    public Integer getFromBedCode() {
        return fromBedCode;
    }

    public TransferIntimation fromBedCode(Integer fromBedCode) {
        this.fromBedCode = fromBedCode;
        return this;
    }

    public void setFromBedCode(Integer fromBedCode) {
        this.fromBedCode = fromBedCode;
    }

    public Integer getFromBedNo() {
        return fromBedNo;
    }

    public TransferIntimation fromBedNo(Integer fromBedNo) {
        this.fromBedNo = fromBedNo;
        return this;
    }

    public void setFromBedNo(Integer fromBedNo) {
        this.fromBedNo = fromBedNo;
    }

    public Integer getToBedCode() {
        return toBedCode;
    }

    public TransferIntimation toBedCode(Integer toBedCode) {
        this.toBedCode = toBedCode;
        return this;
    }

    public void setToBedCode(Integer toBedCode) {
        this.toBedCode = toBedCode;
    }

    public Integer getToBedNo() {
        return toBedNo;
    }

    public TransferIntimation toBedNo(Integer toBedNo) {
        this.toBedNo = toBedNo;
        return this;
    }

    public void setToBedNo(Integer toBedNo) {
        this.toBedNo = toBedNo;
    }

    public Double getCharge() {
        return charge;
    }

    public TransferIntimation charge(Double charge) {
        this.charge = charge;
        return this;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public LocalDate getDate() {
        return date;
    }

    public TransferIntimation date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public TransferIntimation patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
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
        TransferIntimation transferIntimation = (TransferIntimation) o;
        if (transferIntimation.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transferIntimation.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransferIntimation{" +
            "id=" + getId() +
            ", refDoctor='" + getRefDoctor() + "'" +
            ", fromBedCode='" + getFromBedCode() + "'" +
            ", fromBedNo='" + getFromBedNo() + "'" +
            ", toBedCode='" + getToBedCode() + "'" +
            ", toBedNo='" + getToBedNo() + "'" +
            ", charge='" + getCharge() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
