package com.sketch.order_service.dto;

import lombok.Data;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderRequestDto {

    private Long id;
    private List<OrderRequestItemDto> items;
    private BigDecimal totalPrice;
}
