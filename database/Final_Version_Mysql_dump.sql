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
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredient`
--

LOCK TABLES `ingredient` WRITE;
/*!40000 ALTER TABLE `ingredient` DISABLE KEYS */;
INSERT INTO `ingredient` VALUES (1,'Flour'),(2,'Egg'),(3,'Sugar'),(4,'Onion'),(5,'Garlic'),(6,'Tomato'),(7,'Potato'),(8,'Carrot'),(9,'Chicken Breast'),(10,'Beef Steak'),(11,'Pork Belly'),(12,'Salmon Fillet'),(13,'Shrimp'),(14,'Olive Oil'),(15,'Butter'),(16,'Salt'),(17,'Black Pepper'),(18,'Soy Sauce'),(19,'Rice'),(20,'Pasta'),(21,'Cheese'),(22,'Milk'),(23,'Lemon'),(24,'TestIngredient01'),(25,'Testingredient02'),(26,'Testingredient03'),(27,'Testingredient04'),(28,'Testingredient05');
/*!40000 ALTER TABLE `ingredient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `instruction`
--

DROP TABLE IF EXISTS `instruction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `instruction` (
  `stepNumber` int NOT NULL,
  `recipe_id` int NOT NULL,
  `description` varchar(255) NOT NULL,
  PRIMARY KEY (`stepNumber`,`recipe_id`),
  KEY `recipe_id` (`recipe_id`),
  CONSTRAINT `instruction_ibfk_1` FOREIGN KEY (`recipe_id`) REFERENCES `recipe` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `instruction`
--

LOCK TABLES `instruction` WRITE;
/*!40000 ALTER TABLE `instruction` DISABLE KEYS */;
INSERT INTO `instruction` VALUES (1,1,'Mix all ingredients'),(1,2,'Apply dry rub to ribs.'),(1,3,'Fry potatoes until golden brown.'),(1,4,'Marinate pork in spices.'),(1,5,'Cook pasta until al dente.'),(1,6,'Sear beef until browned.'),(1,7,'Pound the veal cutlet thin.'),(1,8,'Stir-fry chicken with peanuts and chili peppers.'),(1,9,'Prepare sushi rice.'),(1,10,'Stir-fry rice noodles with shrimp, tofu, and bean sprouts.'),(1,11,'Cut raw fish into small pieces.'),(1,12,'Soak black beans overnight.'),(1,13,'Prepare beef filling with spices.'),(1,14,'Create a flavorful tomato and pepper base.'),(1,15,'Brown chicken and onions in the tagine pot.'),(1,16,'Cook spiced minced meat with chutney.'),(1,17,'Whip egg whites to form stiff peaks and bake.'),(1,18,'Fill pastry with meat and gravy mixture.'),(1,19,'Cut butter cake into squares.'),(1,20,'Blend chickpeas, tahini, garlic, and lemon juice until smooth.'),(1,21,'Marinate chicken in yogurt and spices.'),(1,22,'Form ground chickpeas into balls.'),(2,1,'Bake at 180°C for 20 minutes'),(2,2,'Slow cook for 4 hours until tender.'),(2,3,'Top with cheese curds and hot gravy.'),(2,4,'Cook on a vertical rotisserie and shave off slices.'),(2,5,'Whisk eggs and cheese in a bowl.'),(2,6,'Sauté vegetables, add wine and beef, and simmer for 3 hours.'),(2,7,'Coat with flour, egg, and breadcrumbs, then fry until golden.'),(2,8,'Add Kung Pao sauce and vegetables.'),(2,9,'Slice fresh fish and assemble nigiri and maki rolls.'),(2,10,'Add Pad Thai sauce and top with crushed peanuts.'),(2,11,'Marinate in lime and lemon juice for 15 minutes.'),(2,12,'Simmer beans with various cuts of pork and beef for several hours.'),(2,13,'Fill dough circles and bake until golden.'),(2,14,'Cook rice in the base until all liquid is absorbed.'),(2,15,'Add spices, apricots, and broth, then simmer slowly.'),(2,16,'Top with a savory egg custard and bake.'),(2,17,'Top cooled meringue with whipped cream and fresh fruit.'),(2,18,'Bake until pastry is golden and flaky.'),(2,19,'Dip each square in chocolate icing, then roll in desiccated coconut.'),(2,20,'Serve drizzled with olive oil.'),(2,21,'Grill on a skewer and serve in warm pita bread with sauce.'),(2,22,'Deep-fry until golden brown and crispy.'),(3,2,'Brush with BBQ sauce and grill for 10 minutes.'),(3,4,'Serve on small tortillas with pineapple and onion.'),(3,5,'Combine hot pasta with egg mixture and pancetta.'),(3,11,'Mix with chopped onion, cilantro, and chili.');
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
  `unit` varchar(15) DEFAULT NULL,
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
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `servings` int DEFAULT NULL,
  `imagePath` varchar(255) DEFAULT NULL,
  `region_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `region_id` (`region_id`),
  CONSTRAINT `recipe_ibfk_1` FOREIGN KEY (`region_id`) REFERENCES `region` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipe`
--

LOCK TABLES `recipe` WRITE;
/*!40000 ALTER TABLE `recipe` DISABLE KEYS */;
INSERT INTO `recipe` VALUES (1,'Vanilla Cake','A classic vanilla cake recipe',1,'/groupf/recipeapp/images/Vanilla_Cake_image.png',1),(2,'BBQ Ribs','Smoky and tender barbecue ribs.',1,'/groupf/recipeapp/images/BBQ_Ribs_image.png',1),(3,'Poutine','Classic Canadian dish with fries, cheese curds, and gravy.',1,'/groupf/recipeapp/images/Poutine_image.png',1),(4,'Tacos al Pastor','Mexican shawarma-style pork tacos.',1,'/groupf/recipeapp/images/Tacos_al_Pastor_image.png',1),(5,'Spaghetti Carbonara','Classic Italian pasta with eggs, cheese, and pancetta.',1,'/groupf/recipeapp/images/Spaghetti_Carbonara_image.png',2),(6,'Beef Bourguignon','A French beef stew braised in red wine.',1,'/groupf/recipeapp/images/Beef_Bourguignon_image.png',2),(7,'Wiener Schnitzel','A classic Austrian dish of breaded and fried veal cutlet.',1,'/groupf/recipeapp/images/Wiener_Schnitzel_image_1751635796256.png',2),(8,'Kung Pao Chicken','A spicy, stir-fried Chinese chicken dish.',1,'/groupf/recipeapp/images/Kung_Pao_Chicken_image.png',3),(9,'Sushi Platter','A selection of fresh Japanese sushi.',1,'/groupf/recipeapp/images/Sushi_Platter_image.png',3),(10,'Pad Thai','A common stir-fried rice noodle dish from Thailand.',1,'/groupf/recipeapp/images/Pad_Thai_image_1751635822036.png',3),(11,'Ceviche','Peruvian dish of fresh raw fish cured in citrus juices.',1,'/groupf/recipeapp/images/Ceviche_image_1751635837757.png',4),(12,'Feijoada','A rich stew of black beans with beef and pork from Brazil.',1,'/groupf/recipeapp/images/Feijoada_image.png',4),(13,'Argentinian Empanadas','Baked pastries filled with beef.',1,'/groupf/recipeapp/images/Argentinian_Empanadas_image.png',4),(14,'Jollof Rice','A popular West African one-pot rice dish.',1,'/groupf/recipeapp/images/Jollof_Rice_image_1751635853940.png',5),(15,'Chicken Tagine','A flavorful Moroccan stew cooked in a special pot.',1,'/groupf/recipeapp/images/Chicken_Tagine_image.png',5),(16,'Bobotie','South African dish of spiced minced meat with a creamy topping.',1,'/groupf/recipeapp/images/Bobotie_image.png',5),(17,'Pavlova','A meringue-based dessert with a crisp crust and soft, light inside.',1,'/groupf/recipeapp/images/Pavlova_image.png',6),(18,'Australian Meat Pie','A hand-sized pie containing diced or minced meat and gravy.',1,'/groupf/recipeapp/images/Australian_Meat_Pie_image.png',6),(19,'Lamingtons','Australian cakes made from squares of butter cake coated in chocolate and coconut.',1,'/groupf/recipeapp/images/Lamingtons_image.png',6),(20,'Hummus','A creamy dip made from chickpeas, tahini, lemon, and garlic.',1,'/groupf/recipeapp/images/Hummus_image.png',7),(21,'Chicken Shawarma','Marinated chicken, grilled and served in a pita.',1,'/groupf/recipeapp/images/Chicken_Shawarma_image.png',7),(22,'Falafel','Deep-fried balls made from ground chickpeas.',1,'/groupf/recipeapp/images/Falafel_image.png',7);
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

-- Dump completed on 2025-07-04 15:33:49
