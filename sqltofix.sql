SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema gwk
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema gwk
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gwk` DEFAULT CHARACTER SET utf8 COLLATE utf8_polish_ci;
USE `gwk`;

-- -----------------------------------------------------
-- Table `gwk`.`place`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gwk`.`place`
(
    `id`      BIGINT NOT NULL AUTO_INCREMENT,
    `name`    TEXT   NULL,
    `subname` TEXT   NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_polish_ci;


-- -----------------------------------------------------
-- Table `gwk`.`organizer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gwk`.`organizer`
(
    `id`          BIGINT NOT NULL AUTO_INCREMENT,
    `designation` TEXT   NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_polish_ci;


-- -----------------------------------------------------
-- Table `gwk`.`category`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gwk`.`category`
(
    `id`               BIGINT NOT NULL AUTO_INCREMENT,
    `name`             TEXT   NULL,
    `root_category_id` BIGINT NULL,
    PRIMARY KEY (`id`),
    INDEX `root_category_id_idx` (`root_category_id` ASC),
    CONSTRAINT `root_category_id`
        FOREIGN KEY (`root_category_id`)
            REFERENCES `gwk`.`category` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_polish_ci;


-- -----------------------------------------------------
-- Table `gwk`.`ticket`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gwk`.`ticket`
(
    `id`           BIGINT      NOT NULL,
    `type`         VARCHAR(45) NULL,
    `start_ticket` INT         NULL,
    `end_ticket`   INT         NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_polish_ci;


-- -----------------------------------------------------
-- Table `gwk`.`urls`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gwk`.`urls`
(
    `id`      BIGINT      NOT NULL,
    `www`     VARCHAR(50) NULL,
    `tickets` VARCHAR(45) NULL,
    `fb`      VARCHAR(45) NULL,
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_polish_ci;


-- -----------------------------------------------------
-- Table `gwk`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gwk`.`event`
(
    `id`           BIGINT NOT NULL,
    `name`         TEXT   NULL,
    `end_date`     DATE   NULL,
    `start_date`   DATE   NULL,
    `active`       INT    NULL,
    `desc_long`    TEXT   NULL,
    `desc_short`   TEXT   NULL,
    `ticket_id`    BIGINT NULL,
    `urls_id`      BIGINT NULL,
    `attachments`  TEXT   NULL,
    `place_id`     BIGINT NULL,
    `organizer_id` BIGINT NULL,
    `category_id`  BIGINT NULL,
    PRIMARY KEY (`id`),
    INDEX `place_id_idx` (`place_id` ASC),
    INDEX `organizer_id_idx` (`organizer_id` ASC),
    INDEX `category_id_idx` (`category_id` ASC),
    INDEX `ticket_id_idx` (`ticket_id` ASC),
    INDEX `urls_id_idx` (`urls_id` ASC),
    CONSTRAINT `place_id`
        FOREIGN KEY (`place_id`)
            REFERENCES `gwk`.`place` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `organizer_id`
        FOREIGN KEY (`organizer_id`)
            REFERENCES `gwk`.`organizer` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `category_id`
        FOREIGN KEY (`category_id`)
            REFERENCES `gwk`.`category` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `ticket_id`
        FOREIGN KEY (`ticket_id`)
            REFERENCES `gwk`.`ticket` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `urls_id`
        FOREIGN KEY (`urls_id`)
            REFERENCES `gwk`.`urls` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_polish_ci;


-- -----------------------------------------------------
-- Table `gwk`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gwk`.`address`
(
    `street`     TEXT   NULL,
    `zipcode`    TEXT   NULL,
    `city`       TEXT   NULL,
    `lat`        DOUBLE NULL,
    `lng`        DOUBLE NULL,
    `place_id`   BIGINT NOT NULL,
    `placefk_id` BIGINT not null,
    PRIMARY KEY (`place_id`),
    FOREIGN KEY (`placefk_id`)
        REFERENCES `gwk`.`place` (`id`)
        ON DELETE NO ACTION
        ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_polish_ci;


-- -----------------------------------------------------
-- Table `gwk`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gwk`.`role`
(
    `id`   INT         NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_polish_ci;


-- -----------------------------------------------------
-- Table `gwk`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gwk`.`user`
(
    `id`      BIGINT      NOT NULL AUTO_INCREMENT,
    `mail`    VARCHAR(50) NULL,
    `role_id` INT         NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `mail_UNIQUE` (`mail` ASC),
    INDEX `role_id_idx` (`role_id` ASC),
    CONSTRAINT `role_id`
        FOREIGN KEY (`role_id`)
            REFERENCES `gwk`.`role` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_polish_ci;


-- -----------------------------------------------------
-- Table `gwk`.`favourite`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `gwk`.`favourite`
(
    `user_id`  BIGINT NULL,
    `event_id` BIGINT NULL,
    INDEX `user_id_idx` (`user_id` ASC),
    INDEX `event_id_idx` (`event_id` ASC),
    CONSTRAINT `user_id`
        FOREIGN KEY (`user_id`)
            REFERENCES `gwk`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `event_id`
        FOREIGN KEY (`event_id`)
            REFERENCES `gwk`.`event` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8
    COLLATE = utf8_polish_ci;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;