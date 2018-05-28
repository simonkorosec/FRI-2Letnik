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
        <h1 class="pageTitle">Iskanje Gore</h1>
    </div>
    <div class="container">
        <form class="inputForm" id="searchFilterFrom" action="<?= BASE_URL . "list"?>" method="get">
            <fieldset>
                <label>
                    <select name="range_id" id="range_id" tabindex="1">
                        <option value="0" disabled selected hidden>Izberite Gorovje</option>
                        <?php
                        foreach ($ranges as $range):
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
                <input placeholder="Ime Gore" type="text" tabindex="2" name="m_name" id="m_name"
                       pattern="[\-A-Za-z]+">
            </fieldset>
            <fieldset>
                <legend>Višina Gore</legend>
                <label>
                    <select name="min_H" id="min_H" class="selectOdDo" tabindex="3">
                        <option value="0" selected>Min Višina</option>
                        <option value="500">500m</option>
                        <option value="1000">1000m</option>
                        <option value="1500">1500m</option>
                        <option value="2000">2000m</option>
                        <option value="2500">2500m</option>
                    </select>
                </label>
                <label>
                    <select name="max_H" id="max_H" class="selectOdDo" tabindex="4">
                        <option value="9000" selected>Max Višina</option>
                        <option value="500">500m</option>
                        <option value="1000">1000m</option>
                        <option value="1500">1500m</option>
                        <option value="2000">2000m</option>
                        <option value="2500">2500m</option>
                    </select>
                </label>
            </fieldset>
            <fieldset>
                <legend>Čas Hoje</legend>
                <label>
                    <select name="min_WT" id="min_WT" class="selectOdDo" tabindex="5">
                        <option value="0" selected>Min Čas Hoje</option>
                        <option value="15">15min</option>
                        <option value="30">30min</option>
                        <option value="60">1h</option>
                        <option value="90">1h 30min</option>
                        <option value="120">2h</option>
                        <option value="150">2h 30min</option>
                        <option value="180">3h</option>
                        <option value="240">4h</option>
                        <option value="300">5h</option>
                        <option value="360">6h</option>
                    </select>
                </label>
                <label>
                    <select name="max_WT" id="max_WT" class="selectOdDo" tabindex="6">
                        <option value="9999" selected>Max Čas Hoje</option>
                        <option value="15">15min</option>
                        <option value="30">30min</option>
                        <option value="60">1h</option>
                        <option value="90">1h 30min</option>
                        <option value="120">2h</option>
                        <option value="150">2h 30min</option>
                        <option value="180">3h</option>
                        <option value="240">4h</option>
                        <option value="300">5h</option>
                        <option value="360">6h</option>
                        <option value="9999">več kot 6h</option>
                    </select>
                </label>
            </fieldset>
            <fieldset>
                <input name="submit" type="submit" class="searchBtn" tabindex="7" value="Iskanje Gore" >
            </fieldset>
        </form>
    </div>
</article>


<script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
<script src="../js/script.js"></script>

</html>