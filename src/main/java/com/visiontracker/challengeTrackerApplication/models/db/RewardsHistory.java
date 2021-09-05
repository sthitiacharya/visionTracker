package com.visiontracker.challengeTrackerApplication.models.db;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "rewardsHistory")
public class RewardsHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "rewardsHistoryId")
    private Long rewardsHistoryId;

    @Column(name = "dateOfRedemption")
    @Temporal(TemporalType.DATE)
    private Date dateOfRedemption;

    @Column(name = "redeemPointValue")
    private int redeemPointValue;

    @JoinColumn(name = "reward", referencedColumnName = "rewardId")
    @ManyToOne(optional = false)
    private Reward reward;

    @JoinColumn(name = "user", referencedColumnName = "userId")
    @ManyToOne(optional = false)
    private User user;

    public Long getRewardsHistoryId() {
        return rewardsHistoryId;
    }

    public void setRewardsHistoryId(Long rewardsHistoryId) {
        this.rewardsHistoryId = rewardsHistoryId;
    }

    public Date getDateOfRedemption() {
        return dateOfRedemption;
    }

    public void setDateOfRedemption(Date dateOfRedemption) {
        this.dateOfRedemption = dateOfRedemption;
    }

    public int getRedeemPointValue() {
        return redeemPointValue;
    }

    public void setRedeemPointValue(int redeemPointValue) {
        this.redeemPointValue = redeemPointValue;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rewardsHistoryId != null ? rewardsHistoryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RewardsHistory)) {
            return false;
        }
        RewardsHistory other = (RewardsHistory) object;
        if ((this.rewardsHistoryId == null && other.rewardsHistoryId != null) || (this.rewardsHistoryId != null && !this.rewardsHistoryId.equals(other.rewardsHistoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.RewardsHistory[ rewardsHistoryId=" + rewardsHistoryId + " ]";
    }
}
