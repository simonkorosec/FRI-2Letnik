<?php

require_once ("BookDB.php");

?><!DOCTYPE html>

<meta charset="UTF-8" />
<title>Library</title>

<h1>A book library written in PHP</h1>

<p>Check our collection of fine books. <a href="search-books.php">Search for books.</a></p>

<ul>
    <?php foreach (BookDB::getAllBooks() as $book): 
    # Implemet a nicer presentation for a book, for instance: <li>Author: Title (XY EUR)</li>

        $preset = "<a href='book-detail.php?id=%d'>%s : %s : %d€</a>";
        $book = sprintf($preset, $book->id, $book->author, $book->title, $book->price);
    ?>
        <li><?= $book ?></li>
    <?php endforeach; ?>
</ul>