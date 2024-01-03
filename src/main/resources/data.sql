INSERT INTO public.discount (discount, discount_type, name, minimum_amount) VALUES (25.00, 'RATE_BASED', '%25',  150.00);
INSERT INTO public.discount ( discount, discount_type, name,minimum_amount) VALUES (50.00, 'AMOUNT_BASED', '50 dollar', 250.00);

-- Sample Categories
INSERT INTO public.category (name) VALUES ('Electronics');
INSERT INTO public.category (name) VALUES ('Clothing');
INSERT INTO public.category (name) VALUES ('Home and Kitchen');

-- Sample Customers
INSERT INTO public.customer (first_name, last_name, user_name, password, email, role) VALUES ('John', 'Doe', 'john_doe', 'password123', 'john.doe@email.com', 'CUSTOMER');
INSERT INTO public.customer (first_name, last_name, user_name, password, email, role) VALUES ('Alice', 'Smith', 'alice_smith', 'securepass', 'alice.smith@email.com', 'CUSTOMER');

-- Sample Addresses
INSERT INTO public.address (name, zip_code, street, customer_id) VALUES ('Home', '12345', '123 Main St', 1);
INSERT INTO public.address (name, zip_code, street, customer_id) VALUES ('Work', '67890', '456 Business St', 2);

-- Sample Products
INSERT INTO public.product (price, quantity, remaining_quantity, name, product_code) VALUES (199.99, 50, 50, 'Smartphone', 'b90d6f9b-1f62-475b-b57d-c70eaf9c046e');
INSERT INTO public.product (price, quantity, remaining_quantity, name, product_code) VALUES (49.99, 100, 100, 'T-shirt', '846c94aa-b0c3-4ba2-91db-4759f191f643');
INSERT INTO public.product (price, quantity, remaining_quantity, name, product_code) VALUES (299.99, 30, 30, 'Coffee Maker', '160dbcb9-c870-4374-90f9-29315acd4479');

-- Sample Carts
INSERT INTO public.cart (customer_id, discount_id, cart_state) VALUES (1, 1, 'CREATED');
INSERT INTO public.cart (customer_id, discount_id, cart_state) VALUES (2, 2, 'CREATED');

-- Sample Cart Items
INSERT INTO public.cart_item (product_id, quantity, cart_id) VALUES (1, 2, 1);
INSERT INTO public.cart_item (product_id, quantity, cart_id) VALUES (2, 3, 2);

-- Sample Balances
INSERT INTO public.balance (customer_id, amount) VALUES (1, 500.00);
INSERT INTO public.balance (customer_id, amount) VALUES (2, 1000.00);

-- Sample Payments
INSERT INTO public.payment (paid_price, payment_method) VALUES (150.00, 'CREDIT_CARD');
INSERT INTO public.payment (paid_price, payment_method) VALUES (200.00, 'BUY_NOW_PAY_LATER');

-- Sample Orders
INSERT INTO public.orders (cart_id, paid_price, total_price, customer_id, payment_id) VALUES (1, 150.00, 199.99, 1, 1);
INSERT INTO public.orders (cart_id, paid_price, total_price, customer_id, payment_id) VALUES (2, 200.00, 249.97, 2, 2);
