
<nav>
    <div class="navBar" id="myNavBar">
        <a href="<?=BASE_URL . "home"?>" class="<?php if($currentPage == 'home.php'){echo 'active'; }else { echo ''; } ?>">Domov</a>
        <a href="<?=BASE_URL . "search"?>" class="<?php if($currentPage == 'search.php'){echo 'active'; }else { echo ''; } ?>">Iskanje</a>

        <?php if (isset($_SESSION["username"]) && !empty($_SESSION["username"])) :?>
        <a href="<?=BASE_URL . "input"?>" class="<?php if($currentPage == 'input.php'){echo 'active'; }else { echo ''; } ?>">Vnos</a>
        <?php endif; ?>

        <a href="<?=BASE_URL . "list"?>" class="<?php if($currentPage == 'list.php'){echo 'active'; }else { echo ''; } ?>">Seznam Gora</a>
        <a href="<?=BASE_URL . "about"?>" class="<?php if($currentPage == 'about.php'){echo 'active'; }else { echo ''; } ?>">About</a>


        <?php if (isset($_SESSION["username"]) && !empty($_SESSION["username"])) :?>
            <a href="<?=BASE_URL . "logout"?>" class="login active">Odjava</a>
        <?php else :?>
            <a href="<?=BASE_URL . "register"?>" class="login <?php if($currentPage == 'register.php'){echo 'active'; }else { echo ''; } ?>">Registracija</a>
            <a href="<?=BASE_URL . "login"?>" class="login <?php if($currentPage == 'login.php'){echo 'active'; }else { echo ''; } ?>">Prijava</a>
        <?php endif; ?>

        <a href="javascript:void(0);" class="icon" onclick="menuDropDown()">&#9776;</a>
    </div>
</nav>