package myuquilima.spring.mongo.mongo.product.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import myuquilima.spring.mongo.mongo.product.model.Product;

@RepositoryRestResource(collectionResourceRel = "productdata", path = "productdata")
public interface ProductRepository extends MongoRepository<Product, String> {
    public List<Product> findByProductCategoryName(@Param("productCategory") String  productCatagoryName);
	public List<Product> findByCode(@Param("code") String  code);
}