CREATE TABLE `imdb_users` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(256) UNIQUE NOT NULL,
  `url` VARCHAR(256) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `movie_rating` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `movie_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  `rating` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`movie_id`) references `movies` (`id`),
  FOREIGN KEY (`user_id`) references `imdb_users` (`id`)
);

