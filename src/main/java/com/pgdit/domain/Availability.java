package com.pgdit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import com.pgdit.domain.enumeration.Gender;

/**
 * A Availability.
 */
@Entity
@Table(name = "availability")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Availability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wards")
    private String wards;

    @Column(name = "bed_code")
    private String bedCode;

    @Column(name = "bed_no")
    private String bedNo;

    @Column(name = "reserved")
    private Boolean reserved;

    @Column(name = "charge")
    private Double Charge;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWards() {
        return wards;
    }

    public Availability wards(String wards) {
        this.wards = wards;
        return this;
    }

    public void setWards(String wards) {
        this.wards = wards;
    }

    public String getBedCode() {
        return bedCode;
    }

    public Availability bedCode(String bedCode) {
        this.bedCode = bedCode;
        return this;
    }

    public void setBedCode(String bedCode) {
        this.bedCode = bedCode;
    }

    public String getBedNo() {
        return bedNo;
    }

    public Availability bedNo(String bedNo) {
        this.bedNo = bedNo;
        return this;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public Boolean isReserved() {
        return reserved;
    }

    public Availability reserved(Boolean reserved) {
        this.reserved = reserved;
        return this;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public Double getCharge() {
        return Charge;
    }

    public Availability Charge(Double Charge) {
        this.Charge = Charge;
        return this;
    }

    public void setCharge(Double Charge) {
        this.Charge = Charge;
    }

    public Gender getGender() {
        return gender;
    }

    public Availability gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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
        Availability availability = (Availability) o;
        if (availability.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), availability.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Availability{" +
            "id=" + getId() +
            ", wards='" + getWards() + "'" +
            ", bedCode='" + getBedCode() + "'" +
            ", bedNo='" + getBedNo() + "'" +
            ", reserved='" + isReserved() + "'" +
            ", Charge='" + getCharge() + "'" +
            ", gender='" + getGender() + "'" +
            "}";
    }
}
