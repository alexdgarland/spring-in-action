
INSERT INTO ingredient (ingredient_id, ingredient_name, ingredient_type)
    VALUES
        ('FLTO', 'Flour Tortilla', 'WRAP'),
        ('COTO', 'Corn Tortilla', 'WRAP'),
        ('GRBF', 'Ground Beef', 'PROTEIN'),
        ('CARN', 'Carnitas', 'PROTEIN'),
        ('TMTO', 'Diced Tomatoes', 'VEGGIES'),
        ('LETC', 'Lettuce', 'VEGGIES'),
        ('CHED', 'Cheddar', 'CHEESE'),
        ('JACK', 'Monterrey Jack', 'CHEESE'),
        ('SLSA', 'Salsa', 'SAUCE'),
        ('SRCR', 'Sour Cream', 'SAUCE')
    ON CONFLICT(ingredient_id) DO
        UPDATE SET
            ingredient_name = EXCLUDED.ingredient_name,
            ingredient_type = EXCLUDED.ingredient_type
            ;
