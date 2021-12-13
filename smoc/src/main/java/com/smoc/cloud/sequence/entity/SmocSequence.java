package com.smoc.cloud.sequence.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "smoc_sequence")
public class SmocSequence {
    private String seqName;
    private long currentVal;
    private int incrementVal;

    @Id
    @Column(name = "SEQ_NAME")
    public String getSeqName() {
        return seqName;
    }

    public void setSeqName(String seqName) {
        this.seqName = seqName;
    }

    @Basic
    @Column(name = "CURRENT_VAL")
    public long getCurrentVal() {
        return currentVal;
    }

    public void setCurrentVal(long currentVal) {
        this.currentVal = currentVal;
    }

    @Basic
    @Column(name = "INCREMENT_VAL")
    public int getIncrementVal() {
        return incrementVal;
    }

    public void setIncrementVal(int incrementVal) {
        this.incrementVal = incrementVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmocSequence that = (SmocSequence) o;
        return currentVal == that.currentVal &&
                incrementVal == that.incrementVal &&
                Objects.equals(seqName, that.seqName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seqName, currentVal, incrementVal);
    }
}
