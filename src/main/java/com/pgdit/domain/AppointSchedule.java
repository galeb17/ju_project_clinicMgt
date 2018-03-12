package com.pgdit.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A AppointSchedule.
 */
@Entity
@Table(name = "appoint_schedule")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppointSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "schedule_date")
    private Instant scheduleDate;

    @Column(name = "schedule_time")
    private Instant scheduleTime;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private Doctor doctor;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getScheduleDate() {
        return scheduleDate;
    }

    public AppointSchedule scheduleDate(Instant scheduleDate) {
        this.scheduleDate = scheduleDate;
        return this;
    }

    public void setScheduleDate(Instant scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Instant getScheduleTime() {
        return scheduleTime;
    }

    public AppointSchedule scheduleTime(Instant scheduleTime) {
        this.scheduleTime = scheduleTime;
        return this;
    }

    public void setScheduleTime(Instant scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public Patient getPatient() {
        return patient;
    }

    public AppointSchedule patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public AppointSchedule doctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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
        AppointSchedule appointSchedule = (AppointSchedule) o;
        if (appointSchedule.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appointSchedule.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AppointSchedule{" +
            "id=" + getId() +
            ", scheduleDate='" + getScheduleDate() + "'" +
            ", scheduleTime='" + getScheduleTime() + "'" +
            "}";
    }
}
