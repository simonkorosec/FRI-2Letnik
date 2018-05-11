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

    public static function helloWorld() {
        $isQueryParam = isset($_GET["person"]) && !empty($_GET["person"]);

        $vars = [
            "person" => $isQueryParam ? $_GET["person"] : "World"
        ];

        ViewHelper::render("view/hello-world.php", $vars);
    }

    public static function search() {

        $isQuery = isset($_GET["query"]) && !empty($_GET["query"]);
        $vars = [
            "query" => $isQuery ? $_GET["query"] : "",
            "hits" => $isQuery ? BookDB::search($_GET["query"]) : []
        ];

        ViewHelper::render("view/book-search.php", $vars);
    }

    public static function editDelete() {
        $isEdit = isset($_POST["author"]) && !empty($_POST["author"]) &&
            isset($_POST["title"]) && !empty($_POST["title"]) &&
            isset($_POST["price"]) && !empty($_POST["price"]) &&
            isset($_POST["id"]) && !empty($_POST["id"]) &&
            isset($_POST["year"]) && !empty($_POST["year"]);

        $isDelete = isset($_POST["delete_confirmation"]) &&
            isset($_POST["id"]) && !empty($_POST["id"]);

        // If we send a valid POST request (contains all required data)
        $vars = [];
        if ($isEdit) {
            try {
                BookDB::update($_POST["id"], $_POST["author"], $_POST["title"], $_POST["price"], $_POST["year"]);

                // Go to the detail page
                $book = BookDB::get($_REQUEST["id"]);
                $vars = [
                    "book" => $book
                ];
                ViewHelper::render("view/book-detail.php", $vars);
                return;
            } catch (Exception $e) {
                $errorMessage = "A database error occured: $e";
                $vars = [
                    "errorMessage" => $errorMessage
                ];
            }
            // Do we delete the record?
        } else if ($isDelete) {
            try {
                BookDB::delete($_POST["id"]);
                ViewHelper::redirect(BASE_URL . "book");
                return;
            } catch (Exception $e) {
                $errorMessage = "A database error occured: $e";
                $vars = [
                    "errorMessage" => $errorMessage
                ];
            }
            // Read the contents from the DB and populate the form with it
        } else {
            try {
                // GET id from either GET or POST request
                $book = BookDB::get($_REQUEST["id"]);
                $vars = [
                    "book" => $book
                ];
            } catch (Exception $e) {
                $errorMessage = "A database error occured: $e";
                $vars = [
                    "errorMessage" => $errorMessage
                ];
            }
        }
        ViewHelper::render("view/book-edit.php", $vars);
    }

}