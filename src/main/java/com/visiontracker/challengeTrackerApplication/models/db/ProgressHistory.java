package com.visiontracker.challengeTrackerApplication.models.db;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "progressHistory")
@XmlRootElement
public class ProgressHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long progressHistoryId;
    @Temporal(TemporalType.DATE)
    private Date dateOfRecord;
    private BigDecimal value;

    @JoinColumn(name = "programId", referencedColumnName = "programId")
    @ManyToOne(optional = false)
    private Program programId;
    @JoinColumn(name = "milestoneId", referencedColumnName = "milestoneId")
    @ManyToOne(optional = false)
    private Milestone milestoneId;

    public ProgressHistory() {

    }

    public ProgressHistory(Date date, BigDecimal value)
    {
        this.dateOfRecord = date;
        this.value = value;
    }

    public Long getProgressHistoryId() {
        return progressHistoryId;
    }

    public void setProgressHistoryId(Long progressHistoryId) {
        this.progressHistoryId = progressHistoryId;
    }

    public Date getDateOfRecord() {
        return dateOfRecord;
    }

    public void setDateOfRecord(Date dateOfRecord) {
        this.dateOfRecord = dateOfRecord;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Program getProgramId() {
        return programId;
    }

    public void setProgramId(Program programId) {
        this.programId = programId;
    }

    public Milestone getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(Milestone milestoneId) {
        this.milestoneId = milestoneId;
    }
}
