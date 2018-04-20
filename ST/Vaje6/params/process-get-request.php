<!DOCTYPE html>

<meta charset="utf-8">
<title>Processing GET parameters</title>
<?php 

// var_dump($var) simply outputs the contents of $var, its type and size
// var_dump($_GET);

    if ((isset($_GET["first_name"]) && !empty($_GET["first_name]"]))
        && (isset($_GET["last_name"]) && !empty($_GET["last_name"]))){
        $first_name = $_GET["first_name"];
        $last_name = $_GET["last_name"];
        $format2 = "H:i";
        $time = date($format2);

        echo "Hello $first_name $last_name, the time is $time.";
    } else {
        echo "Required parameters are missing.";
    }

?>
