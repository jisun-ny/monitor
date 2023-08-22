package com.delivery.monitor.products;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.delivery.monitor.domain.Products;

@Mapper
public interface ProductsMapper {
    List<Products> getRandomProducts(int count);
    void inventoryReduction(int product_id, int sale);
    void autoInsertProducts(List<Products> products);
    String getCategoryByProductsId(int product_id);
}
