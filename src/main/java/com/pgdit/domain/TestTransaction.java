package com.pgdit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A TestTransaction.
 */
@Entity
@Table(name = "test_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TestTransaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "treatment")
    private String treatment ;

    @Column(name = "cause")
    private String cause;

    @Column(name = "charge")
    private Double charge;

    @Column(name = "received")
    private Double received;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "ref_doctor")
    private String refDoctor;

    @Column(name = "treat_no")
    private Integer treatNo;

    @Column(name = "test_report")
    private String testReport;

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

    public String getTreatment () {
        return treatment ;
    }

    public TestTransaction treatment (String treatment ) {
        this.treatment  = treatment ;
        return this;
    }

    public void setTreatment (String treatment ) {
        this.treatment  = treatment ;
    }

    public String getCause() {
        return cause;
    }

    public TestTransaction cause(String cause) {
        this.cause = cause;
        return this;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Double getCharge() {
        return charge;
    }

    public TestTransaction charge(Double charge) {
        this.charge = charge;
        return this;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public Double getReceived() {
        return received;
    }

    public TestTransaction received(Double received) {
        this.received = received;
        return this;
    }

    public void setReceived(Double received) {
        this.received = received;
    }

    public Double getBalance() {
        return balance;
    }

    public TestTransaction balance(Double balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getRefDoctor() {
        return refDoctor;
    }

    public TestTransaction refDoctor(String refDoctor) {
        this.refDoctor = refDoctor;
        return this;
    }

    public void setRefDoctor(String refDoctor) {
        this.refDoctor = refDoctor;
    }

    public Integer getTreatNo() {
        return treatNo;
    }

    public TestTransaction treatNo(Integer treatNo) {
        this.treatNo = treatNo;
        return this;
    }

    public void setTreatNo(Integer treatNo) {
        this.treatNo = treatNo;
    }

    public String getTestReport() {
        return testReport;
    }

    public TestTransaction testReport(String testReport) {
        this.testReport = testReport;
        return this;
    }

    public void setTestReport(String testReport) {
        this.testReport = testReport;
    }

    public LocalDate getDate() {
        return date;
    }

    public TestTransaction date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public TestTransaction patient(Patient patient) {
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
        TestTransaction testTransaction = (TestTransaction) o;
        if (testTransaction.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), testTransaction.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TestTransaction{" +
            "id=" + getId() +
            ", treatment ='" + getTreatment () + "'" +
            ", cause='" + getCause() + "'" +
            ", charge='" + getCharge() + "'" +
            ", received='" + getReceived() + "'" +
            ", balance='" + getBalance() + "'" +
            ", refDoctor='" + getRefDoctor() + "'" +
            ", treatNo='" + getTreatNo() + "'" +
            ", testReport='" + getTestReport() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
