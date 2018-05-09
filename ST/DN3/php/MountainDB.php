<?php
/**
 * Created by PhpStorm.
 * User: Simon Korošec
 * Date: 24. 04. 2018
 * Time: 19:15
 */

require_once "DBInit.php";

class MountainDB {

    public static function getRanges() {
        $db = DBInit::getInstance();

        $statement = $db->prepare("SELECT range_id, name FROM rangenames;");
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

    public static function getAllMountains() {
        $db = DBInit::getInstance();

        $statement = $db->prepare("SELECT `id`, `range_id`, `Range Name`, `Mountain Name`, `height`, `walk_time`, `description`, `author_id`, `username` FROM `alldetails`");

        $statement->execute();

        return $statement->fetchAll();
    }

    public static function getMountainsQuery($query) {
        $db = DBInit::getInstance();

//        $statement = $db->prepare("SELECT `id`, `range_id`, `Range Name`, `Mountain Name`, `height`, `walk_time`, `description`, `author_id`, `username`
//FROM `alldetails`
//WHERE :query");

        $q = "";
        $min_H = -13;
        $max_H = 9999;
        $min_WT = -13;
        $max_WT = 9999;

        if (isset($query["min_H"]) || array_key_exists("min_H", $query)) {
            $min_H = $query["min_H"];
        }
        if (isset($query["max_H"]) || array_key_exists("max_H", $query)) {
            $max_H = $query["max_H"];
        }
        if (isset($query["min_WT"]) || array_key_exists("min_WT", $query)) {
            $min_WT = $query["min_WT"];
        }
        if (isset($query["max_WT"]) || array_key_exists("max_WT", $query)) {
            $max_WT = $query["max_WT"];
        }

        $q .= "`height` BETWEEN $min_H AND $max_H AND ";
        $q .= "`walk_time` BETWEEN $min_WT AND $max_WT AND ";

        if (isset($query["range_id"]) || array_key_exists("range_id", $query)) {
            $q .= "`range_id` = $query[range_id] AND ";
        }
        if (isset($query["m_name"]) || array_key_exists("m_name", $query)) {
            $q .= "`Mountain Name` LIKE \"%$query[m_name]%\" AND ";
        }

        $q .= " 1";



        //$statement->bindParam(":query", $q);

        $statement = $db->prepare("SELECT `id`, `range_id`, `range_name`, `mountain_name`, `height`, `walk_time`, `description`, `author_id` FROM `alldetails` WHERE $q");

        //var_dump($statement);

        $statement->execute();

        return $statement->fetchAll();

    }

}