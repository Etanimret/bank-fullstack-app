CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    citizen_id VARCHAR(13) NOT NULL UNIQUE,
    gender VARCHAR(20) NOT NULL,
    account_holder_name_th VARCHAR(50) NOT NULL,
    account_holder_name_en VARCHAR(50) NOT NULL,
    pin VARCHAR(6) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    account_number VARCHAR(7) NOT NULL UNIQUE,
    customer_id INTEGER NOT NULL UNIQUE REFERENCES customers(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE statements (
    id SERIAL PRIMARY KEY,
    account_id INTEGER NOT NULL REFERENCES accounts(id),
    code VARCHAR(2) NOT NULL,
    channel VARCHAR(3) NOT NULL,
    amount DECIMAL(14, 2) NOT NULL,
    balance DECIMAL(15, 2) NOT NULL,
    remarks VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE SEQUENCE account_number_seq START 1 INCREMENT 1;
