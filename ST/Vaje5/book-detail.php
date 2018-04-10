<?php

require_once("BookDB.php");

?><!DOCTYPE html>
<meta charset="UTF-8"/>
<title>Book detail</title>

<?php $book = BookDB::get($_GET["id"]); ?>

<h1>Details about: <?= $book->title ?></h1>
<?php
    $format = "<ul><li><strong>Author:</strong> %s <br /></li>" .
        "<li><strong>Title:</strong> %s <br /></li>" .
        "<li><strong>Price:</strong> %s €<br /> </ul></li>";

    echo "<br /><br />";
    echo sprintf($format, $book->author, $book->title, $book->price);
?>