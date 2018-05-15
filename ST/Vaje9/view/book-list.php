<!DOCTYPE html>

<link rel="stylesheet" type="text/css" href="<?= ASSETS_URL . "style.css" ?>">
<meta charset="UTF-8" />
<title>Library</title>

<h1>All books</h1>

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

    <?php foreach ($books as $book): ?>
        <li><a href="<?= BASE_URL . "book?id=" . $book["id"] ?>"><?= $book["author"] ?>: 
        	<?= $book["title"] ?> (<?= $book["year"] ?>)</a></li>
    <?php endforeach; ?>

</ul>
