package com.sketch.order_service.clients;

import com.sketch.order_service.dto.OrderRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", path = "/inventory")
public interface InventoryFeignClient {

    @PutMapping("/products/reduce-stock")
    Double reduceStock(@RequestBody OrderRequestDto orderRequestDto);
}
