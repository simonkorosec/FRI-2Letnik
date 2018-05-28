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
            <form class="inputForm" id="comment-form" action="<?= BASE_URL . "addComment" ?>" method="post">
                <fieldset>
                    <textarea class="comment-tx" title="comment-input" name="comment" required
                              placeholder="Vnesite komentar..."></textarea>
                </fieldset>

                <fieldset>
                    <input name="submit" type="submit" class="searchBtn" value="Komentiraj">
                    <input hidden type="text" name="id_mountain" value="<?= $id ?>">
                    <input hidden type="text" name="user_name" value="<?= htmlspecialchars($_SESSION["username"]) ?>">
                </fieldset>
            </form>
        </div>
    <?php endif ?>

    <?php if ($numImgs > 0): ?>
        <div class="container" id="slideshow">
            <div class="slideshow-container">
                <?php $i = 1;
                foreach ($images as $img): ?>

                    <!-- Full-width images with number and caption text -->
                    <div class="mySlides fade">
                        <div class="numbertext"><?= $i++ ?> / <?= $numImgs ?></div>
                        <img src="../<?= $img["path"] ?>" style="width:100%;">
                        <!--<div class="text">Slika 1</div>-->
                    </div>

                <?php endforeach; ?>

                <!-- Next and previous buttons -->
                <a class="prev" onclick="plusSlides(-1)">&#10094;</a>
                <a class="next" onclick="plusSlides(1)">&#10095;</a>
            </div>

            <!-- The dots/circles -->
            <div style="text-align:center;">
                <?php for ($i = 1; $i <= $numImgs; $i++): ?>
                    <span class="dot" onclick="currentSlide(<?= $i ?>)"></span>
                <?php endfor; ?>
            </div>

        </div>
    <?php endif; ?>

    <?php if (count($comments) > 0): ?>
        <div class="container" id="comment-section">
            <table id="comment-table">
                <?php foreach ($comments as $comment): ?>
                    <tr class="comment">
                        <td class="comment-info">
                            <p class="comment-userName"><?= $comment["user_name"] ?></p>
                            <p class="comment-time"><?= $comment["time"] ?></p>
                        </td>
                        <td class="comment-text">
                            <p><?= $comment["comment"] ?></p>
                        </td>
                    </tr>
                <?php endforeach; ?>
            </table>
        </div>
    <?php endif; ?>

</article>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
<script src="../js/script.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", () => {
        displayMountainDetails(<?= $mnt ?>);
        showSlides(slideIndex);
    });
</script>

</html>