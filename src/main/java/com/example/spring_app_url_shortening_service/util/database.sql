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

-- Persistent Login Table
CREATE TABLE persistent_logins (
                                   username VARCHAR(64) NOT NULL,
                                   series VARCHAR(64) PRIMARY KEY,
                                   token VARCHAR(64) NOT NULL,
                                   last_used TIMESTAMP NOT NULL
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

CREATE TABLE persistent_logins (
                                   username VARCHAR(64) NOT NULL,
                                   series VARCHAR(64) PRIMARY KEY,
                                   token VARCHAR(64) NOT NULL,
                                   last_used TIMESTAMP NOT NULL
);

CREATE TABLE urls (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      original_url VARCHAR(2048) NOT NULL,
                      alias VARCHAR(255) NOT NULL UNIQUE,
                      expiration_date DATETIME,
                      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                      is_active BOOLEAN DEFAULT TRUE
);

CREATE TABLE user_urls (
                           user_id BIGINT NOT NULL,
                           url_id BIGINT NOT NULL,
                           PRIMARY KEY (user_id, url_id),
                           FOREIGN KEY (user_id) REFERENCES users(id),
                           FOREIGN KEY (url_id) REFERENCES urls(id)
);

ALTER TABLE users ADD COLUMN api_key VARCHAR(255) UNIQUE;
ALTER TABLE urls MODIFY original_url VARCHAR(2048) NOT NULL;
ALTER TABLE urls MODIFY alias VARCHAR(255) NOT NULL UNIQUE;
CREATE INDEX idx_expiration ON urls(expiration_date);