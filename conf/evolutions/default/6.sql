# --- !Ups

CREATE TABLE `logs` (
  `id`              INT UNSIGNED           NOT NULL AUTO_INCREMENT,
  `product_type_id` INT UNSIGNED           NOT NULL,
  `category_id`     INT UNSIGNED           NOT NULL,
  `pack_id`         INT UNSIGNED           NOT NULL,
  `action`          ENUM ('add', 'remove') NOT NULL,
  `time`            DATETIME               NOT NULL DEFAULT now(),
  PRIMARY KEY (`id`),
  INDEX `fk_logs_category_idx` (`category_id` ASC),
  INDEX `fk_logs_product_type_idx1` (`product_type_id` ASC),
  INDEX `fk_logs_pack_idx2` (`pack_id` ASC),
  CONSTRAINT `fk_logs_category`
  FOREIGN KEY (`category_id`)
  REFERENCES `categories` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_logs_product_types`
  FOREIGN KEY (`product_type_id`)
  REFERENCES `product_types` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_logs_packs`
  FOREIGN KEY (`pack_id`)
  REFERENCES `packs` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

# --- !Downs

DROP TABLE `logs`;