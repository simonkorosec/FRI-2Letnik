<?php

if (!isset($_COOKIE["counter"])) {
    setcookie('counter', 1, time() + 5);
    $message = "This is your first visit.";
} else {
    $counter = intval($_COOKIE["counter"]) + 1;
    $message = "You have visited this site $counter times.";
    setcookie('counter', $counter, time() + 5);
}
?><!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Counter of your visits</title>
    </head>
    <body>
        <p><?= $message ?></p>
    </body>
</html>
