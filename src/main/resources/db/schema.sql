-- SMART INVENTORY SYSTEM DATABASE SCHEMA

-- DROP TABLES IF THEY EXIST
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `purchase_order_items`;
DROP TABLE IF EXISTS `purchase_orders`;
DROP TABLE IF EXISTS `sales`;
DROP TABLE IF EXISTS `inventory`;
DROP TABLE IF EXISTS `products`;
DROP TABLE IF EXISTS `categories`;
DROP TABLE IF EXISTS `suppliers`;
DROP TABLE IF EXISTS `users`;
DROP TABLE IF EXISTS `roles`;
SET FOREIGN_KEY_CHECKS = 1;

-- CATEGORIES TABLE
CREATE TABLE `categories` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `description` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- SUPPLIERS TABLE
CREATE TABLE `suppliers` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(150) NOT NULL,
    `contact_person` VARCHAR(100),
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `phone` VARCHAR(20),
    `address` TEXT,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PRODUCTS TABLE
CREATE TABLE `products` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `sku` VARCHAR(50) NOT NULL UNIQUE,
    `name` VARCHAR(150) NOT NULL,
    `description` TEXT,
    `category_id` BIGINT,
    `supplier_id` BIGINT,
    `unit_price` DECIMAL(10, 2) NOT NULL,
    `reorder_threshold` INT DEFAULT 10,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`category_id`) REFERENCES `categories`(`id`) ON DELETE SET NULL,
    FOREIGN KEY (`supplier_id`) REFERENCES `suppliers`(`id`) ON DELETE SET NULL
);

-- INVENTORY TABLE
CREATE TABLE `inventory` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL UNIQUE,
    `current_stock` INT NOT NULL DEFAULT 0,
    `last_updated` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
);

-- SALES TABLE
CREATE TABLE `sales` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `unit_price` DECIMAL(10, 2) NOT NULL,
    `total_price` DECIMAL(12, 2) NOT NULL,
    `sale_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
);

-- PURCHASE ORDERS TABLE
CREATE TABLE `purchase_orders` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `supplier_id` BIGINT NOT NULL,
    `order_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `status` ENUM('PENDING', 'APPROVED', 'SENT', 'RECEIVED', 'CANCELLED') DEFAULT 'PENDING',
    `total_amount` DECIMAL(15, 2),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`supplier_id`) REFERENCES `suppliers`(`id`) ON DELETE CASCADE
);

-- PURCHASE ORDER ITEMS TABLE
CREATE TABLE `purchase_order_items` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `purchase_order_id` BIGINT NOT NULL,
    `product_id` BIGINT NOT NULL,
    `quantity` INT NOT NULL,
    `unit_price` DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (`purchase_order_id`) REFERENCES `purchase_orders`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`product_id`) REFERENCES `products`(`id`) ON DELETE CASCADE
);

-- ROLES TABLE
CREATE TABLE `roles` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL UNIQUE
);

-- USERS TABLE
CREATE TABLE `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `full_name` VARCHAR(100),
    `role_id` BIGINT,
    `enabled` BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (`role_id`) REFERENCES `roles`(`id`) ON DELETE SET NULL
);

-- SAMPLE DATA
INSERT INTO `roles` (`name`) VALUES ('ADMIN'), ('STAFF');
