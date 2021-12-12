package com.sense.mos.infrastructure.repository.entity;

import com.sense.mos.service.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Optional.ofNullable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "\"order\"")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    @JoinTable(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Fetch(FetchMode.SELECT)
    private List<Item> items;

    @EqualsAndHashCode.Include
    private BigDecimal getAmountForEquals() {
        return ofNullable(amount).map(BigDecimal::stripTrailingZeros).orElse(null);
    }

}
