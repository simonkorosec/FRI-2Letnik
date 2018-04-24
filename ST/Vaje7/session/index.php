<?php
session_start();

if (!isset($_SESSION["counter"])) {
    $_SESSION["counter"] = 1;
    $message = "This is your first visit!";
} else {
    $_SESSION["counter"] = $_SESSION["counter"] + 1;
    $message = "You have visited this site $_SESSION[counter] times.";
}
?><!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Counting your visits with PHP session</title>
    </head>
    <body>
        <p><?= $message ?></p>
    </body>
</html>
