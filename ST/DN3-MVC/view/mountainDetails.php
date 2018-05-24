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

    <?php if (isset($_SESSION["username"]) && !empty($_SESSION["username"])) : ?>
        <div class="container">
            <form class="inputForm" action="<?= BASE_URL . "addComment" ?>" method="post">
                <fieldset>
                    <textarea class="comment-tx" title="comment-input" name="comment" required placeholder="Vnesite komentar..."></textarea>
                </fieldset>

                <fieldset>
                    <input name="submit" type="submit" class="searchBtn" value="Komentiraj">
                    <input hidden type="text" name="id_mountain" value="<?= $id ?>">
                    <input hidden type="text" name="user_name" value="<?= htmlspecialchars($_SESSION["username"]) ?>">
                </fieldset>
            </form>
        </div>
    <?php endif ?>

    <div class="container" id="comment-section">
        <table id="comment-table">
        <?php foreach ($comments as $comment):?>
            <tr class="comment">
                <td class="comment-info">
                    <p class="comment-userName"><?= $comment["user_name"]?></p>
                    <p class="comment-time"><?= $comment["time"]?></p>
                </td>
                <td class="comment-text">
                    <p><?= $comment["comment"]?></p>
                </td>
            </tr>
        <?php endforeach; ?>
        </table>
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