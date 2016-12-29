# --- !Ups

CREATE TABLE `categories` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `category_id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `category_name_UNIQUE` (`name` ASC)
);

# --- !Downs

DROP TABLE `categories`;