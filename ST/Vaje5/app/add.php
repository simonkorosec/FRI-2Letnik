<?php 

require_once("UserDB.php");
$first = $_POST["first"];
$last = $_POST["last"];
UsersDB::save_to_db($first, $last);

?>

<!DOCTYPE html>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="style.css" />

<title>Web technologies</title>

<header>Web technologies</header>

<div id="content1">
	<p>A new student has been added. <a href="index.php">Add another one.</a></p>
</div>

<footer>Web technologies @ FRI</footer>