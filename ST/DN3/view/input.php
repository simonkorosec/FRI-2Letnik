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
        <h1 class="pageTitle">Vnos nove gore</h1>
    </div>

    <?php include_once "errors.php"; ?>

    <?php
    if (isset($mountainInserted) && $mountainInserted === true):?>
        <div class="success">
            <h2>Mountain successfully inserted.</h2>
        </div>
    <?php endif; ?>

    <div class="container">
        <form class="inputForm" id="newInputForm" action="<?= BASE_URL . "parseNewMountain" ?>" method="post"
              enctype="multipart/form-data">
            <fieldset>
                <label>
                    <select name="range_id" id="range_id" class="select" required tabindex="1">
                        <option value="0" disabled selected hidden>Izberite Gorovje</option>

                        <?php foreach ($ranges as $range): ?>
                            <option value="<?= $range["range_id"] ?>"><?= $range["name"] ?></option>
                        <?php endforeach; ?>

                    </select>
                </label>
            </fieldset>
            <fieldset>
                <input placeholder="Ime Gore" type="text" tabindex="2" required name="name" id="name"
                       pattern="[\-A-Za-z čšžČŠŽđĐ]+" maxlength="150" value="<?= $name ?>">
            </fieldset>
            <fieldset>
                <input placeholder="Višina Gore (v metrih)" type="text" tabindex="3" required name="height"
                       id="height" pattern="[0-9]{1,4}([ ]*)?(m)?" <?= $height ?>>
            </fieldset>
            <fieldset>
                <input placeholder="Čas Hoje (format HH:MM)" type="text" tabindex="4" required name="walk_time"
                       id="walk_time"
                       pattern="[0-9]{1,2}:[0-5][0-9]" <?= $walk_time ?>>
            </fieldset>
            <fieldset>
                <textarea placeholder="Tukaj vpišite opis gore..." name="description" id="description"
                          tabindex="5" required <?= $description ?> ></textarea>
            </fieldset>
            <fieldset>
                <input type="file" name="images[]" multiple>
            </fieldset>

            <fieldset>
                <button name="submit" type="submit" class="searchBtn" tabindex="6">Vnos
                    Nove Gore
                </button>
                <label>
                    <input hidden type="text" name="author_id" value="<?= $_SESSION['id'] ?>">
                </label>
            </fieldset>
        </form>
    </div>
</article>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
<script src="../js/script.js"></script>

</html>