package com.delivery.monitor.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.delivery.monitor.domain.Products;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductsGenerator {

    private final ProductsMapper productsMapper;

    // 제품 정보를 JSON 파일에서 읽어와 DB에 삽입하는 메서드
    @Transactional
    public void loadProductsFromFile() {
        log.info("상품 등록");
        try (InputStream inputStream = getProductsJsonInputStream()) {
            insertProductsIntoDatabase(readProductsFromJson(inputStream));
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    // 제품 정보가 있는 JSON 파일의 InputStream을 얻는 메서드
    private InputStream getProductsJsonInputStream() throws IOException {
        InputStream inputStream = getClass().getResourceAsStream("/static/Products.json");
        if (inputStream == null) {
            throw new IOException("File not found: Products.json");
        }
        return inputStream;
    }

    // InputStream에서 제품 정보를 읽어 List로 반환하는 메서드
    private List<Products> readProductsFromJson(InputStream inputStream) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            return new GsonBuilder().create().fromJson(reader, new TypeToken<List<Products>>() {
            }.getType());
        }
    }

    // 읽어온 제품 정보를 DB에 삽입하는 메서드
    private void insertProductsIntoDatabase(List<Products> products) {
        try {
            for (int i = 0; i < products.size(); i += 100) {
                productsMapper.autoInsertProducts(products.subList(i, Math.min(products.size(), i + 100)));
            }
        } catch (DataAccessException e) {
            handleDatabaseException(e);
        }
    }

    // DB 삽입 중 발생한 예외를 처리하는 메서드
    private void handleDatabaseException(DataAccessException e) {
        log.error("An error occurred while inserting products into the database", e);
        throw new RuntimeException("An error occurred while inserting products into the database", e);
    }

    // 파일 읽기 중 발생한 예외를 처리하는 메서드
    private void handleIOException(IOException e) {
        log.error("An error occurred while reading the products from the JSON file", e);
        throw new RuntimeException("An error occurred while reading the products from the JSON file", e);
    }
}
