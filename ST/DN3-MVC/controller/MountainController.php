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

            $vars =[
                "errors" => $errors,
                "range_id" => $rangeId,
                "name" => $mountainName,
                "height" => $mountainHeight,
                "walk_time" => $mountainWalkTime,
                "description" => $description
            ];

            if (count($errors) > 0){
                ViewHelper::render("view/input.php", $vars);
                return;
            }

            $mountainHeight = str_replace("m", "", $mountainWalkTime);
            $mountainWalkTime = explode(":", $mountainWalkTime);
            $mountainWalkTime = $mountainWalkTime[0] * 60 + $mountainWalkTime[1];

            $authorId = $_SESSION["id"];

            try {
                MountainDB::insertNewMountain($rangeId, $mountainName, $mountainHeight, $mountainWalkTime, $description, $authorId);

                $vars =[
                    "errors" => [],
                    "range_id" => "",
                    "name" => "",
                    "height" => "",
                    "walk_time" => "",
                    "description" => "",
                    "mountainInserted" => true
                ];

                ViewHelper::render("view/input.php", $vars);

            } catch (PDOException $e){
                array_push($errors, "Neznana napaka: $e");
                $vars["errors"] = $errors;
                ViewHelper::render("view/input.php", $vars);
            }
        }


    }
}