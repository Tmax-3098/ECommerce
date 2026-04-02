package com.sketch.order_service.service;

import com.sketch.order_service.clients.InventoryFeignClient;
import com.sketch.order_service.dto.OrderRequestDto;
import com.sketch.order_service.entity.OrderItem;
import com.sketch.order_service.entity.OrderStatus;
import com.sketch.order_service.entity.Orders;
import com.sketch.order_service.repositories.OrderRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepo orderRepo;
    private final ModelMapper mapper;
    private final InventoryFeignClient inventoryFeignClient;

    public List<OrderRequestDto> getAllOrders(){
        log.info("Getting all orders");
        List<Orders> orders = orderRepo.findAll();
        return orders.stream().map(
                order -> mapper.map(order, OrderRequestDto.class)
        ).toList();
    }

    public OrderRequestDto getOrderById(Long id){
        log.info("Getting order by id: {}", id);
        Orders order = orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        return mapper.map(order, OrderRequestDto.class);

    }

   // @Retry(name="inventoryRetry", fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name="inventoryCircuitBreaker", fallbackMethod = "createOrderFallback")
    //@RateLimiter(name="inventoryRateLimiter", fallbackMethod = "createOrderFallback")
    public OrderRequestDto createOrder(OrderRequestDto orderRequestDto) {
        log.info("Creating order");
        Double total_Price = inventoryFeignClient.reduceStock(orderRequestDto);
        Orders order = mapper.map(orderRequestDto, Orders.class);
        for(OrderItem orderItem: order.getItems()){
            orderItem.setOrder(order);
        }
        order.setTotalPrice(total_Price);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        Orders savedOrder = orderRepo.save(order);
        return mapper.map(savedOrder, OrderRequestDto.class);
    }

    public OrderRequestDto createOrderFallback(OrderRequestDto orderRequestDto, Throwable throwable) {
        log.error("Falling due to : {}", throwable.getMessage());
        return new OrderRequestDto();
    }

}
