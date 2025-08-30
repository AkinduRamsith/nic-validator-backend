package org.oxy12.mintinvoiz.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private InvoiceEntity invoice;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private ItemEntity item;
    
    @Column(name = "item_name", nullable = false)
    private String itemName;
    
    @Column(name = "item_description", columnDefinition = "TEXT")
    private String itemDescription;
    
    @Column(name = "quantity", precision = 10, scale = 2, nullable = false)
    private BigDecimal quantity;
    
    @Column(name = "unit_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;
    
    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;
    
    @Column(name = "discount_type")
    @Enumerated(EnumType.STRING)
    private DiscountType discountType = DiscountType.NONE;
    
    @Column(name = "discount_value", precision = 10, scale = 2)
    private BigDecimal discountValue = BigDecimal.ZERO;
    
    @Column(name = "line_total", precision = 12, scale = 2, nullable = false)
    private BigDecimal lineTotal = BigDecimal.ZERO;
    
    @Column(name = "line_tax", precision = 12, scale = 2)
    private BigDecimal lineTax = BigDecimal.ZERO;
    
    @Column(name = "line_discount", precision = 12, scale = 2)
    private BigDecimal lineDiscount = BigDecimal.ZERO;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    public enum DiscountType {
        NONE, PERCENTAGE, FIXED_AMOUNT
    }
}