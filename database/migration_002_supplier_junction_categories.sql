/*
AUTHOR: Corey Robinson
PURPOSE: Adding price comparison, categories for items, and price updating functionality.
DATE: 05/05/2026
*/
USE restaurant_inventory;
ALTER TABLE items DROP FOREIGN KEY items_fk_suppliers;
ALTER TABLE price_history DROP FOREIGN KEY priceHistory_fk_items;
ALTER TABLE items 
	DROP COLUMN supplierID,
    DROP COLUMN currentPrice;
ALTER TABLE price_history DROP COLUMN itemID;
CREATE TABLE categories
(
	categoryID			INT				AUTO_INCREMENT			PRIMARY KEY,
    categoryName		VARCHAR(50)		UNIQUE					NOT NULL,
    description			VARCHAR(255),
    is_active			BOOLEAN			DEFAULT TRUE
);
INSERT INTO categories (categoryName, description) VALUES ('Uncategorized', 'Default category for items without a specific category assignment.');
ALTER TABLE items
	ADD COLUMN categoryID	INT				NOT NULL				DEFAULT 1,
	ADD CONSTRAINT items_fk_categories
		FOREIGN KEY (categoryID) REFERENCES categories (categoryID);
CREATE TABLE item_suppliers
(
	itemSupplierID		INT 			AUTO_INCREMENT			PRIMARY KEY,
    itemID				INT				NOT NULL,
    supplierID			INT				NOT NULL,
    supplier_sku		VARCHAR(50),
    price				DECIMAL(10,2)	NOT NULL				DEFAULT 0,
    pack_size			DECIMAL(10,2)	NOT NULL				DEFAULT 1,
    pack_unit_id		INT 			NOT NULL,
    is_preferred		BOOLEAN			DEFAULT FALSE,
    is_active			BOOLEAN			DEFAULT TRUE,
    last_price_update	TIMESTAMP		DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, /*auto updates current timestamp when any column in this row gets updated*/
	CONSTRAINT itemSuppliers_fk_items
		FOREIGN KEY (itemID) REFERENCES items (itemID),
	CONSTRAINT itemSuppliers_fk_suppliers
		FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID),
	CONSTRAINT itemSuppliers_fk_units
		FOREIGN KEY (pack_unit_id) REFERENCES units_of_measurement (unitID),
	CONSTRAINT itemSuppliers_uq_item_supplier
		UNIQUE (itemID, supplierID)
	);
ALTER TABLE price_history
	ADD COLUMN 	itemSupplierID	INT 		NOT NULL,
	ADD CONSTRAINT priceHistory_fk_itemSuppliers
		FOREIGN KEY (itemSupplierID) REFERENCES item_suppliers (itemSupplierID);
CREATE TABLE csv_import_templates
(
	templateID			INT 			AUTO_INCREMENT			PRIMARY KEY,
    supplierID			INT 			NOT NULL				UNIQUE,
    sku_column_name		VARCHAR(50)		NOT NULL,
    price_column_name	VARCHAR(50)		NOT NULL,
    header_row_number	INT 			NOT NULL				DEFAULT 1,
    created_by			INT 			NOT NULL,
    created_at			TIMESTAMP		DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT csvImportTemplates_fk_suppliers
		FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID),
	CONSTRAINT csvImportTemplates_fk_users
		FOREIGN KEY (created_by) REFERENCES users (userID)
);
CREATE TABLE price_imports
(
	importID			INT 			AUTO_INCREMENT			PRIMARY KEY,
    supplierID			INT 			NOT NULL,
    imported_by			INT				NOT NULL,
    import_date			TIMESTAMP 		DEFAULT CURRENT_TIMESTAMP,
    file_name			VARCHAR(255),
    rows_processed		INT				NOT NULL				DEFAULT 0,
    rows_updated		INT 			NOT NULL				DEFAULT 0,
    rows_skipped		INT 			NOT NULL				DEFAULT 0,
    notes				TEXT,
    CONSTRAINT priceImports_fk_suppliers
		FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID),
	CONSTRAINT priceImports_fk_users
		FOREIGN KEY (imported_by) REFERENCES users (userID)
);