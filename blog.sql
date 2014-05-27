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
