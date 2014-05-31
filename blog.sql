CREATE TABLE `entries` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `content` longtext NOT NULL,
  `publishedDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `authorId` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `users` (
  `id` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `pass` varchar(100) NOT NULL,
  `active` tinyint default 0,
  `level` int default 1,
  PRIMARY KEY (`id`)
);

CREATE TABLE `comments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `entry` int(11) DEFAULT NULL,
  `title` varchar(45) DEFAULT NULL,
  `content` longtext,
  `name` varchar(45) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `publishedDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `entry_idx` (`entry`),
  CONSTRAINT `entry` FOREIGN KEY (`entry`) REFERENCES `entries` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);