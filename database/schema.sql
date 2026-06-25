/*
Corey Robinson
Tailgate Tavern Inventory Tables
04/29/26
*/
USE restaurant_inventory;
CREATE TABLE users	/*stores login accounts*/
(
	userID			INT				AUTO_INCREMENT			PRIMARY KEY,
    username		VARCHAR(50)		UNIQUE 					NOT NULL,
    password_hash	VARCHAR(255)							NOT NULL,
    position		ENUM('Manager', 'Staff')				NOT NULL,
    created_at		TIMESTAMP 		DEFAULT CURRENT_TIMESTAMP,
    is_active		BOOLEAN 		DEFAULT	TRUE	/*allows for an employee to be denied access to the application without deleting them*/
);
CREATE TABLE units_of_measurement	/*look-up table for measurements*/
(
	unitID			INT				AUTO_INCREMENT			PRIMARY KEY,
    unitName		VARCHAR(30)		UNIQUE					NOT NULL
);
CREATE TABLE suppliers	/*vendor information*/
(
	supplierID		INT				AUTO_INCREMENT			PRIMARY KEY,
    supplierName	VARCHAR(50)		NOT NULL,
    repName			VARCHAR(50),
    phone			VARCHAR(20),
    email			VARCHAR(100),
    address			VARCHAR(255),
    is_active		BOOLEAN			DEFAULT	TRUE	/*allows for a supplier to not be displayed in the application without deleting them*/
);
CREATE TABLE items	/*inventory*/
(
	itemID			INT				AUTO_INCREMENT			PRIMARY KEY,
    itemName		VARCHAR(50)		NOT NULL,
    unitID			INT				NOT NULL,
    supplierID		INT				NOT NULL,
    currentStock	DECIMAL(10,2)	DEFAULT	0,
    parstock		DECIMAL(10,2)	DEFAULT	0,	/*reorder when item falls below this amount*/
    currentPrice	DECIMAL(10,2)	DEFAULT	0,
    is_active		BOOLEAN			DEFAULT TRUE,	/*allows for an item to not be displayed in the application without deleting it*/
    CONSTRAINT items_fk_unitsOfMeasurement
		FOREIGN KEY (unitID) REFERENCES units_of_measurement (unitID),
	CONSTRAINT items_fk_suppliers
		FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID)
);
CREATE TABLE price_history	/*tracks every price change*/
(
	priceHistoryID	INT				AUTO_INCREMENT			PRIMARY KEY,
    itemID			INT				NOT NULL,
    price			DECIMAL(10,2)	NOT NULL,
    changed_at		TIMESTAMP		DEFAULT CURRENT_TIMESTAMP,
    changed_by		INT				NOT NULL,
	CONSTRAINT priceHistory_fk_items
		FOREIGN KEY (itemID) REFERENCES items (itemID),
	CONSTRAINT priceHistory_fk_users
		FOREIGN KEY (changed_by) REFERENCES users (userID)
);
CREATE TABLE stock_transactions	/*tracks every quantity change*/
(
	transactionID	INT				AUTO_INCREMENT			PRIMARY KEY,
    itemID			INT				NOT NULL,
    quantity_change	DECIMAL(10,2)	NOT NULL,
    trans_type		ENUM('Received', 'Used', 'Adjustment')	NOT NULL,
    trans_date		TIMESTAMP		DEFAULT CURRENT_TIMESTAMP,
    changed_by		INT				NOT NULL,
    notes			VARCHAR(255),
    CONSTRAINT stockTransactions_fk_items
		FOREIGN KEY (itemID) REFERENCES items (itemID),
	CONSTRAINT stockTransactions_fk_users
		FOREIGN KEY (changed_by) REFERENCES users (userID)
);
CREATE TABLE audit_log	/*tracks all data changes*/
(
	auditID			INT				AUTO_INCREMENT			PRIMARY KEY,
    changed_by		INT				NOT NULL,
    action_taken	ENUM('Created', 'Updated', 'Deleted')	NOT NULL,
    table_affected	VARCHAR(50)		NOT NULL,
    recordID		INT				NOT NULL,
    details			TEXT,
    audit_date		TIMESTAMP		DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT auditLog_fk_user
		FOREIGN KEY (changed_by) REFERENCES users (userID)
);
CREATE TABLE purchase_orders	/*header info for each PO*/
(
	poID			INT				AUTO_INCREMENT			PRIMARY KEY,
    supplierID		INT				NOT NULL,
    created_by		INT				NOT NULL,
    created_date	TIMESTAMP		DEFAULT CURRENT_TIMESTAMP,
    current_status	ENUM('Draft', 'Finalized', 'Sent')	DEFAULT 'Draft' NOT NULL,
    total			DECIMAL(10,2)	DEFAULT 0				NOT NULL,
    notes			TEXT,
    CONSTRAINT purchaseOrders_fk_suppliers
		FOREIGN KEY (supplierID) REFERENCES suppliers (supplierID),
	CONSTRAINT purchaseOrders_fk_users
		FOREIGN KEY (created_by) REFERENCES users (userID)
	);
CREATE TABLE purchase_order_items	/*line items in a PO*/
(
	po_itemID		INT				AUTO_INCREMENT			PRIMARY KEY,
    poID			INT				NOT NULL,	/*parent purchase order*/
    itemID			INT				NOT NULL,
    quantityOrdered	DECIMAL(10,2)	NOT NULL,
    price_at_time	DECIMAL(10,2)	NOT NULL,
    CONSTRAINT purchaseOrderItems_fk_purchaseOrders
		FOREIGN KEY (poID) REFERENCES purchase_orders (poID),
	CONSTRAINT purchaseOrderItems_fk_items
		FOREIGN KEY (itemID) REFERENCES items (itemID)
	);
    INSERT IGNORE INTO units_of_measurement (unitName) VALUES
		('lbs'), ('oz'), ('gallons'), ('count'), ('cases'), ('each'), ('grams'),
        ('liters'), ('fluid oz'), ('bottles'), ('cans');