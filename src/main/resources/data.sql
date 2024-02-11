INSERT INTO public.discounts (discount_amount, discount_type, discount_code, minimum_amount, description)
VALUES (25.00, 'RATE_BASED', 'IXCDAE', 150.00, 'Gives %25 discount');
INSERT INTO public.discounts (discount_amount, discount_type, discount_code, minimum_amount, description)
VALUES (50.00, 'AMOUNT_BASED', 'ORMFRG', 250.00, 'Gives $ 50 discount');

-- Sample Categories
INSERT INTO public.categories (name)
VALUES ('Electronics');
INSERT INTO public.categories (name)
VALUES ('Clothing');
INSERT INTO public.categories (name)
VALUES ('Home and Kitchen');
INSERT INTO public.categories (name)
VALUES ('Books');
INSERT INTO public.categories (name)
VALUES ('Toys');
INSERT INTO public.categories (name)
VALUES ('Sports and Outdoors');
INSERT INTO public.categories (name)
VALUES ('Beauty and Personal Care');
INSERT INTO public.categories (name)
VALUES ('Health and Household');
INSERT INTO public.categories (name)
VALUES ('Automotive');
INSERT INTO public.categories (name)
VALUES ('Garden and Outdoor Living');
INSERT INTO public.categories (name)
VALUES ('Pet Supplies');
INSERT INTO public.categories (name)
VALUES ('Jewelry');
INSERT INTO public.categories (name)
VALUES ('Office Products');
INSERT INTO public.categories (name)
VALUES ('Baby Products');
INSERT INTO public.categories (name)
VALUES ('Tools and Home Improvement');
INSERT INTO public.categories (name)
VALUES ('Movies and TV');
INSERT INTO public.categories (name)
VALUES ('Music');
INSERT INTO public.categories (name)
VALUES ('Grocery and Gourmet Food');
INSERT INTO public.categories (name)
VALUES ('Video Games');
INSERT INTO public.categories (name)
VALUES ('Arts, Crafts and Sewing');
INSERT INTO public.categories (name)
VALUES ('Industrial and Scientific');
INSERT INTO public.categories (name)
VALUES ('Luggage and Travel Gear');
INSERT INTO public.categories (name)
VALUES ('Furniture');
INSERT INTO public.categories (name)
VALUES ('Watches');


-- Sample Customers
INSERT INTO public.customers (first_name, last_name, user_name, password, email, role)
VALUES ('John', 'Doe', 'john_doe', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', 'john.doe@email.com',
        'CUSTOMER');
INSERT INTO public.customers (first_name, last_name, user_name, password, email, role)
VALUES ('Alice', 'Smith', 'alice_smith', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'alice.smith@email.com', 'CUSTOMER');
INSERT INTO public.customers (first_name, last_name, user_name, password, email, role)
VALUES ('Max', 'Ian', 'max_ian', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK', 'maxIan@email.com',
        'CUSTOMER');
INSERT INTO public.customers (first_name, last_name, user_name, password, email, role)
VALUES ('Michael', 'Johnson', 'michaelj', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'michael.johnson@email.com', 'CUSTOMER');
INSERT INTO public.customers (first_name, last_name, user_name, password, email, role)
VALUES ('Emily', 'Brown', 'emilyb', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'emily.brown@email.com', 'CUSTOMER'),
       ('Daniel', 'Wilson', 'dwilson', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'daniel.wilson@email.com', 'CUSTOMER'),
       ('Sarah', 'Taylor', 'saraht', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'sarah.taylor@email.com', 'CUSTOMER'),
       ('Christopher', 'Martinez', 'chrism', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'christopher.martinez@email.com', 'CUSTOMER'),
       ('Amanda', 'Anderson', 'amandaa', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'amanda.anderson@email.com', 'CUSTOMER'),
       ('Matthew', 'Thomas', 'mthomas', '$2a$10$nAB5j9G1c3JHgg7qzhiIXO7cqqr5oJ3LXRNQJKssDUwHXzDGUztNK',
        'matthew.thomas@email.com', 'CUSTOMER');



-- Sample Addresses
INSERT INTO public.addresses (name, zip_code, street, customer_id)
VALUES ('Home', '12345', '123 Main St', 1);
INSERT INTO public.addresses (name, zip_code, street, customer_id)
VALUES ('Work', '67890', '456 Business St', 2);
INSERT INTO public.addresses (name, zip_code, street, customer_id)
VALUES
    ('Work', '34567', '789 Office St', 3),
    ('Home', '90123', '123 Corporate St', 4),
    ('Work', '23456', '567 Professional St', 5),
    ('Work', '78901', '890 Company St', 6),
    ('Work', '45678', '234 Business Park St', 7),
    ('Home', '01234', '345 Corporate Park St', 8),
    ('Work', '56789', '678 Office Park St', 9),
    ('Work', '67890', '456 Business St', 10);


-- Sample Products
INSERT INTO public.products (price, remaining_quantity, name, product_code)
VALUES (199.99, 50, 'Smartphone', 'b90d6f9b-1f62-475b-b57d-c70eaf9c046e');
INSERT INTO public.products (price, remaining_quantity, name, product_code)
VALUES (49.99, 100, 'T-shirt', '846c94aa-b0c3-4ba2-91db-4759f191f643');
INSERT INTO public.products (price, remaining_quantity, name, product_code)
VALUES (299.99, 30, 'Coffee Maker', '160dbcb9-c870-4374-90f9-29315acd4479');
INSERT INTO public.products (price, remaining_quantity, name, product_code)
VALUES (151, 60, 'Acoustic Guitar', 'd4406409-6bab-4417-9d6b-e259a0a64670');
INSERT INTO public.products (price, remaining_quantity, name, product_code)
VALUES (749.90, 15, 'MacBook', 'dc44d646-b022-44df-8ba3-268fcae8d9d0');
INSERT INTO public.products (price, remaining_quantity, name, product_code)
VALUES (549.90, 25, 'Sun Glass', '95c0b8e2-720f-48f0-b0ba-71ab670346f1');
INSERT INTO public.products (price, remaining_quantity, name, product_code)
VALUES (125, 45, 'Dry Cat Food', '2ea89450-8599-4061-91d5-41ea800f681e');

-- Sample Category-Products
INSERT INTO public.product_category_rel (product_id, category_id)
VALUES
    (1, 1),
    (1, 17),
    (2, 2),
    (2, 6),
    (2, 14),
    (3, 3),
    (3, 13),
    (4, 17),
    (4, 1),
    (4, 13),
    (5, 2),
    (6, 11);

-- Sample Carts
INSERT INTO public.carts (customer_id, discount_id, cart_state)
VALUES (1, 1, 'CREATED');
INSERT INTO public.carts (customer_id, discount_id, cart_state)
VALUES (2, 2, 'CREATED');

-- Sample Cart Items
INSERT INTO public.cart_items (product_id, quantity, cart_id)
VALUES (1, 2, 1);
INSERT INTO public.cart_items (product_id, quantity, cart_id)
VALUES (2, 3, 2);

-- Sample Balances
INSERT INTO public.balances (customer_id, amount)
VALUES (1, 500.00);
INSERT INTO public.balances (customer_id, amount)
VALUES
    (2, 1000.00),
    (3, 1000.00),
    (4, 1000.00),
    (5, 1000.00),
    (6, 1000.00),
    (7, 1000.00),
    (8, 1000.00),
    (9, 1000.00),
    (10, 1000.00);

-- Sample Payments
INSERT INTO public.payments (paid_price, payment_method)
VALUES (150.00, 'CREDIT_CARD');
INSERT INTO public.payments (paid_price, payment_method)
VALUES (200.00, 'BUY_NOW_PAY_LATER');

-- Sample Orders
INSERT INTO public.orders (cart_id, paid_price, total_price, customer_id, payment_id)
VALUES (1, 150.00, 199.99, 1, 1);
INSERT INTO public.orders (cart_id, paid_price, total_price, customer_id, payment_id)
VALUES (2, 200.00, 249.97, 2, 2);
