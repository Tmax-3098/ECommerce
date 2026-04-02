package com.sketch.inventory_service.service;

import com.sketch.inventory_service.dto.OrderRequestDto;
import com.sketch.inventory_service.dto.OrderRequestItemDto;
import com.sketch.inventory_service.dto.ProductDto;
import com.sketch.inventory_service.entity.Product;
import com.sketch.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;


    public List<ProductDto> getAllProducts(){
        log.info("Getting all products");
        List<Product> products = productRepository.findAll();
        return products.stream().map(
                product -> modelMapper.map(product, ProductDto.class)
        ).toList();
    }


    public ProductDto getProductById(Long id){
        log.info("Getting Product By ID");
        Product product = productRepository.findById(id).orElseThrow(()-> new RuntimeException("Product Not Found"));
        return modelMapper.map(product, ProductDto.class);

    }

    @Transactional
    public Double reduceStocks(OrderRequestDto orderRequestDto) {
        log.info("Reducing Stocks");
        Double total_price = 0.0;
        for(OrderRequestItemDto item: orderRequestDto.getItems()){
            Long pId= item.getProductId();
            Integer quantity = item.getQuantity();
            Product product = productRepository.findById(pId).orElseThrow(() -> new RuntimeException("Product Not Found"));
            if(product.getStock()<quantity){
                throw new RuntimeException("Product Not in Stock");
            }
            total_price+=product.getPrice()*quantity;
            product.setStock(product.getStock()-quantity);
            productRepository.save(product);
        }
        return total_price;
    }
}
