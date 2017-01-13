# --- !Ups

CREATE TABLE `product_types` (
  `id`   INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `product_type_id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `product_type_name_UNIQUE` (`name` ASC)
);

# --- !Downs

DROP TABLE `product_types`;