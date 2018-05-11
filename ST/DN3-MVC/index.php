<?php
session_start();

require_once("controller/MountainController.php");
require_once("controller/UserController.php");

# Define a global constant pointing to the URL of the application
define("BASE_URL", $_SERVER["SCRIPT_NAME"] . "/");

# Request path after /index.php/ with leading and trailing slashes removed
$path = isset($_SERVER["PATH_INFO"]) ? trim($_SERVER["PATH_INFO"], "/") : "";

# The mapping of URLs. It is a simple array where:
# - keys represent URLs
# - values represent functions to be called when a client requests that URL
$urls = [
    "home" => function () {
        $_SESSION["currPage"] = "home";
        MountainController::home();
    },
    "search" => function () {
        $_SESSION["currPage"] = "search";
        MountainController::search();
    },
    "list" => function () {
        $_SESSION["currPage"] = "list";
        MountainController::list();
    },
    "about" => function () {
        $_SESSION["currPage"] = "about";
        MountainController::about();
    },
    "input" => function () {
        $_SESSION["currPage"] = "home";
        MountainController::input();
    },
    "login" => function () {
        $_SESSION["currPage"] = "login";
        UserController::login();
    },
    "register" => function () {
        $_SESSION["currPage"] = "register";
        UserController::register();
    },
    "parseLogin" => function () {
        UserController::parseLogin();
    },
    "parseRegister" => function () {
        UserController::parseRegister();
    },
    "logout" => function () {
        UserController::logout();
    },
    "parseNewMountain" => function () {
        MountainController::parseNewMountain();
    },
    "mountainDetails" => function () {
        echo "helolo";
    },
    "" => function () {
        $_SESSION["currPage"] = "home";
        ViewHelper::redirect(BASE_URL . "home");
    }
];

# The actual router.
# Tries to invoke the function that is mapped for the given path
try {
    if (isset($urls[$path])) {
        # Great, the path is defined in the router
        $urls[$path](); // invokes function that calls the controller
    } else {
        # Fail, the path is not defined. Show an error message.
        echo "No controller for '$path'";
    }
} catch (Exception $e) {
    # Provisional: whenever there is an exception, display some info about it
    # this should be disabled in production
    ViewHelper::error400($e);
} finally {
    exit();
}
