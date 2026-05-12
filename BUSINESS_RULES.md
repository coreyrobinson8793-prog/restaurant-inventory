**Author:** Corey Robinson
**Last Updated:** 2026-04-28

2026-04-28 10:57

Tags:

This application is for management of all inventory items in the restaurant. It will use MySQL on a computer in your office as the server for the database. During application installation, a one-time setup process creates the first Manager account. All subsequent users are created by existing Managers. It will implement the following features:

## Authentication
- A login screen to track the following with a username and password for the following roles:
	- ### Manager
		- can manipulate the data in the database and application
		- can create, read, update, and delete records for vendors, inventory items, and pricing through the application interface. The database schema itself is managed separately through migration scripts and not modifiable from the application.
	- ### Staff
		- can log information through the application to keep track of inventory and what to order
		- no direct access to the database or application (cannot manipulate code)
- ### Password Requirements
	- 8 minimum characters 
- ### Account Lockout
	- account locked after 5 failed login attempts
		- duration: 10 minutes
	- failed attempt counter resets on successful login
- ### User Account Management
	- Managers can create, deactivate, and reactivate any user account, including other Managers
	- Users cannot deactivate their own account (self-lockout prevention)
	- There must always be at least one active Manager. The system prevents deactivating the last active Manager.

## Inventory Management
- will provide a GUI (graphical user interface) to access and use the program
- view all inventory items with current quantities
- add new items to the inventory (manager only)
- update quantities by adding the stock received and logging the usage of products
- edit item details like name, unit, and par-stock (manager only)
- delete items from the application and database (manager only)
- Low-stock alert triggered when current stock is strictly below par-stock (e.g., par = 10, current = 9.99 triggers alert; current = 10 does not)
- V1: Inventory quantities updated manually through staff usage logging. 
	- V2 (future): Automated inventory depletion via integration with the restaurant's POS system, mapped through recipe-based ingredient deduction.
- Each item must be assigned to at least one supplier before it can be ordered.
- Each item must belong to a category.
- Items have a pack size (numeric quantity) and pack unit (lbs, oz, count, etc.) used to calculate price-per-unit for fair comparison across suppliers and brands.

## Suppliers
- keeps track of vendors (US Foods, PFG, etc.)
- view, add, edit, or delete suppliers (manager only)
- link items to their suppliers
- Pricing updates are managed manually through bulk-edit screens or CSV import (see Price Import section)
- ### Item-Supplier Relationships
	- One item can be sold by multiple suppliers, and one supplier can sell multiple items (many-to-many relationship)
	- Each item-supplier pairing has its own price, supplier-specific SKU, and active status
	- Manager can mark one supplier as the **preferred supplier** for an item, used as the default when generating purchase orders
	- When a supplier is deactivated, all of its item-supplier relationships are automatically deactivated; if the supplier is reactivated, relationships remain inactive until manually reactivated
	- Pricing is tracked per item-supplier pair, not just per item — the same item from different suppliers can have different prices and different price histories

## Audit Trail
- data logging
- every action that changes data gets logged
	- who logged it
	- what was logged
	- when it was logged with a date and time stamp
	- Category creation, modification, and deactivation
	- Item-supplier relationship creation, modification, and deactivation
	- Preferred supplier changes
	- Bulk price imports (with file name and import statistics)
- V1: Audit log records are retained indefinitely. 
	- V2 (future): Archive records older than 7 years to a separate cold-storage table.

## Reporting
- personal reports for reviewing business purposes
- current stock report 
- usage history over a date range
- items that are below the par-stock
- generates a purchase order for you to reference when ordering from vendors
- **Price comparison report:** for a given category, shows price-per-unit across all items and suppliers, sortable by lowest cost
- **Price change report:** lists all price changes (manual or imported) within a date range
- **Items without preferred supplier:** lists active items that haven't been assigned a preferred supplier

## Purchase Orders 
- Generated based on items below par-stock
- States: Draft → Finalized → Sent (one-way transitions only)
- Draft POs are fully editable by Managers
- Once Finalized or Sent, POs are read-only
- Saved to database for historical reference and cost analysis
- When generating purchase orders for items below par-stock, the system uses the preferred supplier for each item by default
- Manager can override the supplier selection on draft POs — switching an item to a different supplier that carries it
- If an item has no preferred supplier set, the system uses the first active supplier alphabetically and flags the PO for manager review

## Categories
- Items are grouped into categories representing similar product types (e.g., "Shoestring Fries", "Mozzarella Cheese", "Chicken Breast")
- Each item belongs to exactly one category
- Manager can create, edit, and deactivate categories
- Categories cannot be deleted if any items reference them; instead, they are deactivated

## Price Comparison
- **Supplier comparison view:** for any item, manager can view all suppliers that carry it with current price, supplier SKU, and last-updated date
- **Category comparison view:** manager can select a category and view all items in that category across all suppliers with price-per-unit calculated for fair comparison
- Price-per-unit is calculated as `price ÷ pack_size` and displayed alongside the raw price
- Comparison views show the date of the last price update so managers can see how stale the data is

## Price Import (CSV)
- Price imports are restricted to Manager role only
- Managers can bulk-update supplier pricing by uploading a CSV file exported from the supplier's customer portal
- Each supplier has its own column-mapping configuration (which column contains SKU, which contains price, etc.) saved as a reusable template
- On first import for a new supplier, the manager sets the column mapping; subsequent imports reuse the saved mapping
- The import process matches rows by supplier-specific SKU; rows that don't match any existing item-supplier record are reported but not imported
- Successful imports update prices and create entries in the price_history table for each price change
- Import results show a summary: total rows processed, items updated, items skipped (with reasons), and any errors
- All imports are logged in the audit trail with the importing user, supplier, file name, and result counts
