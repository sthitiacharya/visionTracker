/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visiontracker.challengeTrackerApplication.models.db;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sthit
 */
@Entity
@Table(name = "milestone")
@XmlRootElement
public class Milestone implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "milestoneId")
    private Long milestoneId;
    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 64)
    @Column(name = "title", unique = true, length = 64)
    private String title;
    //@Size(max = 255)
    @Column(name = "description")
    private String description;
    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 64)
    @Column(name = "milestoneType")
    private String milestoneType;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "creationDate")
    @Temporal(TemporalType.DATE)
    private Date creationDate;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "targetCompletionDate")
    @Temporal(TemporalType.DATE)
    private Date targetCompletionDate;
    @Column(name = "actualCompletedDate")
    @Temporal(TemporalType.DATE)
    private Date actualCompletedDate;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    //@NotNull
    @Column(name = "initialValue")
    private BigDecimal initialValue;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "targetValue")
    private BigDecimal targetValue;
    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 64)
    @Column(name = "valueCategory")
    private String valueCategory;
    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 64)
    @Column(name = "valueType")
    private String valueType;
    @Basic(optional = false)
    //@NotNull
    @Column(name = "rewardValue")
    private int rewardValue;
    @Column(name = "reminderStartDate")
    @Temporal(TemporalType.DATE)
    private Date reminderStartDate;
    @Column(name = "reminderInterval")
    @Temporal(TemporalType.DATE)
    private Date reminderInterval;
    @JoinColumn(name = "programId", referencedColumnName = "programId")
    @ManyToOne(optional = false)
    private Program programId;
    @JoinColumn(name = "assignedUser", referencedColumnName = "userId")
    @ManyToOne
    private User assignedUser;
    @JoinColumn(name = "milestoneCreatedBy", referencedColumnName = "userId")
    @ManyToOne(optional = false)
    private User milestoneCreatedBy;

    public Milestone() {
    }

    public Milestone(Long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public Milestone(String title, String description, String milestoneType, Date creationDate, Date targetCompletionDate, BigDecimal initialValue, BigDecimal targetValue, String valueCategory, String valueType, int rewardValue) {
        this.title = title;
        this.description = description;
        this.milestoneType = milestoneType;
        this.creationDate = creationDate;
        this.targetCompletionDate = targetCompletionDate;
        this.initialValue = initialValue;
        this.targetValue = targetValue;
        this.valueCategory = valueCategory;
        this.valueType = valueType;
        this.rewardValue = rewardValue;
    }

    public Long getMilestoneId() {
        return milestoneId;
    }

    public void setMilestoneId(Long milestoneId) {
        this.milestoneId = milestoneId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMilestoneType() {
        return milestoneType;
    }

    public void setMilestoneType(String milestoneType) {
        this.milestoneType = milestoneType;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getTargetCompletionDate() {
        return targetCompletionDate;
    }

    public void setTargetCompletionDate(Date targetCompletionDate) {
        this.targetCompletionDate = targetCompletionDate;
    }

    public Date getActualCompletedDate() {
        return actualCompletedDate;
    }

    public void setActualCompletedDate(Date actualCompletedDate) {
        this.actualCompletedDate = actualCompletedDate;
    }

    public BigDecimal getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }

    public BigDecimal getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(BigDecimal targetValue) {
        this.targetValue = targetValue;
    }

    public String getValueCategory() {
        return valueCategory;
    }

    public void setValueCategory(String valueCategory) {
        this.valueCategory = valueCategory;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public int getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(int rewardValue) {
        this.rewardValue = rewardValue;
    }

    public Date getReminderStartDate() {
        return reminderStartDate;
    }

    public void setReminderStartDate(Date reminderStartDate) {
        this.reminderStartDate = reminderStartDate;
    }

    public Date getReminderInterval() {
        return reminderInterval;
    }

    public void setReminderInterval(Date reminderInterval) {
        this.reminderInterval = reminderInterval;
    }

    public com.visiontracker.challengeTrackerApplication.models.db.Program getProgramId() {
        return programId;
    }

    public void setProgramId(Program programId) {
        this.programId = programId;
    }

    public com.visiontracker.challengeTrackerApplication.models.db.User getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(com.visiontracker.challengeTrackerApplication.models.db.User assignedUser) {
        this.assignedUser = assignedUser;
    }

    public com.visiontracker.challengeTrackerApplication.models.db.User getMilestoneCreatedBy() {
        return milestoneCreatedBy;
    }

    public void setMilestoneCreatedBy(User milestoneCreatedBy) {
        this.milestoneCreatedBy = milestoneCreatedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (milestoneId != null ? milestoneId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Milestone)) {
            return false;
        }
        Milestone other = (Milestone) object;
        if ((this.milestoneId == null && other.milestoneId != null) || (this.milestoneId != null && !this.milestoneId.equals(other.milestoneId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Milestone[ milestoneId=" + milestoneId + " ]";
    }
    
}
