<!DOCTYPE html>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" type="text/css" href="style.css"/>
<title>Web technologies</title>

<header>Web technologies</header>

<div id="content1">
    <form action="add.php" method="post">
        <fieldset>
            <legend>Add a new student</legend>
            <label for="first_add">First name: </label>
            <input id="first_add" type="text" name="first" required/>
            <label for="last_add">Last name: </label>
            <input id="last_add" type="text" name="last" required/>
            <button>Add participant</button>
        </fieldset>
    </form>
    <form action="index.php" method="get">
        <fieldset>
            <legend>Search for students</legend>
            <label for="number_search">Number: </label>
            <input id="number_search" type="number" min="1" name="number"/>
            <label for="first_search">First name: </label>
            <input id="first_search" type="text" name="first"/>
            <label for="last_search">Last name: </label>
            <input id="last_search" type="text" name="last"/>
            <button>Search</button>
        </fieldset>
    </form>
</div>

<div id="content2">
    <table>
        <tr>
            <th>Number</th>
            <th>First name</th>
            <th>Last name</th>
        </tr>

        <?php
        require_once("UserDB.php");

        $query = ["number" => $_GET["number"] ?? "", "first" => $_GET["first"] ?? "", "last" => $_GET["last"] ?? ""];
        $hits = UserDB::read_from_db($query);

        foreach ($hits as $hit):
            ?>

            <tr>
                <td><?php echo $hit["number"] ?></td>
                <td><?php echo $hit["first"] ?></td>
                <td><?php echo $hit["last"] ?></td>
            </tr>

        <?php endforeach; ?>

    </table>
</div>

<footer>Web technologies @ FRI</footer>