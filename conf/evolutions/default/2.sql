# --- !Ups

CREATE TABLE `packs` (
  `id`   INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `pack_id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `pack_name_UNIQUE` (`name` ASC)
);

# --- !Downs

DROP TABLE `packs`;