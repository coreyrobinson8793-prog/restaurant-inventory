/*
AUTHOR: Corey Robinson
PURPOSE: Adding lockout field to user table
DATE: 05/03/2026
*/
USE restaurant_inventory;
ALTER TABLE users
ADD COLUMN 		failed_login_attempts	INT			DEFAULT 0		NOT NULL,
ADD COLUMN		locked_until			TIMESTAMP					NULL;