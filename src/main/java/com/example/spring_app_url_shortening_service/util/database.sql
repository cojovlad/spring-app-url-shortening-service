CREATE DATABASE url_shortener;
USE url_shortener;

-- Users Table
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       profile_picture_url VARCHAR(255),
                       role_id BIGINT NOT NULL,
                       language_id BIGINT,
                       is_active BOOLEAN DEFAULT TRUE,
                       FOREIGN KEY (role_id) REFERENCES roles(id),
                       FOREIGN KEY (language_id) REFERENCES languages(id)
);

-- Roles Table
CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE
);

-- Languages Table
CREATE TABLE languages (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           language_code VARCHAR(10) NOT NULL UNIQUE
);

-- Insert Roles
INSERT INTO roles (name) VALUES ('USER'), ('ADMIN');

-- Insert Languages
INSERT INTO languages (language_code) VALUES ('en'), ('fr'), ('de');

-- Insert Users
INSERT INTO users (email, password, role_id, language_id, is_active)
VALUES
    ('user1@example.com', '{noop}123', 1, 1, TRUE),
    ('admin@example.com', '{noop}123', 2, 2, TRUE),
    ('user2@example.com', '{noop}123', NULL, 1, 3, FALSE);