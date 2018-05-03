<?php
session_start();
include_once "php/MountainDB.php";
include_once "php/newmountain.php";
?>

<!DOCTYPE html>
<html lang="sl">

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="css/style.css"/>

<title>Hribi</title>

<?php include_once "navigation.php"; ?>

<article>
    <div class="pageTitleDiv">
        <h1 class="pageTitle">Vnos nove gore</h1>
    </div>

    <?php include_once "php/errors.php"; ?>

    <?php
    if (isset($mountainInserted) && $mountainInserted === true):
        ?>

        <div class="success">
            <h2>Mountain successfully inserted.</h2>
        </div>

    <?php endif; ?>

    <div class="container">
        <form class="inputForm" id="newInputForm" action="input.php" method="post">
            <fieldset>
                <label>
                    <select name="mountainRangeId" id="mountainRangeId" class="select" required tabindex="1">
                        <option value="0" disabled selected hidden>Izberite Gorovje</option>

                        <?php
                        foreach (MountainDB::getRanges() as $range):
                            $id = $range["id"];
                            $name = $range["name"];
                            ?>
                            <option value="<?= $id ?>"><?= $name ?></option>
                        <?php
                        endforeach;
                        ?>

                    </select>
                </label>
            </fieldset>
            <fieldset>
                <input placeholder="Ime Gore" type="text" tabindex="2" required name="mountainName" id="mountainName"
                       pattern="[\-A-Za-z čšžČŠŽđĐ]+" maxlength="150">
            </fieldset>
            <fieldset>
                <input placeholder="Višina Gore (v metrih)" type="text" tabindex="3" required name="mountainHeight"
                       id="mountainHeight" pattern="[0-9]{1,4}([ ]*)?(m)?">
            </fieldset>
            <fieldset>
                <input placeholder="Čas Hoje (format HH:MM)" type="text" tabindex="4" required name="mountainWalkTime"
                       id="mountainWalkTime"
                       pattern="[0-9]{1,2}:[0-5][0-9]">
            </fieldset>
            <fieldset>
                <textarea placeholder="Tukaj vpišite opis gore..." name="description" id="hillDescription"
                          tabindex="5" required></textarea>
            </fieldset>

            <fieldset>
                <button name="submit" type="submit" class="searchBtn" tabindex="6">Vnos
                    Nove Gore
                </button>
            </fieldset>
        </form>
    </div>
</article>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
<script src="js/script.js"></script>

</html>