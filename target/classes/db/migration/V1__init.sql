CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(50),
    address TEXT,
    avatar_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10,2) NOT NULL,
    category VARCHAR(100),
    rating NUMERIC(2,1),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product_images (
    product_id INTEGER REFERENCES products(id) ON DELETE CASCADE,
    image_url TEXT,
    PRIMARY KEY (product_id, image_url)
);

CREATE TABLE IF NOT EXISTS reviews (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES products(id) ON DELETE CASCADE,
    rating INTEGER NOT NULL,
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS cart_items (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES products(id) ON DELETE CASCADE,
    quantity INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    total_amount NUMERIC(10,2) NOT NULL,
    shipping_address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER REFERENCES orders(id) ON DELETE CASCADE,
    product_id INTEGER REFERENCES products(id) ON DELETE CASCADE,
    price NUMERIC(10,2) NOT NULL,
    quantity INTEGER NOT NULL
);

INSERT INTO users (full_name, email, password, phone, address, avatar_url) VALUES
('Arjun Mehta', 'arjun@urbancart.ai', '$2a$10$E6Nhlp/Sv0HgnIY7M1bRCOvtnxo5jBqFaUG2RglN6E466zvWXTm6.', '9876543210', 'Mumbai, India', NULL),
('Priya Sharma', 'priya@urbancart.ai', '$2a$10$E6Nhlp/Sv0HgnIY7M1bRCOvtnxo5jBqFaUG2RglN6E466zvWXTm6.', '9876500000', 'Bengaluru, India', NULL),
('Rohan Das', 'rohan@urbancart.ai', '$2a$10$E6Nhlp/Sv0HgnIY7M1bRCOvtnxo5jBqFaUG2RglN6E466zvWXTm6.', '9876111111', 'Delhi, India', NULL),
('Nisha Gupta', 'nisha@urbancart.ai', '$2a$10$E6Nhlp/Sv0HgnIY7M1bRCOvtnxo5jBqFaUG2RglN6E466zvWXTm6.', '9876222222', 'Pune, India', NULL),
('Kabir Jain', 'kabir@urbancart.ai', '$2a$10$E6Nhlp/Sv0HgnIY7M1bRCOvtnxo5jBqFaUG2RglN6E466zvWXTm6.', '9876333333', 'Hyderabad, India', NULL);

INSERT INTO products (title, description, price, category, rating) VALUES
('Black Oversized Denim Jacket', 'Oversized charcoal denim with dropped shoulders.', 1499.00, 'Men', 4.6),
('Beige Minimalist Hoodie', 'Soft fleece hoodie in warm beige.', 1299.00, 'Men', 4.4),
('Charcoal Tech Trousers', 'Tapered fit with stretch.', 1599.00, 'Men', 4.5),
('White Luxe Tee', 'Premium cotton crewneck tee.', 799.00, 'Men', 4.2),
('Black Satin Slip Dress', 'Bias-cut satin slip dress.', 1899.00, 'Women', 4.7),
('Beige Linen Co-ord', 'Relaxed linen co-ord set.', 2199.00, 'Women', 4.5),
('Charcoal Ribbed Tank', 'Ribbed cotton tank top.', 699.00, 'Women', 4.1),
('Matte Black Duffle Bag', 'Weekender bag with matte finish.', 2499.00, 'Accessories', 4.3),
('Beige Chunky Sneakers', 'Chunky sole sneakers with beige upper.', 2799.00, 'Accessories', 4.4),
('Charcoal Baseball Cap', 'Structured cap in charcoal.', 499.00, 'Accessories', 4.0);

INSERT INTO product_images (product_id, image_url) VALUES
(1, 'https://images.unsplash.com/photo-1521572267360-ee0c2909d518'),
(1, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab'),
(2, 'https://images.unsplash.com/photo-1521572267360-ee0c2909d518?beige'),
(3, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?trousers'),
(4, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?tee'),
(5, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?slip'),
(6, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?linen'),
(7, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?tank'),
(8, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?bag'),
(9, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?sneakers'),
(10, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?cap');

INSERT INTO reviews (user_id, product_id, rating, comment) VALUES
(1, 1, 5, 'Perfect oversized fit and premium feel'),
(2, 5, 4, 'Loved the fabric and drape'),
(3, 2, 4, 'Warm and comfy hoodie'),
(4, 9, 5, 'Sneakers are super comfy'),
(5, 8, 4, 'Great duffle for weekend trips');

INSERT INTO orders (user_id, total_amount) VALUES
(1, 2998.00),
(2, 1899.00);

INSERT INTO order_items (order_id, product_id, price, quantity) VALUES
(1, 1, 1499.00, 1),
(1, 2, 1299.00, 1),
(2, 5, 1899.00, 1);
