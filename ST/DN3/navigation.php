
<nav>
    <div class="navBar" id="myNavBar">
        <a href="index.php" class="<?php if(basename($_SERVER['SCRIPT_NAME']) == 'index.php'){echo 'active'; }else { echo ''; } ?>">Domov</a>
        <a href="search.php" class="<?php if(basename($_SERVER['SCRIPT_NAME']) == 'search.php'){echo 'active'; }else { echo ''; } ?>">Iskanje</a>

        <?php if (isset($_SESSION["username"])) :?>
        <a href="input.php" class="<?php if(basename($_SERVER['SCRIPT_NAME']) == 'input.php'){echo 'active'; }else { echo ''; } ?>">Vnos</a>
        <?php endif; ?>

        <a href="list.php" class="<?php if(basename($_SERVER['SCRIPT_NAME']) == 'list.php'){echo 'active'; }else { echo ''; } ?>">Seznam Gora</a>
        <a href="about.php" class="<?php if(basename($_SERVER['SCRIPT_NAME']) == 'about.php'){echo 'active'; }else { echo ''; } ?>">About</a>


        <?php if (isset($_SESSION["username"])) :?>
            <a href="php/logout.php" class="login active">Odjava</a>
        <?php else :?>
            <a href="register.php" class="login <?php if(basename($_SERVER['SCRIPT_NAME']) == 'register.php'){echo 'active'; }else { echo ''; } ?>">Registracija</a>
            <a href="login.php" class="login <?php if(basename($_SERVER['SCRIPT_NAME']) == 'login.php'){echo 'active'; }else { echo ''; } ?>">Prijava</a>
        <?php endif; ?>

        <a href="javascript:void(0);" class="icon" onclick="menuDropDown()">&#9776;</a>
    </div>
</nav>