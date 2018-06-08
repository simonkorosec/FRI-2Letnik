<?php
/**
 * Created by PhpStorm.
 * User: Simon Korošec
 * Date: 24. 04. 2018
 * Time: 19:15
 */

require_once("DBInit.php");

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

    public static function getMountainsQuery($query) {
        $db = DBInit::getInstance();

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
            $q .= "`mountain_name` LIKE \"%$query[m_name]%\" AND ";
        }


        $q .= " 1";

        $statement = $db->prepare("SELECT `id`, `range_id`, `range_name`, `mountain_name`, `height`, `walk_time`, `description`, `author_id` FROM `alldetails` WHERE $q");

        //$statement->bindParam(":q", $q);

        $statement->execute();
        return $statement->fetchAll();
    }

    public static function getMountainById($id) {
        $db = DBInit::getInstance();

        $statement = $db->prepare("SELECT * FROM `alldetails` WHERE `id`=:id");
        $statement->bindParam(":id", $id);

        $statement->execute();
        return $statement->fetchAll();
    }

    public static function getComments($id) {
        $db = DBInit::getInstance();

        $statement = $db->prepare("SELECT * FROM `comments` WHERE `id_mountain`=:id");
        $statement->bindParam(":id", $id);

        $statement->execute();
        return $statement->fetchAll();
    }

    public static function insertComment($id_mountain, $user_name, $comment) {
        $db = DBInit::getInstance();

        $statement = $db->prepare("INSERT INTO `comments`(`id_mountain`, `user_name`, `time`, `comment`) VALUES (:id_mountain, :user_name, Now(), :comment)");
        $statement->bindParam(":id_mountain", $id_mountain);
        $statement->bindParam(":user_name", $user_name);
        //$statement->bindParam(":time", $time);
        $statement->bindParam(":comment", $comment);

        $statement->execute();
    }

    public static function insertFiles($mountainName, $files) {
        $db = DBInit::getInstance();

        $id = MountainDB::getMountainByName($mountainName)["id"];
        $basePath = BASE_IMG_DIR . $mountainName;

        if (!is_dir($basePath)) {
            mkdir($basePath, 0777, true);
        }
        $basePath = $basePath . "/";

        $statement = $db->prepare("INSERT INTO `images`(`id_mountain`, `path`) VALUES (:id, :path)");
        $statement->bindParam(":id", $id);
        $statement->bindParam(":path", $path);

        $total = count($files["name"]);
        for ($i = 0; $i < $total; $i++) {
            $f = explode('.', $files["name"][$i]);
            $file_ext = strtolower(end($f));
            $extensions = array("jpeg", "jpg", "png");
            if (in_array($file_ext, $extensions) === false) {
                throw new Exception("Samo datoteke tipa PNG in JPEG so dovoljene.");
            }

            $path = $basePath . $files["name"][$i];
            if (move_uploaded_file($files["tmp_name"][$i], $path)) {
                $statement->execute();
            } else {
                throw new Exception("Napaka pri prenosu datoteke.");
            }
        }
    }

    public static function getMountainByName($mountainName) {
        $db = DBInit::getInstance();

        $statement = $db->prepare("SELECT `id` FROM `alldetails` WHERE `mountain_name` = :name LIMIT 1");
        $statement->bindParam(":name", $mountainName);
        $statement->execute();
        return $statement->fetch();
    }

    public static function getImages($id) {
        $db = DBInit::getInstance();

        $statement = $db->prepare("SELECT `path` FROM `images` WHERE `id_mountain` = :id");
        $statement->bindParam(":id", $id);

        $statement->execute();
        return $statement->fetchAll();
    }

}