package com.pgdit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.pgdit.domain.enumeration.Gender;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "bed_code")
    private String bed_Code;

    @Column(name = "bed_no")
    private String bed_No;

    @Column(name = "entry_type")
    private String entry_Type;

    @Column(name = "discharge_status")
    private String discharge_Status;

    @Column(name = "discharge_date")
    private Instant discharge_Date;

    @Column(name = "admission_date")
    private Instant admission_Date;

    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "age")
    private Double age;

    @Column(name = "contact_no")
    private String contactNo;

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

    public Patient name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBed_Code() {
        return bed_Code;
    }

    public Patient bed_Code(String bed_Code) {
        this.bed_Code = bed_Code;
        return this;
    }

    public void setBed_Code(String bed_Code) {
        this.bed_Code = bed_Code;
    }

    public String getBed_No() {
        return bed_No;
    }

    public Patient bed_No(String bed_No) {
        this.bed_No = bed_No;
        return this;
    }

    public void setBed_No(String bed_No) {
        this.bed_No = bed_No;
    }

    public String getEntry_Type() {
        return entry_Type;
    }

    public Patient entry_Type(String entry_Type) {
        this.entry_Type = entry_Type;
        return this;
    }

    public void setEntry_Type(String entry_Type) {
        this.entry_Type = entry_Type;
    }

    public String getDischarge_Status() {
        return discharge_Status;
    }

    public Patient discharge_Status(String discharge_Status) {
        this.discharge_Status = discharge_Status;
        return this;
    }

    public void setDischarge_Status(String discharge_Status) {
        this.discharge_Status = discharge_Status;
    }

    public Instant getDischarge_Date() {
        return discharge_Date;
    }

    public Patient discharge_Date(Instant discharge_Date) {
        this.discharge_Date = discharge_Date;
        return this;
    }

    public void setDischarge_Date(Instant discharge_Date) {
        this.discharge_Date = discharge_Date;
    }

    public Instant getAdmission_Date() {
        return admission_Date;
    }

    public Patient admission_Date(Instant admission_Date) {
        this.admission_Date = admission_Date;
        return this;
    }

    public void setAdmission_Date(Instant admission_Date) {
        this.admission_Date = admission_Date;
    }

    public String getAddress() {
        return address;
    }

    public Patient address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public Patient gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Double getAge() {
        return age;
    }

    public Patient age(Double age) {
        this.age = age;
        return this;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public String getContactNo() {
        return contactNo;
    }

    public Patient contactNo(String contactNo) {
        this.contactNo = contactNo;
        return this;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
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
        Patient patient = (Patient) o;
        if (patient.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), patient.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", bed_Code='" + getBed_Code() + "'" +
            ", bed_No='" + getBed_No() + "'" +
            ", entry_Type='" + getEntry_Type() + "'" +
            ", discharge_Status='" + getDischarge_Status() + "'" +
            ", discharge_Date='" + getDischarge_Date() + "'" +
            ", admission_Date='" + getAdmission_Date() + "'" +
            ", address='" + getAddress() + "'" +
            ", gender='" + getGender() + "'" +
            ", age='" + getAge() + "'" +
            ", contactNo='" + getContactNo() + "'" +
            "}";
    }
}
