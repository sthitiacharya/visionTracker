package com.visiontracker.challengeTrackerApplication.models.db;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Reward implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rewardId")
    private Long rewardId;

    @Basic(optional = false)
    @Column(name = "rewardCode", unique = true)
    private String rewardCode;

    @Basic(optional = false)
    @Column(name = "rewardTitle", unique = true, length = 64)
    private String rewardTitle;

    @Basic(optional = false)
    @Column(name = "rewardDescription")
    private String rewardDescription;

    @Basic(optional = false)
    @Column(name = "rewardType")
    private String rewardType;

    @Column(name = "storeAddress")
    private String storeAddress;

    @Column(name = "redeemablePoints")
    private int redeemablePoints;

    @Column(name = "rewardImageLink")
    private String rewardImageLink;

    public Reward() {

    }

    public Reward(String rewardCode, String rewardTitle, String rewardDescription, String rewardType, int redeemablePoints) {
        this();
        this.setRedeemablePoints(redeemablePoints);
        this.setRewardCode(rewardCode);
        this.setRewardTitle(rewardTitle);
        this.setRewardDescription(rewardDescription);
        this.setRewardType(rewardType);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getRewardId() != null ? getRewardId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reward)) {
            return false;
        }
        Reward other = (Reward) object;
        if ((this.getRewardId() == null && other.getRewardId() != null) || (this.getRewardId() != null && !this.getRewardId().equals(other.getRewardId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Reward[ rewardId=" + getRewardId() + " ]";
    }

    public Long getRewardId() {
        return rewardId;
    }

    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }

    public String getRewardCode() {
        return rewardCode;
    }

    public void setRewardCode(String rewardCode) {
        this.rewardCode = rewardCode;
    }

    public String getRewardTitle() {
        return rewardTitle;
    }

    public void setRewardTitle(String rewardTitle) {
        this.rewardTitle = rewardTitle;
    }

    public String getRewardDescription() {
        return rewardDescription;
    }

    public void setRewardDescription(String rewardDescription) {
        this.rewardDescription = rewardDescription;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public int getRedeemablePoints() {
        return redeemablePoints;
    }

    public void setRedeemablePoints(int redeemablePoints) {
        this.redeemablePoints = redeemablePoints;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardImageLink() {
        return rewardImageLink;
    }

    public void setRewardImageLink(String rewardImageLink) {
        this.rewardImageLink = rewardImageLink;
    }
}
