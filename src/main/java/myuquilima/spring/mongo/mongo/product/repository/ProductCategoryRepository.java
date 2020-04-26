package myuquilima.spring.mongo.mongo.product.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import myuquilima.spring.mongo.mongo.product.model.ProductCategory;

@RepositoryRestResource(collectionResourceRel = "categories", path = "categories")
public interface ProductCategoryRepository extends MongoRepository<ProductCategory, String> {

}
