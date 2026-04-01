package com.sketch.inventory_service.service;

import com.sketch.inventory_service.dto.ProductDto;
import com.sketch.inventory_service.entity.Product;
import com.sketch.inventory_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
}
