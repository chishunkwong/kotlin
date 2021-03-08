DROP TABLE IF EXISTS meal_item;

CREATE TABLE meal_item(
    meal_item_id uuid DEFAULT uuid_generate_v4 (),
    description VARCHAR NOT NULL,
    portion_size NUMERIC DEFAULT 1
);
