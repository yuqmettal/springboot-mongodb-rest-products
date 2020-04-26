package myuquilima.spring.mongo.mongo.product.model;

import org.springframework.data.annotation.Id;

public class ProductCategory {
    @Id
    private String id;
    private String name;
    private String title;
    private String description;
    private String imgUrl;
}