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
        product1.setName("ASG Gun");
        product1.setShortDescription("Simple ASG Gun for beginners.");
        product1.setLongDescription("Lorem ipsum dolor sit amet");
        product1.setPrice(22.50);

        Product product2 = new Product();
        product2.setName("TechGadget X");
        product2.setShortDescription("Compact and powerful tech gadget.");
        product2.setLongDescription("Explore the latest features of TechGadget X.");
        product2.setPrice(99.99);

        Product product3 = new Product();
        product3.setName("Fashionable Watch");
        product3.setShortDescription("Elegant design, perfect for any occasion.");
        product3.setLongDescription("Stay on time with this fashionable and functional watch.");
        product3.setPrice(49.99);

        Product product4 = new Product();
        product4.setName("Adventure Backpack");
        product4.setShortDescription("Durable backpack for all your adventures.");
        product4.setLongDescription("Explore the outdoors with confidence using our Adventure Backpack.");
        product4.setPrice(79.99);

        Product product5 = new Product();
        product5.setName("Smart Home Hub");
        product5.setShortDescription("Control your home with the latest smart technology.");
        product5.setLongDescription("Transform your living space with our advanced Smart Home Hub.");
        product5.setPrice(129.99);

        Product product6 = new Product();
        product6.setName("Eco-Friendly Water Bottle");
        product6.setShortDescription("Stay hydrated with an environmentally conscious choice.");
        product6.setLongDescription("Our Eco-Friendly Water Bottle is made from 100% recycled materials, designed to keep you hydrated while reducing plastic waste. Perfect for the eco-conscious consumer.");
        product6.setPrice(14.95);

        Product product7 = new Product();
        product7.setName("Wireless Earbuds Pro");
        product7.setShortDescription("Immersive sound, anytime, anywhere.");
        product7.setLongDescription("Experience unparalleled sound quality with our Wireless Earbuds Pro. Featuring noise cancellation technology and long-lasting battery life for all-day listening.");
        product7.setPrice(159.99);

        Product product8 = new Product();
        product8.setName("Professional Kitchen Knife Set");
        product8.setShortDescription("Precision cutting for culinary enthusiasts.");
        product8.setLongDescription("Elevate your cooking with our Professional Kitchen Knife Set. Each knife is crafted for durability and precision, ensuring the perfect cut every time.");
        product8.setPrice(199.99);

        Product product9 = new Product();
        product9.setName("Organic Skin Care Bundle");
        product9.setShortDescription("Nourish your skin with the best of nature.");
        product9.setLongDescription("Our Organic Skin Care Bundle includes a range of products made from natural ingredients, designed to hydrate, nourish, and rejuvenate your skin.");
        product9.setPrice(59.99);

        Product product10 = new Product();
        product10.setName("Virtual Reality Headset");
        product10.setShortDescription("Step into new worlds with cutting-edge VR technology.");
        product10.setLongDescription("Our Virtual Reality Headset offers an immersive experience with high-resolution display and motion tracking, bringing virtual worlds to life.");
        product10.setPrice(299.99);

        Product product11 = new Product();
        product11.setName("Ergonomic Office Chair");
        product11.setShortDescription("Comfort and support for your workday.");
        product11.setLongDescription("Maximize productivity with our Ergonomic Office Chair, featuring adjustable height and lumbar support to keep you comfortable for hours.");
        product11.setPrice(249.99);

        Product product12 = new Product();
        product12.setName("LED Smart Light Bulb");
        product12.setShortDescription("Illuminate your home with smart lighting.");
        product12.setLongDescription("Our LED Smart Light Bulb can be controlled via smartphone, offering millions of colors and adjustable brightness to suit any mood or occasion.");
        product12.setPrice(19.99);

        Product product13 = new Product();
        product13.setName("High-Performance Gaming Mouse");
        product13.setShortDescription("Precision and speed for serious gamers.");
        product13.setLongDescription("Dominate the competition with our High-Performance Gaming Mouse, featuring customizable buttons and ultra-responsive tracking.");
        product13.setPrice(89.99);

        Product product14 = new Product();
        product14.setName("Compact Home Gym");
        product14.setShortDescription("A complete workout from the comfort of home.");
        product14.setLongDescription("Our Compact Home Gym includes everything you need for a full-body workout, saving space without compromising on functionality.");
        product14.setPrice(599.99);

        Product product15 = new Product();
        product15.setName("Portable Bluetooth Speaker");
        product15.setShortDescription("Your soundtrack, anywhere you go.");
        product15.setLongDescription("Bring the party with you with our Portable Bluetooth Speaker. High-quality sound, durable design, and long battery life for music wherever you are.");
        product15.setPrice(49.99);

        productRepository.saveAll(Arrays.asList(product1,
                product2,
                product3,
                product4,
                product5,
                product6,
                product7,
                product8,
                product9,
                product10,
                product11,
                product12,
                product13,
                product14,
                product15));

    }
}
