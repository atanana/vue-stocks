# --- !Ups

ALTER TABLE `product_types`
  ADD COLUMN `category_id` INT UNSIGNED NULL
  AFTER `name`,
  ADD INDEX `fk_product_types_category_idx` (`category_id` ASC);
ALTER TABLE `product_types`
  ADD CONSTRAINT `fk_product_types_category`
FOREIGN KEY (`category_id`)
REFERENCES `categories` (`id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;

# --- !Downs

ALTER TABLE `product_types`
  DROP FOREIGN KEY `fk_product_types_category`,
  DROP COLUMN `catecategory_id`;