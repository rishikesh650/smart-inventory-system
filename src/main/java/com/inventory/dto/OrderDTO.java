package com.inventory.dto;

import com.inventory.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private Long supplierId;
    private String supplierName;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;
}
