package com.pgdit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A FinalBill.
 */
@Entity
@Table(name = "final_bill")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FinalBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "treatment_charge")
    private Double treatmentCharge;

    @Column(name = "treatment_received")
    private String treatmentReceived;

    @Column(name = "treatment_balance")
    private Double treatmentBalance;

    @Column(name = "bed_charge")
    private Double bedCharge;

    @Column(name = "nursing_charge")
    private Double nursingCharge;

    @Column(name = "medicine_charge")
    private Double medicineCharge;

    @Column(name = "doctor_visit")
    private String doctorVisit;

    @Column(name = "operation")
    private String operation;

    @Column(name = "jhi_date")
    private Instant Date;

    @ManyToOne
    private Patient patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public FinalBill billNo(String billNo) {
        this.billNo = billNo;
        return this;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Double getTreatmentCharge() {
        return treatmentCharge;
    }

    public FinalBill treatmentCharge(Double treatmentCharge) {
        this.treatmentCharge = treatmentCharge;
        return this;
    }

    public void setTreatmentCharge(Double treatmentCharge) {
        this.treatmentCharge = treatmentCharge;
    }

    public String getTreatmentReceived() {
        return treatmentReceived;
    }

    public FinalBill treatmentReceived(String treatmentReceived) {
        this.treatmentReceived = treatmentReceived;
        return this;
    }

    public void setTreatmentReceived(String treatmentReceived) {
        this.treatmentReceived = treatmentReceived;
    }

    public Double getTreatmentBalance() {
        return treatmentBalance;
    }

    public FinalBill treatmentBalance(Double treatmentBalance) {
        this.treatmentBalance = treatmentBalance;
        return this;
    }

    public void setTreatmentBalance(Double treatmentBalance) {
        this.treatmentBalance = treatmentBalance;
    }

    public Double getBedCharge() {
        return bedCharge;
    }

    public FinalBill bedCharge(Double bedCharge) {
        this.bedCharge = bedCharge;
        return this;
    }

    public void setBedCharge(Double bedCharge) {
        this.bedCharge = bedCharge;
    }

    public Double getNursingCharge() {
        return nursingCharge;
    }

    public FinalBill nursingCharge(Double nursingCharge) {
        this.nursingCharge = nursingCharge;
        return this;
    }

    public void setNursingCharge(Double nursingCharge) {
        this.nursingCharge = nursingCharge;
    }

    public Double getMedicineCharge() {
        return medicineCharge;
    }

    public FinalBill medicineCharge(Double medicineCharge) {
        this.medicineCharge = medicineCharge;
        return this;
    }

    public void setMedicineCharge(Double medicineCharge) {
        this.medicineCharge = medicineCharge;
    }

    public String getDoctorVisit() {
        return doctorVisit;
    }

    public FinalBill doctorVisit(String doctorVisit) {
        this.doctorVisit = doctorVisit;
        return this;
    }

    public void setDoctorVisit(String doctorVisit) {
        this.doctorVisit = doctorVisit;
    }

    public String getOperation() {
        return operation;
    }

    public FinalBill operation(String operation) {
        this.operation = operation;
        return this;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Instant getDate() {
        return Date;
    }

    public FinalBill Date(Instant Date) {
        this.Date = Date;
        return this;
    }

    public void setDate(Instant Date) {
        this.Date = Date;
    }

    public Patient getPatient() {
        return patient;
    }

    public FinalBill patient(Patient patient) {
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
        FinalBill finalBill = (FinalBill) o;
        if (finalBill.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), finalBill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FinalBill{" +
            "id=" + getId() +
            ", billNo='" + getBillNo() + "'" +
            ", treatmentCharge='" + getTreatmentCharge() + "'" +
            ", treatmentReceived='" + getTreatmentReceived() + "'" +
            ", treatmentBalance='" + getTreatmentBalance() + "'" +
            ", bedCharge='" + getBedCharge() + "'" +
            ", nursingCharge='" + getNursingCharge() + "'" +
            ", medicineCharge='" + getMedicineCharge() + "'" +
            ", doctorVisit='" + getDoctorVisit() + "'" +
            ", operation='" + getOperation() + "'" +
            ", Date='" + getDate() + "'" +
            "}";
    }
}
