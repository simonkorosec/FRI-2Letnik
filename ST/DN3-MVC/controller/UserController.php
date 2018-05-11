<?php

require_once("model/UsersDB.php");
require_once("ViewHelper.php");

# Controller for handling user login / registration
class UserController {

    public static function login() {
        ViewHelper::render("view/login.php", ["username" => ""]);
    }

    public static function register() {
        ViewHelper::render("view/register.php", ["username" => ""]);
    }

    public static function parseLogin() {
        $username = $_POST["username"];
        $password = $_POST["password"];
        $errors = [];

        $query = UsersDB::getPassword($username);
        if ($username != $query["username"]) {
            array_push($errors, "Username does not exist.");
        } else {
            $hash = $query["password"];
            if (password_verify($password, $hash)) {
                $_SESSION['username'] = $username;
                $_SESSION['id'] = $query["id"];
                ViewHelper::redirect(BASE_URL . "home");
//                header("Location: index.php");
            } else {
                array_push($errors, "Wrong password.");
            }
        }
        ViewHelper::render("view/login.php", ["errors" => $errors, "username" => $username]);
    }

    public static function parseRegister() {
        $username = $_POST["username"];
        $password1 = $_POST["password_1"];
        $password2 = $_POST["password_2"];
        $errors = [];

        if ($password1 != $password2) {
            array_push($errors, "Passwords don't match.");
        } else {
            try {
                $password1 = password_hash($password1, PASSWORD_DEFAULT);
                UsersDB::register($username, $password1);

                $_SESSION['username'] = $username;
                $_SESSION['id'] = UsersDB::getPassword($username)["id"];

                ViewHelper::redirect(BASE_URL . "home");
            } catch (PDOException $e) {
                array_push($errors, "Username already taken.");
            }
        }
        ViewHelper::render("view/register.php", ["errors" => $errors, "username" => $username]);

    }

    public static function logout() {
        session_destroy();
        ViewHelper::redirect(BASE_URL . $_SESSION["currPage"]);
    }
}