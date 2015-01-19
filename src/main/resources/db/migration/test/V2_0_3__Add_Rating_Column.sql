DROP TABLE `movie_rating`;

CREATE TABLE `movie_rating` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `movie_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  `rating` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`movie_id`) references `movies` (`id`),
  FOREIGN KEY (`user_id`) references `imdb_users` (`id`)
);