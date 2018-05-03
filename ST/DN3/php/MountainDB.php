<?php
/**
 * Used to register a new user or check if given user name and password match.
 *
 * Created by PhpStorm.
 * User: Simon Korošec
 * Date: 24. 04. 2018
 * Time: 19:15
 */

require_once "DBInit.php";

class MountainDB {

    public static function getRanges() {
        $db = DBInit::getInstance();

        $statement = $db->prepare("SELECT id, name FROM rangenames;");
        $statement->execute();

        return $statement->fetchAll();
    }

    public static function insertNewMountain($rangeId, $mountainName, $mountainHeight, $mountainWalkTime, $description, $authorId) {
        $db = DBInit::getInstance();

        $statement = $db->prepare("INSERT INTO `mountain`(`range_id`, `name`, `height`, `walk_time`, `description`, `author_id`) VALUES (:rangeID, :name, :height, :walk_time, :description, :author_id);");

        $statement->bindParam(":rangeID", $rangeId);
        $statement->bindParam(":name", $mountainName);
        $statement->bindParam(":height", $mountainHeight);
        $statement->bindParam(":walk_time", $mountainWalkTime);
        $statement->bindParam(":description", $description);
        $statement->bindParam(":author_id", $authorId);

        $statement->execute();
    }


}