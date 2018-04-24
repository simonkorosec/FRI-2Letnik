<?php

session_start();

require_once("BookDB.php");

?><!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="style.css">
    <meta charset="UTF-8"/>
    <title>Library</title>
</head>
<body>

<h1>A PHP bookstore</h1>

<div id="main">

    <?php foreach (BookDB::getAll() as $book): ?>

        <div class="book">
            <form action="manage-cart.php" method="post" id="form_<?= $book["id"] ?>">
                <input type="hidden" name="cart_action" value="add"/>
                <input type="hidden" name="id" value="<?= $book["id"] ?>"/>
                <p><?= $book["title"] ?></p>
                <p><?= $book["author"] ?>, <?= $book["year"] ?> </p>
                <p><?= number_format($book["price"], 2) ?> EUR<br/>
                    <button type="submit">Add to cart</button>
            </form>

        </div>


    <?php endforeach; ?>

</div>

<?php

$cart = (isset($_SESSION["cart"])) ? $_SESSION["cart"] : [];

if (!empty($cart)): ?>
    <div class="cart">
        <h3>Shopping cart</h3>
        <?php
        $total = 0;
        foreach ($cart as $id => $quantity):
            $book = BookDB::get($id);
            $total += $quantity * $book["price"]; ?>

            <form action="manage-cart.php" method="post">
                <input type="hidden" name="cart_action" value="edit"/>
                <input type="hidden" name="id" value="<?= $book['id'] ?>"/>
                <label>
                    <input type="number" name="quantity" value="<?= $quantity ?>" style="width: 35px;"/>
                </label>
                &times; <?= $book["title"] ?>
                <input type="submit" value="Update"/>
                <br/>
            </form>
        <?php endforeach; ?>

        <p>Total: <b><?= number_format($total, 2) ?> EUR</b></p>

        <form action="manage-cart.php" method="post">
            <input type="hidden" name="cart_action" value="purge_cart"/>
            <p>
                <button type="submit">Purge cart</button>
            </p>
        </form>
    </div>

<?php endif; ?>

</body>
</html>
