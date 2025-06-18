-- MySQL dump 10.13  Distrib 5.7.24, for osx11.1 (x86_64)
--
-- Host: 127.0.0.1    Database: refactored_se2
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ingredient`
--

DROP TABLE IF EXISTS `ingredient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ingredient` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredient`
--

LOCK TABLES `ingredient` WRITE;
/*!40000 ALTER TABLE `ingredient` DISABLE KEYS */;
INSERT INTO `ingredient` VALUES (1,'Flour'),(2,'Egg'),(3,'Sugar'),(4,'Onion'),(5,'Garlic'),(6,'Tomato'),(7,'Potato'),(8,'Carrot'),(9,'Chicken Breast'),(10,'Beef Steak'),(11,'Pork Belly'),(12,'Salmon Fillet'),(13,'Shrimp'),(14,'Olive Oil'),(15,'Butter'),(16,'Salt'),(17,'Black Pepper'),(18,'Soy Sauce'),(19,'Rice'),(20,'Pasta'),(21,'Cheese'),(22,'Milk'),(23,'Lemon');
/*!40000 ALTER TABLE `ingredient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instruction`
--

DROP TABLE IF EXISTS `instruction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instruction` (
  `stepNumber` int NOT NULL AUTO_INCREMENT,
  `recipe_id` int NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`stepNumber`,`recipe_id`),
  KEY `recipe_id` (`recipe_id`),
  CONSTRAINT `instruction_ibfk_1` FOREIGN KEY (`recipe_id`) REFERENCES `recipe` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instruction`
--

LOCK TABLES `instruction` WRITE;
/*!40000 ALTER TABLE `instruction` DISABLE KEYS */;
INSERT INTO `instruction` VALUES (1,1,'Mix all ingredients'),(2,1,'Bake at 180°C for 20 minutes'),(3,2,'Apply dry rub to ribs.'),(4,2,'Slow cook for 4 hours until tender.'),(5,2,'Brush with BBQ sauce and grill for 10 minutes.'),(6,3,'Fry potatoes until golden brown.'),(7,3,'Top with cheese curds and hot gravy.'),(8,4,'Marinate pork in spices.'),(9,4,'Cook on a vertical rotisserie and shave off slices.'),(10,4,'Serve on small tortillas with pineapple and onion.'),(11,5,'Cook pasta until al dente.'),(12,5,'Whisk eggs and cheese in a bowl.'),(13,5,'Combine hot pasta with egg mixture and pancetta.'),(14,6,'Sear beef until browned.'),(15,6,'Sauté vegetables, add wine and beef, and simmer for 3 hours.'),(16,7,'Pound the veal cutlet thin.'),(17,7,'Coat with flour, egg, and breadcrumbs, then fry until golden.'),(18,8,'Stir-fry chicken with peanuts and chili peppers.'),(19,8,'Add Kung Pao sauce and vegetables.'),(20,9,'Prepare sushi rice.'),(21,9,'Slice fresh fish and assemble nigiri and maki rolls.'),(22,10,'Stir-fry rice noodles with shrimp, tofu, and bean sprouts.'),(23,10,'Add Pad Thai sauce and top with crushed peanuts.'),(24,11,'Cut raw fish into small pieces.'),(25,11,'Marinate in lime and lemon juice for 15 minutes.'),(26,11,'Mix with chopped onion, cilantro, and chili.'),(27,12,'Soak black beans overnight.'),(28,12,'Simmer beans with various cuts of pork and beef for several hours.'),(29,13,'Prepare beef filling with spices.'),(30,13,'Fill dough circles and bake until golden.'),(31,14,'Create a flavorful tomato and pepper base.'),(32,14,'Cook rice in the base until all liquid is absorbed.'),(33,15,'Brown chicken and onions in the tagine pot.'),(34,15,'Add spices, apricots, and broth, then simmer slowly.'),(35,16,'Cook spiced minced meat with chutney.'),(36,16,'Top with a savory egg custard and bake.'),(37,17,'Whip egg whites to form stiff peaks and bake.'),(38,17,'Top cooled meringue with whipped cream and fresh fruit.'),(39,18,'Fill pastry with meat and gravy mixture.'),(40,18,'Bake until pastry is golden and flaky.'),(41,19,'Cut butter cake into squares.'),(42,19,'Dip each square in chocolate icing, then roll in desiccated coconut.'),(43,20,'Blend chickpeas, tahini, garlic, and lemon juice until smooth.'),(44,20,'Serve drizzled with olive oil.'),(45,21,'Marinate chicken in yogurt and spices.'),(46,21,'Grill on a skewer and serve in warm pita bread with sauce.'),(47,22,'Form ground chickpeas into balls.'),(48,22,'Deep-fry until golden brown and crispy.');
/*!40000 ALTER TABLE `instruction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instructionentry`
--

DROP TABLE IF EXISTS `instructionentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instructionentry` (
  `recipe_id` int NOT NULL,
  `ingredient_id` int NOT NULL,
  `quantity` double DEFAULT NULL,
  `unit` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`recipe_id`,`ingredient_id`),
  KEY `ingredient_id` (`ingredient_id`),
  CONSTRAINT `instructionentry_ibfk_1` FOREIGN KEY (`recipe_id`) REFERENCES `recipe` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `instructionentry_ibfk_2` FOREIGN KEY (`ingredient_id`) REFERENCES `ingredient` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instructionentry`
--

LOCK TABLES `instructionentry` WRITE;
/*!40000 ALTER TABLE `instructionentry` DISABLE KEYS */;
INSERT INTO `instructionentry` VALUES (1,1,200,'g'),(1,2,2,'pcs'),(1,3,100,'g'),(2,5,1,'pc'),(2,8,1000,'g'),(2,14,10,'g'),(2,15,5,'g'),(3,7,500,'g'),(3,21,200,'g'),(4,4,1,'pc'),(4,5,2,'cloves'),(4,8,500,'g'),(5,2,2,'pcs'),(5,15,5,'g'),(5,20,200,'g'),(5,21,50,'g'),(6,4,2,'pcs'),(6,8,1,'pc'),(6,10,1000,'g'),(6,14,10,'g'),(7,1,100,'g'),(7,2,1,'pc'),(7,10,400,'g'),(7,23,1,'pc'),(8,5,3,'cloves'),(8,9,300,'g'),(8,16,50,'ml'),(9,12,150,'g'),(9,18,20,'ml'),(9,19,300,'g'),(10,2,1,'pc'),(10,13,100,'g'),(10,20,150,'g'),(10,23,1,'pc'),(11,4,1,'pc'),(11,12,400,'g'),(11,23,3,'pcs'),(12,4,2,'pcs'),(12,10,500,'g'),(12,11,500,'g'),(13,1,250,'g'),(13,2,1,'pc'),(13,4,1,'pc'),(13,10,300,'g'),(14,4,1,'pc'),(14,6,3,'pcs'),(14,9,200,'g'),(14,19,400,'g'),(15,4,1,'pc'),(15,8,2,'pcs'),(15,9,500,'g'),(15,14,30,'ml'),(16,2,2,'pcs'),(16,4,1,'pc'),(16,10,500,'g'),(16,22,150,'ml'),(17,2,4,'pcs'),(17,3,200,'g'),(18,1,200,'g'),(18,4,1,'pc'),(18,10,250,'g'),(18,15,50,'g'),(19,1,250,'g'),(19,2,2,'pcs'),(19,3,150,'g'),(19,15,100,'g'),(20,5,2,'cloves'),(20,14,50,'ml'),(20,23,1,'pc'),(21,5,3,'cloves'),(21,9,400,'g'),(21,14,40,'ml'),(21,23,1,'pc'),(22,1,50,'g'),(22,4,1,'pc'),(22,5,4,'cloves'),(22,23,1,'pc');
/*!40000 ALTER TABLE `instructionentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipe`
--

DROP TABLE IF EXISTS `recipe`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recipe` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `servings` int DEFAULT NULL,
  `imagePath` varchar(255) DEFAULT NULL,
  `region_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `region_id` (`region_id`),
  CONSTRAINT `recipe_ibfk_1` FOREIGN KEY (`region_id`) REFERENCES `region` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipe`
--

LOCK TABLES `recipe` WRITE;
/*!40000 ALTER TABLE `recipe` DISABLE KEYS */;
INSERT INTO `recipe` VALUES (1,'Vanilla Cake','A classic vanilla cake recipe',4,'/path/to/image.jpg',1),(2,'BBQ Ribs','Smoky and tender barbecue ribs.',4,'/images/bbq_ribs.jpg',1),(3,'Poutine','Classic Canadian dish with fries, cheese curds, and gravy.',2,'/images/poutine.jpg',1),(4,'Tacos al Pastor','Mexican shawarma-style pork tacos.',4,'/images/tacos_pastor.jpg',1),(5,'Spaghetti Carbonara','Classic Italian pasta with eggs, cheese, and pancetta.',2,'/images/carbonara.jpg',2),(6,'Beef Bourguignon','A French beef stew braised in red wine.',6,'/images/bourguignon.jpg',2),(7,'Wiener Schnitzel','A classic Austrian dish of breaded and fried veal cutlet.',2,'/images/schnitzel.jpg',2),(8,'Kung Pao Chicken','A spicy, stir-fried Chinese chicken dish.',3,'/images/kung_pao.jpg',3),(9,'Sushi Platter','A selection of fresh Japanese sushi.',2,'/images/sushi.jpg',3),(10,'Pad Thai','A common stir-fried rice noodle dish from Thailand.',2,'/images/pad_thai.jpg',3),(11,'Ceviche','Peruvian dish of fresh raw fish cured in citrus juices.',2,'/images/ceviche.jpg',4),(12,'Feijoada','A rich stew of black beans with beef and pork from Brazil.',8,'/images/feijoada.jpg',4),(13,'Argentinian Empanadas','Baked pastries filled with beef.',4,'/images/empanadas.jpg',4),(14,'Jollof Rice','A popular West African one-pot rice dish.',6,'/images/jollof_rice.jpg',5),(15,'Chicken Tagine','A flavorful Moroccan stew cooked in a special pot.',4,'/images/tagine.jpg',5),(16,'Bobotie','South African dish of spiced minced meat with a creamy topping.',6,'/images/bobotie.jpg',5),(17,'Pavlova','A meringue-based dessert with a crisp crust and soft, light inside.',8,'/images/pavlova.jpg',6),(18,'Australian Meat Pie','A hand-sized pie containing diced or minced meat and gravy.',1,'/images/meat_pie.jpg',6),(19,'Lamingtons','Australian cakes made from squares of butter cake coated in chocolate and coconut.',12,'/images/lamingtons.jpg',6),(20,'Hummus','A creamy dip made from chickpeas, tahini, lemon, and garlic.',4,'/images/hummus.jpg',7),(21,'Chicken Shawarma','Marinated chicken, grilled and served in a pita.',2,'/images/shawarma.jpg',7),(22,'Falafel','Deep-fried balls made from ground chickpeas.',4,'/images/falafel.jpg',7);
/*!40000 ALTER TABLE `recipe` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `region`
--

DROP TABLE IF EXISTS `region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `region` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `region`
--

LOCK TABLES `region` WRITE;
/*!40000 ALTER TABLE `region` DISABLE KEYS */;
INSERT INTO `region` VALUES (1,'North America','NA'),(2,'Europe','EU'),(3,'Asia','AS'),(4,'South America','SA'),(5,'Africa','AF'),(6,'Oceania','OC'),(7,'Middle East','ME');
/*!40000 ALTER TABLE `region` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-06-17 13:42:17
