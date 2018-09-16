package com.telran.annotation.product;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telran.annotation.engine.annotations.ProductServiceAnnotation;
import com.telran.annotation.engine.annotations.Run;
import com.telran.annotation.engine.annotations.Value;
import com.telran.annotation.product.model.Product;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Scanner;

@ProductServiceAnnotation
public class ProductService {


    @Value(valueFromPropertyFile = "file.name")
    private String filename;

    @Run
    @SneakyThrows
    public void startPoint() {
        System.out.println("Enter product:");
        Scanner scanner = new Scanner(System.in);
        String rawData = scanner.nextLine();
        //Product p = new Product(param1, param2);
        Product product = Product
                .builder()
                .productName(rawData.split(",")[0])
                .price(Double.parseDouble(
                        rawData.split(",")[1]
                ))
                .build();

        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            fw.append("[]");
            fw.flush();
            fw.close();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Product> products = objectMapper.readValue(
                file,
                new TypeReference<List<Product>>() {});

        products.add(product);
        product.setId(products.size());

        objectMapper.writeValue(file, products);

        products.forEach(x -> System.out.println(x));
    }
}
