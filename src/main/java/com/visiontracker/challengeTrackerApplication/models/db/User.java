/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visiontracker.challengeTrackerApplication.models.db;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sthit
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findByUserId", query = "SELECT u FROM User u WHERE u.userId = :userId"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByUsername", query = "SELECT u FROM User u WHERE u.username = :username"),
    @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password"),
    @NamedQuery(name = "User.findByMailingAddress", query = "SELECT u FROM User u WHERE u.mailingAddress = :mailingAddress")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "userId")
    private Integer userId;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 255)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 64)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 64)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 255)
    @Column(name = "mailingAddress")
    private String mailingAddress;
    @ManyToMany(mappedBy = "userList")
    private List<Program> enrolledPrograms;
    @OneToMany(mappedBy = "assignedUser")
    private List<Milestone> milestoneList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "milestoneCreatedBy")
    private List<Milestone> milestonesCreated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "programManager")
    private List<Program> programsManaging;

    public User() {
        this.enrolledPrograms = new ArrayList<>();
        this.milestoneList = new ArrayList<>();
        this.milestonesCreated = new ArrayList<>();
        this.programsManaging = new ArrayList<>();
    }

    public User(Integer userId) {
        this();
        this.userId = userId;
    }

    public User(String email, String username, String password, String mailingAddress) {
        this();
        this.email = email;
        this.username = username;
        this.password = password;
        this.mailingAddress = mailingAddress;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    @XmlTransient
    public List<Program> getEnrolledPrograms() {
        return enrolledPrograms;
    }

    public void setEnrolledPrograms(List<Program> enrolledPrograms) {
        this.enrolledPrograms = enrolledPrograms;
    }

    @XmlTransient
    public List<Milestone> getMilestoneList() {
        return milestoneList;
    }

    public void setMilestoneList(List<Milestone> milestoneList) {
        this.milestoneList = milestoneList;
    }

    @XmlTransient
    public List<Milestone> getMilestonesCreated() {
        return milestonesCreated;
    }

    public void setMilestonesCreated(List<Milestone> milestonesCreated) {
        this.milestonesCreated = milestonesCreated;
    }

    @XmlTransient
    public List<Program> getProgramsManaging() {
        return programsManaging;
    }

    public void setProgramsManaging(List<Program> programsManaging) {
        this.programsManaging = programsManaging;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.User[ userId=" + userId + " ]";
    }
    
}
