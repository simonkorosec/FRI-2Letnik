<?php

require_once("model/BookDB.php");
require_once("ViewHelper.php");

# Controller for handling books
class BookController {

    public static function getAll() {
        # Reads books from the database
        $variables = ["books" => BookDB::getAll()];

        # Renders the view and sets the $variables array into view's scope
        ViewHelper::render("view/book-list.php", $variables);
    }

    public static function get() {
        $variables = ["book" => BookDB::get($_GET["id"])];
        ViewHelper::render("view/book-detail.php", $variables);
    }

    public static function showAddForm($variables = array("author" => "", "title" => "", 
        "price" => "", "year" => "")) {
        ViewHelper::render("view/book-add.php", $variables);
    }

    public static function add() {
        $validData = isset($_POST["author"]) && !empty($_POST["author"]) && 
                isset($_POST["title"]) && !empty($_POST["title"]) &&
                isset($_POST["year"]) && !empty($_POST["year"]) &&
                isset($_POST["price"]) && !empty($_POST["price"]);

        if ($validData) {
            BookDB::insert($_POST["author"], $_POST["title"], $_POST["price"], $_POST["year"]);
            ViewHelper::redirect(BASE_URL . "book");
        } else {
            self::showAddForm($_POST);
        }
    }

    # TODO: Implement controlers for searching, editing and deleting books
}