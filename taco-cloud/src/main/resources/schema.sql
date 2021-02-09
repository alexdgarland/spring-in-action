
CREATE TABLE IF NOT EXISTS ingredient (
    ingredient_id       VARCHAR(4) NOT NULL PRIMARY KEY,
    ingredient_name     VARCHAR(25) NOT NULL,
    ingredient_type     VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS taco_design (
    taco_design_id      BIGSERIAL PRIMARY KEY,
    taco_design_name    VARCHAR(50) NOT NULL,
    created_at          TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS taco_design_ingredients (
    taco_design_id      BIGINT NOT NULL PRIMARY KEY,
    ingredient_id       VARCHAR(4) NOT NULL
);

ALTER TABLE taco_design_ingredients
    ADD FOREIGN KEY (taco_design_id) REFERENCES taco_design(taco_design_id);

ALTER TABLE taco_design_ingredients
    ADD FOREIGN KEY (ingredient_id) REFERENCES ingredient(ingredient_id);

CREATE TABLE IF NOT EXISTS taco_order (
    taco_order_id       BIGSERIAL PRIMARY KEY,
    delivery_name       VARCHAR(50) NOT NULL,
    delivery_street     VARCHAR(50) NOT NULL,
    delivery_city       VARCHAR(50) NOT NULL,
    delivery_state      VARCHAR(2) NOT NULL,
    delivery_zip        VARCHAR(10) NOT NULL,
    cc_number           VARCHAR(16) NOT NULL,
    cc_expiration       VARCHAR(5) NOT NULL,
    cc_cvv              VARCHAR(3) NOT NULL,
    placed_at           TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS taco_order_taco_designs (
    taco_order_id       BIGINT NOT NULL,
    taco_design_id      BIGINT NOT NULL
);

ALTER TABLE taco_order_taco_designs
    ADD FOREIGN KEY (taco_order_id) REFERENCES taco_order(taco_order_id);

ALTER TABLE taco_order_taco_designs
    ADD FOREIGN KEY (taco_design_id) REFERENCES taco_design(taco_design_id);