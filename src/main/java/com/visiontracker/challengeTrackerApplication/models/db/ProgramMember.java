package com.visiontracker.challengeTrackerApplication.models.db;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ProgramMember implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "programMemberId")
    private Long programMemberId;

    @ManyToOne
    private User userId;

    @ManyToOne
    private Program programId;

    public ProgramMember() {}

    public ProgramMember(User userId, Program programId)
    {
        this.programId = programId;
        this.userId = userId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User programMember) {
        this.userId = programMember;
    }

    public Program getProgramId() {
        return programId;
    }

    public void setProgramId(Program program) {
        this.programId = program;
    }
}
