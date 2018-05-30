<?php

require_once("model/BookDB.php");
require_once("model/Cart.php");
require_once("ViewHelper.php");

class StoreController {

    public static function index() {
        $vars = [
            "books" => BookDB::getAll(),
            "cart" => Cart::getAll(),
            "total" => Cart::total()
        ];

        ViewHelper::render("view/store-index.php", $vars);
    }

    public static function indexAjax() {
        ViewHelper::render("view/store-index-ajax.php", ["books" => BookDB::getAll()]);
    }

    public static function cartContents() {
        $vars = [
            "cart" => Cart::getAll(),
            "total" => Cart::total()
        ];

        if (empty($vars["cart"])) {
            ViewHelper::render("view/cart-empty.html");
        } else {
            ViewHelper::render("view/cart-contents.php", $vars);
        }
    }

    public static function addToCart() {
        $id = isset($_POST["id"]) ? intval($_POST["id"]) : null;

        if ($id !== null) {
            Cart::add($id);
        }

        if (ViewHelper::isAjax()) {
            self::cartContents();
        } else {
            ViewHelper::redirect(BASE_URL . "store");
        }
    }

    public static function updateCart() {
        $id = (isset($_POST["id"])) ? intval($_POST["id"]) : null;
        $quantity = (isset($_POST["quantity"])) ? intval($_POST["quantity"]) : null;

        if ($id !== null && $quantity !== null) {
            Cart::update($id, $quantity);
        }

        if (ViewHelper::isAjax()) {
            self::cartContents();
        } else {
            ViewHelper::redirect(BASE_URL . "store");
        }
    }

    public static function purgeCart() {
        Cart::purge();
        if (ViewHelper::isAjax()) {
            self::cartContents();
        } else {
            ViewHelper::redirect(BASE_URL . "store");
        }
    }
}