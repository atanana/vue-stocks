# --- !Ups

CREATE TABLE `products` (
  `id`              INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `product_type_id` INT UNSIGNED NOT NULL,
  `category_id`     INT UNSIGNED NOT NULL,
  `pack_id`         INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_products_category_idx` (`category_id` ASC),
  INDEX `fk_products_pack_idx` (`pack_id` ASC),
  INDEX `fk_products_product_type_idx` (`product_type_id` ASC),
  CONSTRAINT `fk_products_category`
  FOREIGN KEY (`category_id`)
  REFERENCES `categories` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_products_pack`
  FOREIGN KEY (`pack_id`)
  REFERENCES `packs` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT,
  CONSTRAINT `fk_products_product_type`
  FOREIGN KEY (`product_type_id`)
  REFERENCES `product_types` (`id`)
    ON DELETE RESTRICT
    ON UPDATE RESTRICT
);

# --- !Downs

DROP TABLE `products`;