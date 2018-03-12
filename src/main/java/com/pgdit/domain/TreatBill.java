package com.pgdit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A TreatBill.
 */
@Entity
@Table(name = "treat_bill")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TreatBill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "charge", nullable = false)
    private Double charge;

    @NotNull
    @Column(name = "received", nullable = false)
    private Double received;

    @NotNull
    @Column(name = "balance", nullable = false)
    private Double balance;

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

    public Double getCharge() {
        return charge;
    }

    public TreatBill charge(Double charge) {
        this.charge = charge;
        return this;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public Double getReceived() {
        return received;
    }

    public TreatBill received(Double received) {
        this.received = received;
        return this;
    }

    public void setReceived(Double received) {
        this.received = received;
    }

    public Double getBalance() {
        return balance;
    }

    public TreatBill balance(Double balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDate getDate() {
        return date;
    }

    public TreatBill date(LocalDate date) {
        this.date = date;
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Patient getPatient() {
        return patient;
    }

    public TreatBill patient(Patient patient) {
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
        TreatBill treatBill = (TreatBill) o;
        if (treatBill.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), treatBill.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TreatBill{" +
            "id=" + getId() +
            ", charge='" + getCharge() + "'" +
            ", received='" + getReceived() + "'" +
            ", balance='" + getBalance() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
