USE CatalegSpring;

-- Borramos datos previos si quieres empezar de cero para evitar errores de duplicados
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE video_categoria;
TRUNCATE TABLE video_cataleg;
TRUNCATE TABLE serie;
TRUNCATE TABLE estudi;
TRUNCATE TABLE categoria;
SET FOREIGN_KEY_CHECKS = 1;

-- =========================
-- ESTUDI
-- =========================
INSERT INTO estudi (estudi_id, name) VALUES 
(1, 'Estudi PixelArts'),
(2, 'DreamFrame Studios'),
(3, 'CineMax Productions');

-- =========================
-- SERIE
-- =========================
INSERT INTO serie (serie_id, name, classification) VALUES 
(1, 'Sèrie Fantàstica', 1),
(2, 'Aventura Espacial', 2),
(3, 'Comedia Urbana', 1);

-- =========================
-- CATEGORIA
-- =========================
INSERT INTO categoria (categoria_id, name) VALUES 
(1, 'Acció'), (2, 'Aventura'), (3, 'Comedia'), 
(4, 'Drama'), (5, 'Ciència Ficció'), (6, 'Animació');

-- =========================
-- VIDEO_CATALEG
-- Orden: id, chapter, date_emission, description, duration, rating, season, title, serie_id, estudi_id
-- =========================
INSERT INTO video_cataleg 
(id_video_cataleg, chapter, date_emission, description, duration, rating, season, title, serie_id, estudi_id)
VALUES
(1, 1, CURDATE(), 'gran_aventura.jpg', 130, 4.8, 1, 'La Gran Aventura', 1, 1),
(2, 1, CURDATE(), 'risc_cosmos.jpg', 145, 4.5, 1, 'Risc al Cosmos', 2, 2),
(3, 1, CURDATE(), 'comedia_ciutat.jpg', 90, 4.2, 1, 'Comèdia a la Ciutat', 3, 3),
(4, 2, CURDATE(), 'misteri_foscor.jpg', 120, 4.9, 1, 'Misteri a la Foscor', 1, 1),
(5, 2, CURDATE(), 'viure_estrelles.jpg', 150, 4.7, 1, 'Viure entre Estrelles', 2, 2),
(6, 2, CURDATE(), 'animacio_magica.jpg', 100, 4.6, 1, 'Animació Màgica', 3, 3),
(7, 3, CURDATE(), 'lluita_final.jpg', 140, 4.8, 2, 'Lluita Final', 1, 1),
(8, 3, CURDATE(), 'missio_impossible.jpg', 155, 4.9, 2, 'Missió Impossible', 2, 2),
(9, 3, CURDATE(), 'rialles_sense_fi.jpg', 95, 4.4, 2, 'Rialles Sense Fi', 3, 3);

-- =========================
-- VIDEO_CATEGORIA
-- =========================
INSERT INTO video_categoria (id_video_cataleg, categoria_id) VALUES 
(1,1),(1,2),(4,1),(4,2),(7,1),(7,2),
(2,2),(2,5),(5,2),(5,5),(8,2),(8,5),
(3,3),(3,6),(6,3),(6,6),(9,3),(9,6),
(4,4);