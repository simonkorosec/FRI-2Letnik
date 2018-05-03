<?php
/**
 * Used to register a new user or check if given user name and password match.
 *
 * Created by PhpStorm.
 * User: Simon Korošec
 * Date: 24. 04. 2018
 * Time: 19:15
 */

require_once "DBInit.php";

class UsersDB {

    public static function register($user_name, $hash_password){
        $db = DBInit::getInstance();

        $statement = $db->prepare("INSERT INTO `users`(`username`, `password`) VALUES (:username, :password);");
        $statement->bindParam(":username", $user_name);
        $statement->bindParam(":password", $hash_password);
        $statement->execute();

    }

    public static function getPassword($user_name){
        $db = DBInit::getInstance();

        $statement = $db->prepare("SELECT `id`, `username`,`password` FROM `users` WHERE `username` = :username  LIMIT 1;");
        $statement->bindParam(":username", $user_name);
        $statement->execute();

        return $statement->fetch();
    }

}