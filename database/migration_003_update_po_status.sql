-- migration_003: change purchase order status values to OPEN/RECEIVED/CANCELLED
-- NOTE: assumes purchase_orders is empty. Existing rows with old status
-- values (Draft/Finalized/Sent) would be truncated by this change.
USE restaurant_inventory;
ALTER TABLE purchase_orders 
MODIFY COLUMN current_status ENUM('OPEN','RECEIVED','CANCELLED') NOT NULL DEFAULT 'OPEN';
