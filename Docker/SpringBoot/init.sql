CREATE DATABASE IF NOT EXISTS `CatalegSpring`;
USE `CatalegSpring`;

SET FOREIGN_KEY_CHECKS = 0;

-- =========================
-- TAULES BASE
-- =========================

DROP TABLE IF EXISTS `categoria`;
CREATE TABLE `categoria` (
  `categoria_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`categoria_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `estudi`;
CREATE TABLE `estudi` (
  `estudi_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`estudi_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `serie`;
CREATE TABLE `serie` (
  `serie_id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `classification` int NOT NULL,
  PRIMARY KEY (`serie_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- USUARIS I PERFILS
-- =========================

DROP TABLE IF EXISTS `usuari`;
CREATE TABLE `usuari` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `perfil`;
CREATE TABLE `perfil` (
  `perfil_id` bigint NOT NULL AUTO_INCREMENT,
  `infantile` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `user` bigint NOT NULL,
  PRIMARY KEY (`perfil_id`),
  KEY (`user`),
  CONSTRAINT FK_perfil_usuari
    FOREIGN KEY (`user`)
    REFERENCES `usuari` (`user_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- VIDEOS
-- =========================

DROP TABLE IF EXISTS `video_cataleg`;
CREATE TABLE `video_cataleg` (
  `id_video_cataleg` bigint NOT NULL,
  `chapter` int NOT NULL,
  `date_emission` date NOT NULL,
  `description` varchar(255) NOT NULL,
  `duration` int NOT NULL,
  `rating` double DEFAULT NULL,
  `season` int DEFAULT NULL,
  `title` varchar(255) NOT NULL,
  `serie_id` bigint DEFAULT NULL,
  `estudi_id` bigint NOT NULL,
  `codec` varchar(255) NOT NULL,
  `pes` bigint NOT NULL,
  `resolucio` varchar(255) NOT NULL,
  PRIMARY KEY (`id_video_cataleg`),
  KEY (`serie_id`),
  KEY (`estudi_id`),
  CONSTRAINT FK_video_serie
    FOREIGN KEY (`serie_id`)
    REFERENCES `serie` (`serie_id`)
    ON DELETE CASCADE,
  CONSTRAINT FK_video_estudi
    FOREIGN KEY (`estudi_id`)
    REFERENCES `estudi` (`estudi_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- RELACIÓ VIDEO - CATEGORIA
-- =========================

DROP TABLE IF EXISTS `video_categoria`;
CREATE TABLE `video_categoria` (
  `id_video_cataleg` bigint NOT NULL,
  `categoria_id` bigint NOT NULL,
  PRIMARY KEY (`id_video_cataleg`,`categoria_id`),
  KEY (`categoria_id`),
  CONSTRAINT FK_video_categoria_video
    FOREIGN KEY (`id_video_cataleg`)
    REFERENCES `video_cataleg` (`id_video_cataleg`)
    ON DELETE CASCADE,
  CONSTRAINT FK_video_categoria_categoria
    FOREIGN KEY (`categoria_id`)
    REFERENCES `categoria` (`categoria_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- HISTORIAL
-- =========================

DROP TABLE IF EXISTS `historial`;
CREATE TABLE `historial` (
  `id_historial` bigint NOT NULL AUTO_INCREMENT,
  `completat` bit(1) NOT NULL,
  `latest_play` int NOT NULL,
  `visualization` int NOT NULL,
  `perfil_id` bigint NOT NULL,
  `usuari_id` bigint NOT NULL,
  `video_id` bigint NOT NULL,
  PRIMARY KEY (`id_historial`),
  UNIQUE KEY (`perfil_id`,`video_id`),
  KEY (`usuari_id`),
  KEY (`video_id`),
  CONSTRAINT FK_historial_video
    FOREIGN KEY (`video_id`)
    REFERENCES `video_cataleg` (`id_video_cataleg`)
    ON DELETE CASCADE,
  CONSTRAINT FK_historial_usuari
    FOREIGN KEY (`usuari_id`)
    REFERENCES `usuari` (`user_id`)
    ON DELETE CASCADE,
  CONSTRAINT FK_historial_perfil
    FOREIGN KEY (`perfil_id`)
    REFERENCES `perfil` (`perfil_id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- =========================
-- VALORACIONS
-- =========================

DROP TABLE IF EXISTS `valoracio_video`;
CREATE TABLE `valoracio_video` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rating` int NOT NULL,
  `perfil_id` bigint NOT NULL,
  `video_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY (`perfil_id`),
  KEY (`video_id`),
  CONSTRAINT FK_valoracio_video
    FOREIGN KEY (`video_id`)
    REFERENCES `video_cataleg` (`id_video_cataleg`)
    ON DELETE CASCADE,
  CONSTRAINT FK_valoracio_perfil
    FOREIGN KEY (`perfil_id`)
    REFERENCES `perfil` (`perfil_id`)
    ON DELETE CASCADE,
  CONSTRAINT chk_rating CHECK (`rating` BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
