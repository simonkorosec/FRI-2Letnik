<!DOCTYPE html>
<html lang="sl">

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/style.css"/>

<title>Hribi</title>

<?php include_once "navigation.php"; ?>


<article>
    <div class="pageTitleDiv">
        <h1 class="pageTitle">Domača Stran</h1>
    </div>
    <div class="container">
        <form class="inputForm" id="basicSearch" action="list.php">
            <fieldset>
                <input placeholder="Ime Gore" type="text" tabindex="1" required autofocus name="mountainName" id="mountainName"
                       pattern="[\-A-Za-z]+">
            </fieldset>
            <fieldset>
                <button name="submit" type="submit" class="searchBtn" tabindex="2" onclick="saveBasicSearch()">Iskanje</button>
            </fieldset>
        </form>
    </div>
</article>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
<script src="js/script.js"></script>

</html>