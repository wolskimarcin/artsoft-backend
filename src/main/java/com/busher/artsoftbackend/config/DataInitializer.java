package com.busher.artsoftbackend.config;

import com.busher.artsoftbackend.dao.ProductRepository;
import com.busher.artsoftbackend.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements ApplicationRunner {

    private final ProductRepository productRepository;

    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Initializing products in the database...");

        Product product1 = new Product();
        product1.setName("ASG Gun 1");
        product1.setShortDescription("Short desc for ASG Gun 1...");
        product1.setLongDescription("Long desc for ASG Gun 1...");
        product1.setPrice(22.50);

        Product product2 = new Product();
        product2.setName("TechGadget X");
        product2.setShortDescription("Compact and powerful tech gadget...");
        product2.setLongDescription("Explore the latest features of TechGadget X...");
        product2.setPrice(99.99);

        Product product3 = new Product();
        product3.setName("Fashionable Watch");
        product3.setShortDescription("Elegant design, perfect for any occasion...");
        product3.setLongDescription("Stay on time with this fashionable and functional watch...");
        product3.setPrice(49.99);

        Product product4 = new Product();
        product4.setName("Adventure Backpack");
        product4.setShortDescription("Durable backpack for all your adventures...");
        product4.setLongDescription("Explore the outdoors with confidence using our Adventure Backpack...");
        product4.setPrice(79.99);

        Product product5 = new Product();
        product5.setName("Smart Home Hub");
        product5.setShortDescription("Control your home with the latest smart technology...");
        product5.setLongDescription("Transform your living space with our advanced Smart Home Hub...");
        product5.setPrice(129.99);

        productRepository.saveAll(Arrays.asList(product1, product2, product3, product4, product5));

    }
}
