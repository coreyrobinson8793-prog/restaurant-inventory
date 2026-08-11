-- migration_004: seed item categories
-- Reference data, not a schema change. Safe to re-run only if
-- categories are empty (categoryName is UNIQUE).
INSERT INTO restaurant_inventory.categories (categoryName, description) VALUES
('Beef', 'Ground beef, patties, steaks, and other beef products'),
('Poultry', 'Chicken, turkey, and other poultry'),
('Pork', 'Bacon, sausage, chops, and other pork products'),
('Seafood', 'Shrimp, fish, shellfish'),
('Produce', 'Fresh fruits and vegetables'),
('Dairy', 'Milk, eggs, butter, sour cream'),
('Cheese', 'All cheese products'),
('Bakery', 'Buns, breads, and rolls'),
('Dry Goods', 'Shelf-stable groceries, sauces, seasonings, oils'),
('Frozen', 'Frozen foods and appetizers'),
('Beverage', 'Fountain syrups, juices, and drinks'),
('Disposables', 'Cups, gloves, napkins, containers, and other supplies');