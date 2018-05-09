<?php
/**
 * Created by PhpStorm.
 * User: Simon Korošec
 * Date: 8. 05. 2018
 * Time: 20:06
 */

include_once "MountainDB.php";

if ($_SERVER['REQUEST_METHOD'] === 'POST' /*&& isset($_POST["submit"])*/) {
    echo "($_POST[args])";
    /*
    $args = (MountainDB::getMountainsQuery($_POST));
    $tmp = [];

    foreach ($args as $a){
        array_push($tmp, $a);
    }

    $args = json_encode($tmp, JSON_HEX_TAG | JSON_HEX_AMP);
    echo "$args";*/

}
