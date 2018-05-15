<!DOCTYPE html>

<link rel="stylesheet" type="text/css" href="<?= ASSETS_URL . "style.css" ?>">
<meta charset="UTF-8" />
<title>Book detail</title>

<h1>Details of: <?= $book["title"] ?></h1>

<p>[
<a href="<?= BASE_URL . "book" ?>">All books</a> |
<a href="<?= BASE_URL . "book/search" ?>">Search</a> | 
<a href="<?= BASE_URL . "book/add" ?>">Add new</a> |
<a href="<?= BASE_URL . "store" ?>">Bookstore</a> |
<a href="<?= BASE_URL . "store/ajax" ?>">AJAX Bookstore</a> |
<a href="<?= BASE_URL . "book/search/ajax" ?>">AJAX Search</a> |
<a href="<?= BASE_URL . "book/search/vue" ?>">Vue Search</a>
]</p>

<ul>
    <li>Author: <b><?= $book["author"] ?></b></li>
    <li>Title: <b><?= $book["title"] ?></b></li>
    <li>Price: <b><?= $book["price"] ?> EUR</b></li>
    <li>Year: <b><?= $book["year"] ?></b></li>
</ul>

<p>[ <a href="<?= BASE_URL . "book/edit?id=" . $_GET["id"] ?>">Edit</a> |
<a href="<?= BASE_URL . "book" ?>">Book index</a> ]</p>
