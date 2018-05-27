<?php

require_once("model/MountainDB.php");
require_once("ViewHelper.php");

# Controller for handling mountains
class MountainController {


    public static function home() {
        ViewHelper::render("view/home.php");
    }

    public static function search() {
        $vars = [
            "ranges" => MountainDB::getRanges()
        ];

        ViewHelper::render("view/search.php", $vars);
    }

    public static function list() {
        $tmp = [];
        if ($_SERVER["REQUEST_METHOD"] == "GET") {
            if (empty($_GET["range_id"])) {
                unset($_GET["range_id"]);
            }
            if (empty($_GET["m_name"])) {
                unset($_GET["m_name"]);
            }
            $args = (MountainDB::getMountainsQuery($_GET));

            foreach ($args as $a) {
                array_push($tmp, $a);
            }
        }
        $args = json_encode($tmp, JSON_HEX_TAG | JSON_HEX_AMP);
        $vars = [
            "args" => $args
        ];

        ViewHelper::render("view/list.php", $vars);
    }

    public static function input() {
        $vars = [
            "ranges" => MountainDB::getRanges(),
            "name" => "",
            "height" => "",
            "walk_time" => "",
            "description" => ""
        ];
        ViewHelper::render("view/input.php", $vars);
    }

    public static function about() {
        ViewHelper::render("view/about.php");
    }

    public static function parseNewMountain() {
        $errors = [];

        if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST["submit"])) {
            $rangeId = $_POST["range_id"];
            $mountainName = $_POST["name"];
            $mountainHeight = $_POST["height"];
            $mountainWalkTime = $_POST["walk_time"];
            $description = $_POST["description"];

            if (!preg_match("/^[\-A-Za-z čšžČŠŽđĐ]+$/", $mountainName)) {
                array_push($errors, "Ime gore v napačnem formatu.");
            }
            if (!preg_match("/^[0-9]{1,4}([ ]*)?(m)?$/", $mountainHeight)) {
                array_push($errors, "Višina gore v napačnem formatu.");
            }
            if (!preg_match("/^[0-9]{1,2}:[0-5][0-9]$/", $mountainWalkTime)) {
                array_push($errors, "Čas hoje v napačnem formatu.");
            }

            $vars = [
                "errors" => $errors,
                "ranges" => MountainDB::getRanges(),
                "name" => $mountainName,
                "height" => $mountainHeight,
                "walk_time" => $mountainWalkTime,
                "description" => $description
            ];

            if (count($errors) > 0) {
                ViewHelper::render("view/input.php", $vars);
                return;
            }

            $mountainHeight = str_replace("m", "", $mountainWalkTime);
            $mountainWalkTime = explode(":", $mountainWalkTime);
            $mountainWalkTime = $mountainWalkTime[0] * 60 + $mountainWalkTime[1];
            $authorId = $_SESSION["id"];

            $db = DBInit::getInstance();
            try {
                $db->beginTransaction();
                MountainDB::insertNewMountain($rangeId, $mountainName, $mountainHeight, $mountainWalkTime, $description, $authorId);
                MountainDB::insertFiles($mountainName, $_FILES["images"]);
                $db->commit();

                $vars = [
                    "errors" => [],
                    "ranges" => MountainDB::getRanges(),
                    "name" => "",
                    "height" => "",
                    "walk_time" => "",
                    "description" => "",
                    "mountainInserted" => true
                ];
                ViewHelper::render("view/input.php", $vars);
            } catch (PDOException $e) {
                $db->rollBack();
                array_push($errors, "Neznana napaka: $e");
                $vars["errors"] = $errors;
                ViewHelper::render("view/input.php", $vars);
            } catch (Exception $e){
                $db->rollBack();
                array_push($errors, $e->getMessage());
                $vars["errors"] = $errors;
                ViewHelper::render("view/input.php", $vars);
            }
        }
    }

    public static function showDetails() {
        $id = $_GET["id"];
        $mnt = MountainDB::getMountainById($id);
        //var_dump($mnt);

        $comments = MountainDB::getComments($id);

        ViewHelper::render("view/mountainDetails.php", ["id" => $id, "mnt" => json_encode($mnt, JSON_HEX_TAG | JSON_HEX_AMP), "comments" => $comments]);
    }

    public static function addComment() {
        $id = $_POST["id_mountain"];
        $comment = $_POST["comment"];
        $user_name = $_POST["user_name"];

        MountainDB::insertComment($id, $user_name, $comment);

    }
}