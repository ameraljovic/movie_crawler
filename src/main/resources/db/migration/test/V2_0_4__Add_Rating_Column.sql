ALTER TABLE `movie_rating` DROP COLUMN `rating`;
ALTER TABLE `movie_rating` ADD COLUMN `rating` INT(11) NOT NULL;