<?php
session_start();

require_once "UsersDB.php";

$username = "";
$errors = [];

if (isset($_POST['reg_user'])) {
    $username = $_POST["username"];
    $password1 = $_POST["password_1"];
    $password2 = $_POST["password_2"];

    if ($password1 != $password2) {
        array_push($errors, "Passwords don't match.");
    } else {

        if (count($errors) == 0) {
            try {
                $password1 = password_hash($password1, PASSWORD_DEFAULT);
                UsersDB::register($username, $password1);
            } catch (PDOException $e) {
                array_push($errors, "Username already taken.");
            }
        }

        $_SESSION['username'] = $username;
        $_SESSION['id'] = UsersDB::getPassword($username)["id"];
    }

} else if (isset($_POST['login_user'])) {
    $username = $_POST["username"];
    $password = $_POST["password"];

    $query = UsersDB::getPassword($username);
    if ($username != $query["username"]) {
        array_push($errors, "Username does not exist.");
    } else {
        $hash = $query["password"];

        if (password_verify($password, $hash)) {
            $_SESSION['username'] = $username;
            $_SESSION['id'] = $query["id"];
            header("Location: index.php");
        } else {
            array_push($errors, "Wrong password.");
        }
    }
}