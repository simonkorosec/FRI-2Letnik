<nav>
    <div class="navBar" id="myNavBar">
        <a href="index.php" class="<?php if(basename($_SERVER['SCRIPT_NAME']) == 'index.php'){echo 'active'; }else { echo ''; } ?>">Domov</a>
        <a href="search.php" class="<?php if(basename($_SERVER['SCRIPT_NAME']) == 'search.php'){echo 'active'; }else { echo ''; } ?>">Iskanje</a>
        <a href="input.php" class="<?php if(basename($_SERVER['SCRIPT_NAME']) == 'input.php'){echo 'active'; }else { echo ''; } ?>">Vnos</a>
        <a href="list.php" class="<?php if(basename($_SERVER['SCRIPT_NAME']) == 'list.php'){echo 'active'; }else { echo ''; } ?>">Seznam Gora</a>
        <a href="about.php" class="<?php if(basename($_SERVER['SCRIPT_NAME']) == 'about.php'){echo 'active'; }else { echo ''; } ?>">About</a>
        <a href="javascript:void(0);" class="icon" onclick="menuDropDown()">&#9776;</a>
    </div>
</nav>