package demo.common_entity_demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "account_sequence")
public class AccountSequence {
    @Id
    @Column(name = "seq_key")
    private String seqKey;

    @Column(name = "current_value", nullable = false)
    private long currentValue;

    public String getSeqKey() {
        return seqKey;
    }

    public void setSeqKey(String seqKey) {
        this.seqKey = seqKey;
    }

    public long getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(long currentValue) {
        this.currentValue = currentValue;
    }
}
