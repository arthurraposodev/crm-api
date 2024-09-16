CREATE TABLE customers (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           surname VARCHAR(100) NOT NULL,
                           customer_id UUID NOT NULL,  -- Unique identifier for the customer
                           created_by VARCHAR(100),  -- Reference to the user who created the customer
                           updated_by VARCHAR(100),  -- Reference to the user who last updated the customer
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Timestamp of creation
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Timestamp of last update
);
