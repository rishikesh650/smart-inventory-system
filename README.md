# Smart Inventory & Demand Prediction System - Setup Guide

## Prerequisites
- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+

## Database Setup
1. Create a database named `inventory_db` in MySQL.
2. Run the `src/main/resources/db/schema.sql` script.
3. Use the sample data below to populate the system for testing.

```sql
-- Insert Categories
INSERT INTO categories (name, description) VALUES ('Electronics', 'Devices and gadgets'), ('Office Supplies', 'Stationery and furniture');

-- Insert Suppliers
INSERT INTO suppliers (name, contact_person, email, phone, address) 
VALUES ('Global Tech Inc', 'John Doe', 'supplier@globaltech.com', '123-456-7890', '123 Tech Park');

-- Insert Products
INSERT INTO products (sku, name, description, category_id, supplier_id, unit_price, reorder_threshold)
VALUES ('SKU-001', 'Pro Laptop', 'High performance laptop', 1, 1, 1200.00, 5),
       ('SKU-002', 'Wireless Mouse', 'Ergonomic mouse', 1, 1, 25.00, 10);

-- Initialize Inventory
INSERT INTO inventory (product_id, current_stock) VALUES (1, 10), (2, 50);

-- Register Admin (admin / admin123) - Note: Password is BCrypt hashed for 'admin123'
INSERT INTO users (username, password, email, full_name, role_id)
VALUES ('admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.7uCyQfS', 'admin@inventory.com', 'System Admin', 1);
```

## Running the Application
1. Update `src/main/resources/application.properties` with your MySQL credentials.
2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
3. Open `http://localhost:9090` in your browser.
4. Login with:
   - **Username**: admin
   - **Password**: admin123

## Key Features
- **Dashboard**: View low stock alerts and demand predictions.
- **Product Management**: Track SKU levels and reorder thresholds.
- **Sales Tracking**: Record transactions; stock updates automatically.
- **Auto-POC**: Predicts based on average daily usage and estimates days left.
