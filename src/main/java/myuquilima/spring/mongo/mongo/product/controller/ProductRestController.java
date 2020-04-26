package myuquilima.spring.mongo.mongo.product.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import myuquilima.spring.mongo.mongo.product.model.Product;
import myuquilima.spring.mongo.mongo.product.repository.ProductRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class ProductRestController {

    @Autowired
    private ProductRepository productRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductRestController.class);

    @RequestMapping(value = "products/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Product>> getProduct(@PathVariable("id") String id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent()) {
            return new ResponseEntity<EntityModel<Product>>(HttpStatus.NOT_FOUND);
        }
        EntityModel<Product> productModel = new EntityModel<Product>(product.get(),
                linkTo(methodOn(ProductRestController.class).getProduct(product.get().getId())).withSelfRel());
        return new ResponseEntity<EntityModel<Product>>(productModel, HttpStatus.OK);
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<Product>>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        Link links[] = { linkTo(methodOn(ProductRestController.class).getAllProducts()).withRel("getAllProducts") };
        if (products.isEmpty()) {
            return new ResponseEntity<CollectionModel<EntityModel<Product>>>(HttpStatus.NOT_FOUND);
        }
        List<EntityModel<Product>> list = new ArrayList<EntityModel<Product>>();
        for (Product product : products) {
            list.add(new EntityModel<Product>(product,
                    linkTo(methodOn(ProductRestController.class).getProduct(product.getId())).withSelfRel()));
        }
        CollectionModel<EntityModel<Product>> productRes = new CollectionModel<EntityModel<Product>>(list, links);
        return new ResponseEntity<CollectionModel<EntityModel<Product>>>(productRes, HttpStatus.OK);
    }

    @PostMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Product>> postProduct(@RequestBody Product product,
            UriComponentsBuilder uBuilder) {
        List<Product> products = productRepository.findByCode(product.getCode());
        if (products.size() > 0) {
            LOGGER.debug("A product with code {} already exists", product.getCode());
            return new ResponseEntity<EntityModel<Product>>(HttpStatus.CONFLICT);
        }
        Product newProduct = productRepository.save(product);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri());
        EntityModel<Product> productRes = new EntityModel<Product>(newProduct,
                linkTo(methodOn(ProductRestController.class).getProduct(newProduct.getId())).withSelfRel());
        return new ResponseEntity<EntityModel<Product>>(productRes, headers, HttpStatus.OK);

    }

    @PutMapping(value = "products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EntityModel<Product>> updateProduct(@PathVariable String id, @RequestBody Product product) {
        Optional<Product> currentProduct = productRepository.findById(id);
        if (!currentProduct.isPresent()) {
            return new ResponseEntity<EntityModel<Product>>(HttpStatus.NOT_FOUND);
        }
        Product updateProduct = currentProduct.get();
        updateProduct.setName(product.getName());
        updateProduct.setCode(product.getCode());
        updateProduct.setTitle(product.getTitle());
        updateProduct.setDescription(product.getDescription());
        updateProduct.setImgUrl(product.getImgUrl());
        updateProduct.setPrice(product.getPrice());
        updateProduct.setProductCategoryName(product.getProductCategoryName());
        Product newProduct = productRepository.save(updateProduct);
        
        EntityModel<Product> productRes = new EntityModel<Product>(newProduct,
        linkTo(methodOn(ProductRestController.class).getProduct(newProduct.getId())).withSelfRel());
        return new ResponseEntity<EntityModel<Product>>(productRes, HttpStatus.OK);
    }
    
    @RequestMapping(value="/products/{id}", method=RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> deleteproduct(@PathVariable("id") String id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }
        productRepository.deleteById(id);
        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }
    

}