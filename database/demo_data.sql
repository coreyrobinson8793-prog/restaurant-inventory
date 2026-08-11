-- demo_data.sql: optional sample data for demonstration.
-- NOT part of the migration sequence.
-- Run after schema.sql and migrations 001-004, against empty tables.
-- Requires at least one user (see app/RegisterTestUser.java) — POs reference userID 1.
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: restaurant_inventory
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `csv_import_templates`
--

LOCK TABLES `csv_import_templates` WRITE;
/*!40000 ALTER TABLE `csv_import_templates` DISABLE KEYS */;
INSERT INTO `csv_import_templates` VALUES (3,1,'Product Number','Product Price',1,1,'2026-07-21 02:22:36');
/*!40000 ALTER TABLE `csv_import_templates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `item_suppliers`
--

LOCK TABLES `item_suppliers` WRITE;
/*!40000 ALTER TABLE `item_suppliers` DISABLE KEYS */;
INSERT INTO `item_suppliers` VALUES (10,7,1,'8857013',61.90,1.00,5,0,1,'2026-08-11 19:45:28'),(11,8,1,'1264150',49.62,1.00,5,0,1,'2026-08-11 19:05:24'),(12,9,1,'2721165',63.22,40.00,1,0,1,'2026-08-11 15:47:52'),(13,10,1,'2725042',69.89,40.00,1,0,1,'2026-08-11 19:05:24'),(14,11,1,'9781246',76.45,15.00,1,1,1,'2026-08-11 18:47:18'),(15,12,1,'7205727',54.11,4.00,6,0,1,'2026-08-11 19:45:28'),(16,15,1,'8326696',34.90,50.00,1,0,1,'2026-08-11 19:05:24'),(17,14,1,'9372855',27.96,20.00,1,0,1,'2026-08-11 18:52:52'),(18,13,1,'5326426',37.68,24.00,6,0,1,'2026-08-11 18:53:44'),(19,16,1,'8327041',54.35,1.00,5,1,1,'2026-08-11 19:05:24'),(20,18,1,'9650802',52.59,1.00,5,0,1,'2026-08-11 19:05:24'),(21,17,1,'1328699',39.89,1.00,6,0,1,'2026-08-11 19:05:24'),(22,7,2,'PFG-4412',65.25,1.00,5,0,1,'2026-08-11 19:01:03'),(23,10,2,'PFG-7730',71.50,40.00,1,0,1,'2026-08-11 19:01:30'),(24,16,2,'PFG-2280',51.33,1.00,5,0,1,'2026-08-11 19:01:56'),(25,18,2,'PFG-9004',47.80,1.00,5,0,1,'2026-08-11 19:02:20');
/*!40000 ALTER TABLE `item_suppliers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (7,'Burger Patty 8oz',5,4.00,10.00,1,2),(8,'Brioche Bun',5,3.00,8.00,1,9),(9,'Chicken Breast',1,80.00,80.00,1,3),(10,'Chicken Wings',1,40.00,120.00,1,3),(11,'Bacon',1,10.00,15.00,1,4),(12,'Shredded Cheddar Jack',6,3.00,4.00,1,8),(13,'Iceberg',6,6.00,24.00,1,6),(14,'Tomatoes',1,20.00,20.00,1,6),(15,'Yellow Onions',1,25.00,50.00,1,6),(16,'French Fries',5,3.00,7.00,1,11),(17,'Fryer Oil',6,6.00,6.00,1,10),(18,'Nitrile Glove L',5,1.00,2.00,1,13);
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `purchase_order_items`
--

LOCK TABLES `purchase_order_items` WRITE;
/*!40000 ALTER TABLE `purchase_order_items` DISABLE KEYS */;
INSERT INTO `purchase_order_items` VALUES (19,13,7,6.00,61.90),(20,13,8,5.00,49.62),(22,13,10,2.00,69.89),(23,13,11,1.00,76.45),(24,13,12,1.00,54.11),(25,13,13,1.00,37.68),(26,13,15,1.00,34.90),(27,13,16,4.00,54.35),(28,13,18,1.00,52.59),(29,14,7,5.00,65.25),(30,14,18,2.00,47.80),(31,14,16,1.00,51.33),(32,14,10,2.00,71.50),(33,15,7,4.00,61.90),(34,15,14,2.00,27.96),(35,15,18,3.00,52.59),(36,15,10,4.00,69.89),(37,15,7,4.00,61.90),(38,15,18,2.00,52.59),(39,16,7,4.00,65.25),(40,16,10,2.00,71.50),(41,16,18,2.00,47.80);
/*!40000 ALTER TABLE `purchase_order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `purchase_orders`
--

LOCK TABLES `purchase_orders` WRITE;
/*!40000 ALTER TABLE `purchase_orders` DISABLE KEYS */;
INSERT INTO `purchase_orders` VALUES (13,1,1,'2026-08-11 19:50:25','RECEIVED',1232.41,'Demo PO'),(14,2,1,'2026-08-11 19:55:26','RECEIVED',616.18,'PFG Demo'),(15,1,1,'2026-08-11 19:56:35','OPEN',1093.63,'OPEN Demo'),(16,2,1,'2026-08-11 19:57:05','CANCELLED',499.60,'CANCEL PFG Demo');
/*!40000 ALTER TABLE `purchase_orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `suppliers`
--

LOCK TABLES `suppliers` WRITE;
/*!40000 ALTER TABLE `suppliers` DISABLE KEYS */;
INSERT INTO `suppliers` VALUES (1,'U.S. Foods','Travis','555-555-5555','usfood@test.com','123 Test Street Aiken, SC 29801',1),(2,'Performance Food Group','Kyle ','444-444-4444','pfg@test.com','456 Test Drive Aiken, SC 29801',1),(3,'Sysco','John Doe','5555555555','tes@gmail.com','123 Test Dr. Aiken, SC',1);
/*!40000 ALTER TABLE `suppliers` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-11 12:24:59
