CREATE TABLE customers (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           surname VARCHAR(100) NOT NULL,
                           customer_id UUID DEFAULT gen_random_uuid() NOT NULL,  -- Unique identifier for the customer
                           photo_key TEXT,  -- Stores the image key to the uploaded photo (path on S3)
                           created_by VARCHAR(100),  -- Reference to the user who created the customer
                           updated_by VARCHAR(100),  -- Reference to the user who last updated the customer
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp of creation
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Timestamp of last update
);
