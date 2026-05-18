
USE appdb;


/* DROP TABLE IF EXISTS `clientes`;*/

CREATE TABLE `clientes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nif` varchar(15) NOT NULL,
  `nome` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




/* LOCK TABLES `clientes` WRITE; */
/*!40000 ALTER TABLE `clientes` DISABLE KEYS */;
/* INSERT INTO `clientes` VALUES (1,'','Rodrigo','abc@sapo.pt','123456789'),(2,'','Luis','abc@lixo.pt','123456'); */

/* UNLOCK TABLES;*/

