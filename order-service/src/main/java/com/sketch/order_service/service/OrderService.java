package com.sketch.order_service.service;

import com.sketch.order_service.dto.OrderRequestDto;
import com.sketch.order_service.entity.Orders;
import com.sketch.order_service.repositories.OrderRepo;
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
}
