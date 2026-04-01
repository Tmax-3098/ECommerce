package com.sketch.order_service.dto;

import lombok.Data;

@Data
public class OrderRequestItemDto {

    private Long id;
    private Long productId;
    private Long quantity;
}
