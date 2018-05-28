<!DOCTYPE html>
<html lang="sl">

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="../css/style.css"/>

<title>Hribi</title>

<?php
$currentPage = basename(__FILE__);
include_once "navigation.php";
?>

<article>

    <div class="pageTitleDiv">
        <h1 class="pageTitle">Prijava</h1>
    </div>

    <?php include_once "errors.php"; ?>

    <div class="container">
        <form class="inputForm" id="basicSearch" action="<?= BASE_URL . "parseLogin"?>" method="post">
            <fieldset>
                <input placeholder="Uporabniško ime" type="text" tabindex="1" required name="username"
                       id="username" pattern="[\-A-Za-z][\-A-Za-z0-9]*" value="<?= $username ?>">
            </fieldset>
            <fieldset>
                <input placeholder="Geslo" type="password" tabindex="2" required name="password" id="password"
                       pattern="[\-A-Za-z0-9]+">
            </fieldset>
            <fieldset>
                <input name="login_user" type="submit" class="searchBtn" tabindex="3" value="Prijava" >
            </fieldset>
        </form>
    </div>

</article>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
<script src="../js/script.js"></script>

</html>