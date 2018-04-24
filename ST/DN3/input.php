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
    <div class="container">
        <form class="inputForm" id="newInputForm">
            <fieldset>
                <label>
                    <select name="mountainRangeId" id="mountainRangeId" class="select" required tabindex="1">
                        <option value="" disabled selected hidden>Izberite Gorovje</option>
                        <option value="1">Goriško, Notranjsko in Snežniško hribovje</option>
                        <option value="2">Julijske Alpe</option>
                        <option value="3">Kamniško Savinjske Alpe</option>
                        <option value="4">Karavanke</option>
                        <option value="5">Pohorje in ostala severovzhodna Slovenija</option>
                        <option value="6">Polhograjsko hribovje in Ljubljana</option>
                        <option value="7">Škofjeloško, Cerkljansko hribovje in Jelovica</option>
                        <option value="8">Zasavsko - Posavsko hribovje in Dolenjska</option>
                    </select>
                </label>
            </fieldset>
            <fieldset>
                <input placeholder="Ime Gore" type="text" tabindex="2" required name="mountainName" id="mountainName"
                       pattern="[\-A-Za-z čšžČŠŽđĐ]+">
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
                          tabindex="5" maxlength="500" required></textarea>
            </fieldset>

            <fieldset>
                <button name="submit" type="submit" class="searchBtn" tabindex="6" onclick="inputNewMountain()">Vnos Nove Gore</button>
            </fieldset>
        </form>
    </div>
</article>

<script src="https://code.jquery.com/jquery-3.3.1.min.js"
        integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
        crossorigin="anonymous"></script>
<script src="js/script.js"></script>

</html>