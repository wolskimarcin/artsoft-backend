package com.busher.artsoftbackend.config;

import com.busher.artsoftbackend.dao.InventoryRepository;
import com.busher.artsoftbackend.dao.LocalUserRepository;
import com.busher.artsoftbackend.dao.ProductRepository;
import com.busher.artsoftbackend.model.Inventory;
import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.model.Product;
import com.busher.artsoftbackend.service.PasswordHashingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements ApplicationRunner {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final LocalUserRepository localUserRepository;
    private final PasswordHashingService passwordHashingService;
    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(ProductRepository productRepository, InventoryRepository inventoryRepository, LocalUserRepository localUserRepository, PasswordHashingService passwordHashingService) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.localUserRepository = localUserRepository;
        this.passwordHashingService = passwordHashingService;
    }

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Initializing products in the database...");

        List<Product> products = Arrays.asList(
                new Product(null, "Journey Through Java", "Introduction to Java programming.", "A comprehensive guide to master Java programming for beginners. Covering fundamentals to advanced topics.", 45.00, null),
                new Product(null, "EcoSmart Speaker", "Eco-friendly smart speaker with high-quality sound.", "Experience premium sound quality while being environmentally conscious. Features voice control and seamless integration with your smart home.", 120.00, null),
                new Product(null, "Fashionable Watch", "Elegant design, perfect for any occasion.", "Stay on time with this fashionable and functional watch.", 49.99, null),
                new Product(null, "Adventure Backpack", "Durable backpack for all your adventures.", "Explore the outdoors with confidence using our Adventure Backpack.", 79.99, null),
                new Product(null, "Smart Home Hub", "Control your home with the latest smart technology.", "Transform your living space with our advanced Smart Home Hub.", 129.99, null),
                new Product(null, "Eco-Friendly Water Bottle", "Stay hydrated with an environmentally conscious choice.", "Our Eco-Friendly Water Bottle is made from 100% recycled materials, designed to keep you hydrated while reducing plastic waste. Perfect for the eco-conscious consumer.", 14.95, null),
                new Product(null, "Wireless Earbuds Pro", "Immersive sound, anytime, anywhere.", "Experience unparalleled sound quality with our Wireless Earbuds Pro. Featuring noise cancellation technology and long-lasting battery life for all-day listening.", 159.99, null),
                new Product(null, "Professional Kitchen Knife Set", "Precision cutting for culinary enthusiasts.", "Elevate your cooking with our Professional Kitchen Knife Set. Each knife is crafted for durability and precision, ensuring the perfect cut every time.", 199.99, null),
                new Product(null, "Organic Cream", "Nourish your skin with the best of nature.", "Our Organic Skin Care Bundle includes a range of products made from natural ingredients, designed to hydrate, nourish, and rejuvenate your skin.", 59.99, null),
                new Product(null, "Virtual Reality Headset", "Step into new worlds with cutting-edge VR technology.", "Our Virtual Reality Headset offers an immersive experience with high-resolution display and motion tracking, bringing virtual worlds to life.", 299.99, null),
                new Product(null, "Ergonomic Office Chair", "Comfort and support for your workday.", "Maximize productivity with our Ergonomic Office Chair, featuring adjustable height and lumbar support to keep you comfortable for hours.", 249.99, null),
                new Product(null, "LED Smart Light Bulb", "Illuminate your home with smart lighting.", "Our LED Smart Light Bulb can be controlled via smartphone, offering millions of colors and adjustable brightness to suit any mood or occasion.", 19.99, null),
                new Product(null, "High-Performance Gaming Mouse", "Precision and speed for serious gamers.", "Dominate the competition with our High-Performance Gaming Mouse, featuring customizable buttons and ultra-responsive tracking.", 89.99, null),
                new Product(null, "Compact Home Gym", "A complete workout from the comfort of home.", "Our Compact Home Gym includes everything you need for a full-body workout, saving space without compromising on functionality.", 599.99, null),
                new Product(null, "Portable Bluetooth Speaker", "Your soundtrack, anywhere you go.", "Bring the party with you with our Portable Bluetooth Speaker. High-quality sound, durable design, and long battery life for music wherever you are.", 49.99, null),
                new Product(null, "Ultra HD 4K Monitor", "Experience stunning visuals with cutting-edge display technology.", "The Ultra HD 4K Monitor offers unparalleled clarity and detail for professionals and enthusiasts alike.", 349.99, null),
                new Product(null, "Gourmet Coffee Beans", "Rich flavor and aroma, sourced from the best global coffee farms.", "Our Gourmet Coffee Beans are selected for their unique profiles, promising a delightful brewing experience every morning.", 15.99, null),
                new Product(null, "Luxury Silk Pillowcase", "Elevate your sleep with the soft touch of pure silk.", "Crafted from 100% mulberry silk, our Luxury Silk Pillowcase is gentle on your skin and hair, ensuring a restful night's sleep.", 39.99, null),
                new Product(null, "Ergonomic Wireless Mouse", "Comfort meets functionality in this sleek wireless mouse.", "Designed for comfort and precision, our Ergonomic Wireless Mouse reduces wrist strain while enhancing your computing experience.", 29.99, null),
                new Product(null, "Bluetooth Noise-Cancelling Headphones", "Immerse yourself in sound with advanced noise cancellation.", "Enjoy your favorite music without distractions with our Bluetooth Noise-Cancelling Headphones, featuring long battery life and superior comfort.", 199.99, null),
                new Product(null, "Electric Standing Desk", "Transform your workspace with adjustable height settings.", "Our Electric Standing Desk promotes a healthier work environment by allowing you to switch between sitting and standing with ease.", 499.99, null),
                new Product(null, "Professional Drone with Camera", "Capture breathtaking aerial footage with state-of-the-art technology.", "The Professional Drone offers stability, clarity, and performance for both amateur and experienced pilots looking to explore the skies.", 999.99, null),
                new Product(null, "Smart Thermostat", "Optimize your home's climate with intelligent temperature control.", "With our Smart Thermostat, achieve energy efficiency and comfort by customizing your heating and cooling preferences remotely.", 249.99, null),
                new Product(null, "Organic Matcha Tea Powder", "Premium grade matcha for a healthy and energizing beverage.", "Sourced directly from Japan, our Organic Matcha Tea Powder is rich in antioxidants and provides a smooth, vibrant green tea flavor.", 19.99, null),
                new Product(null, "Adjustable Kettlebell", "Versatile fitness equipment for a full-body workout at home.", "Switch between multiple weight settings with our Adjustable Kettlebell, designed for both beginners and fitness enthusiasts.", 129.99, null),
                new Product(null, "Indoor Herb Garden Kit", "Grow your own herbs indoors with ease.", "Our Indoor Herb Garden Kit includes everything you need to start your own herb garden, perfect for fresh flavors all year round.", 59.99, null),
                new Product(null, "Premium Yoga Mat", "Experience superior grip and comfort during your yoga practice.", "Crafted with eco-friendly materials, our Premium Yoga Mat supports your yoga journey with durability and cushioning.", 49.99, null),
                new Product(null, "LED Desk Lamp with Wireless Charging", "Illuminate your workspace with adjustable lighting and built-in wireless charging.", "Our LED Desk Lamp offers customizable light settings and a wireless charging pad for your devices, combining functionality and style.", 89.99, null),
                new Product(null, "Mechanical Keyboard for Professionals", "Enhance your typing experience with tactile feedback and precision.", "Designed for durability and performance, our Mechanical Keyboard brings a satisfying typing experience to your desk.", 109.99, null),
                new Product(null, "Stainless Steel Water Bottle", "Stay hydrated in style with our eco-friendly and durable water bottle.", "Keep your drinks cold for 24 hours or hot for 12 with our Stainless Steel Water Bottle, perfect for everyday use and outdoor adventures.", 24.99, null),
                new Product(null, "Multi-Purpose Fitness Tracker", "Track your fitness goals and monitor your health with advanced technology.", "Our Multi-Purpose Fitness Tracker offers features like heart rate monitoring, sleep analysis, and exercise tracking to keep you motivated.", 159.99, null)
        );

        List<Product> savedProducts = productRepository.saveAll(products);

        List<Inventory> inventories = savedProducts.stream().map(product ->
                new Inventory(null, product, new Random().nextInt(100) + 1)
        ).collect(Collectors.toList());

        inventoryRepository.saveAll(inventories);


        LocalUser adminUser = new LocalUser();
        adminUser.setId(2137L);
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordHashingService.hashPassword("password2137"));
        adminUser.setEmail("admin@example.com");
        adminUser.setFirstName("Admin");
        adminUser.setLastName("Temporary");
        adminUser.setIsEmailVerified(true);
        localUserRepository.save(adminUser);
    }
}
