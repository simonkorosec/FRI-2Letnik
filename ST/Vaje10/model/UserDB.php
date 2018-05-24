<?php

require_once "DBInit.php";

class UserDB {

    // Returns true if a valid combination of a username and a password are provided.
    public static function validLoginAttempt($username, $password) {
        $dbh = DBInit::getInstance();

        // !!! NEVER CONSTRUCT SQL QUERIES THIS WAY !!!
        // INSTEAD, ALWAYS USE PREPARED STATEMENTS AND BIND PARAMETERS!
        $query = "SELECT COUNT(id) FROM user WHERE username = ':usr' AND password = ':pass'";
        $stmt = $dbh->prepare($query);
        $stmt->bindParam(":usr", $username);
        $stmt->bindParam(":pass", $password);
        $stmt->execute();

        return $stmt->fetchColumn(0) == 1;
    }
}
