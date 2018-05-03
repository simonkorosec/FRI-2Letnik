<?php
/**
 * Created by PhpStorm.
 * User: Simon Korošec
 * Date: 26. 04. 2018
 * Time: 15:22
 */

include_once "MountainDB.php";

$errors = [];

if ($_SERVER['REQUEST_METHOD'] === 'POST' && isset($_POST["submit"])) {
    $rangeId = $_POST["mountainRangeId"];
    $mountainName = $_POST["mountainName"];
    $mountainHeight = $_POST["mountainHeight"];
    $mountainWalkTime = $_POST["mountainWalkTime"];
    $description = $_POST["description"];
    /*
        $retval = ereg("(\.)(com$)", $email_id);

        if( $retval == true )
        {*/

    if (!preg_match("/^[\-A-Za-z čšžČŠŽđĐ]+$/", $mountainName)) {
        array_push($errors, "Ime gore v napačnem formatu.");
    }
    if (!preg_match("/^[0-9]{1,4}([ ]*)?(m)?$/", $mountainHeight)) {
        array_push($errors, "Višina gore v napačnem formatu.");
    }
    if (!preg_match("/^[0-9]{1,2}:[0-5][0-9]$/", $mountainWalkTime)) {
        array_push($errors, "Čas hoje v napačnem formatu.");
    }

    if (count($errors) > 0){
        return;
    }

    $mountainHeight = str_replace("m", "", $mountainWalkTime);

    $mountainWalkTime = explode(":", $mountainWalkTime);
    $mountainWalkTime = $mountainWalkTime[0] * 60 + $mountainWalkTime[1];

    $authorId = $_SESSION["id"];

    MountainDB::insertNewMountain($rangeId, $mountainName, $mountainHeight, $mountainWalkTime, $description, $authorId);

    $mountainInserted = true;
}

