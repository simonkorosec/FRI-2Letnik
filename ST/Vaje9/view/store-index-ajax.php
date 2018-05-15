<!DOCTYPE html>

<link rel="stylesheet" type="text/css" href="<?= ASSETS_URL . "style.css" ?>">
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
    $(".add-to-cart").click(function(){
        $.post("<?= BASE_URL . "store/add-to-cart" ?>", 
            { id: $(this).data("id") },
            function (data) {
                $("#cart").html(data);
            }
        );
    });

    $.get("<?= BASE_URL . "store/cart" ?>", 
        function (data) {
            $("#cart").html(data);
        }
    );
});
</script>
<meta charset="UTF-8" />
<title>AJAX Bookstore</title>

<h1>AJAX Bookstore</h1>

<p>[
<a href="<?= BASE_URL . "book" ?>">All books</a> |
<a href="<?= BASE_URL . "book/search" ?>">Search</a> | 
<a href="<?= BASE_URL . "book/add" ?>">Add new</a> |
<a href="<?= BASE_URL . "store" ?>">Bookstore</a> |
<a href="<?= BASE_URL . "store/ajax" ?>">AJAX Bookstore</a> |
<a href="<?= BASE_URL . "book/search/ajax" ?>">AJAX Search</a> |
<a href="<?= BASE_URL . "book/search/vue" ?>">Vue Search</a>
]</p>

<div id="main">
    <?php foreach ($books as $book): ?>

        <div class="book">
            <p><?= $book["title"] ?></p>
            <p><?= $book["author"] ?>, <?= $book["year"] ?></p>
            <p><?= number_format($book["price"], 2) ?> EUR<br/>
            <button class="add-to-cart" data-id="<?= $book["id"] ?>">Add to cart</button></p>
        </div>

    <?php endforeach; ?>

</div>


<div id="cart"></div>    


