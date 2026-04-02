package com.sketch.inventory_service.controller;


import com.sketch.inventory_service.clients.OrdersFeignClient;
import com.sketch.inventory_service.dto.OrderRequestDto;
import com.sketch.inventory_service.dto.ProductDto;
import com.sketch.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final DiscoveryClient discoveryClient;
    private final OrdersFeignClient ordersFeignClient;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts(){
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id){
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("/fetch-orders")
    public String fetchOrdersfromOrderService(){
//        ServiceInstance orderService = discoveryClient.getInstances("order-service").getFirst();
//        return restClient.get().uri(orderService.getUri()+"/orders/core/status")
//                .retrieve()
//                .body(String.class);
        return ordersFeignClient.status();
    }

    @PutMapping("/reduce-stock")
    public ResponseEntity<Double> reduceStock(@RequestBody OrderRequestDto orderRequestDto){
        Double total_price = productService.reduceStocks(orderRequestDto);
        return new ResponseEntity<>(total_price, HttpStatus.OK);
    }
}
