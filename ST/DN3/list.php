<?php
session_start();
include_once "php/MountainDB.php";
?>


<!DOCTYPE html>
<html lang="sl">

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/style.css"/>

<title>Hribi</title>

<?php include_once "navigation.php"; ?>

<?php
if (empty($_POST["range_id"])) {
    unset($_POST["range_id"]);
}
if (empty($_POST["m_name"])) {
    unset($_POST["m_name"]);
}
//var_dump($_POST);
$args = (MountainDB::getMountainsQuery($_POST));
$tmp = [];

foreach ($args as $a){
    array_push($tmp, $a);
}

$args = json_encode($tmp, JSON_HEX_TAG | JSON_HEX_AMP);
//var_dump($args);
?>

<article>
    <div class="pageTitleDiv">
        <h1 class="pageTitle">Seznam Gora</h1>
    </div>

    <div class="container" id="tableContainer">
        <table class="tableList" id="tableList">
        </table>
    </div>
</article>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
<script src="js/script.js"></script>
<script>

    document.addEventListener("DOMContentLoaded", () => {
        displayMountains(<?= $args ?>);
    });
</script>

</html>