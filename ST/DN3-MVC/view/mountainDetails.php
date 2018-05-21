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
        <h1 class="pageTitle" id="pgTitle"></h1>
    </div>
    <div class="container">
        <table class="tableList" id="dtTable">

        </table>

<!--        <form class="deleteForm" action="list.php">-->
<!--            <fieldset>-->
<!--                <button name="delete" type="submit" id="deleteBtn" >Izbriši Goro</button>-->
<!--            </fieldset>-->
<!--        </form>-->

    </div>
</article>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
<script src="../js/script.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", () => {
        displayMountainDetails(<?= $mnt ?>);
    });
</script>

</html>