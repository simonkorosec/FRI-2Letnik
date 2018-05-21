"use strict";

let name = "";
let vpisnaSt = "";
let naslov = "";
let email = "";
let gsm = "";
let letnikVpisa = "";
let letnik = "";
let program = "";
let vrstaPriloge = "";
let casPrekinitve = "";

let errors = [];

function menuDropDown() {
    const x = document.getElementById("myNavBar");
    if (x.className === "navBar") {
        x.className += " responsive";
    } else {
        x.className = "navBar";
    }
}

function validName() {
    name = $("#name").val().trim();

    if (name.split(" ").length < 2) {
        errors.push("Vnesite ime in priimek");
        //$("#name").setCustomValidity("Vnesite ime in priimek");
        name = "";
        return false;
    }
    return true;
}

function validVpisna() {
    vpisnaSt = $("#vpisnaSt").val().trim();

    let pattern = new RegExp("^63([0-9]{2})([0-9]{4})$");
    if (pattern.test(vpisnaSt)) {
        return true;
    } else {
        errors.push("Vpisna številka ni v skladu s FRI pravili.");
        vpisnaSt = "";
        return false;
        //$("#vpisnaSt").setCustomValidity("Vpisna številka ni v prave formatu");
    }
}

function validNaslov() {
    naslov = $("#naslov").val().trim();
}

function validLetnikVpisa() {
    letnikVpisa = $("#letnikVpisa").val().trim();

    let pattern = new RegExp("^(([0-9]{4})|([0-9]{2}))/(([0-9]{4})|([0-9]{2}))$");
    if (pattern.test(letnikVpisa)) {
        let letnica1 = letnikVpisa.split("/")[0];
        let letnica2 = letnikVpisa.split("/")[1];
        letnica1 = letnica1.substr(letnica1.length - 2);
        letnica2 = letnica2.substr(letnica2.length - 2);

        if (Number(letnica1) + 1 !== Number(letnica2)) {
            letnica2 = Number(letnica1) + 1;
        }
        if (letnica1 !== vpisnaSt.substr(2, 2)) {
            errors.push("Letnica vpisa in vpisna številka se ne ujemata.");
            letnikVpisa = "";
            return false;
        }
    } else {
        errors.push("Letnica vpisa ni v skladu z pravili.");
        letnikVpisa = "";
        return false;
    }
    return true;
}

function validLetnik() {
    letnik = $("#letnik").val();
}

function validProgram() {
    program = $("input[name=smer]:checked").val();
    if (program === "drugo") {
        program = $("#smer_drugo").val().trim();
        if (program === "") {
            errors.push("Vpišite veljaven program")
        }
    } else if (program === undefined) {
        errors.push("Izberite veljaven program")
    }
}

function validPrilogaVrsta() {
    vrstaPriloge = $("input[name=priloga]:checked").val();
    if (vrstaPriloge === "drugo") {
        vrstaPriloge = $("#prilogaDrugo").val().trim();
    } else if (vrstaPriloge === "1") {
        vrstaPriloge = "Potrdilo Olimpijskega komiteja Slovenije (OKS) o kategorizaciji statusa športnika";
    }

}

function validEmail() {
    email = $("#email").val().trim();
    if (name.split(" ").length !== 2) {
        errors.push("Email se ne ujema s pravili FRI.");
        return false;
    }

    let zacetek = name.split(" ")[0].charAt(0) + name.split(" ")[1].charAt(0);
    zacetek = zacetek.toLocaleLowerCase();
    let pattern = new RegExp("^" + zacetek + "[0-9]{4}");

    if (email.startsWith(zacetek) && pattern.test(email.split("@")[0]) && email.split("@")[1] === "student.uni-lj.si") {
        return true;
    } else {
        errors.push("Email se ne ujema s pravili FRI.");
        return false;
    }
}

function validGSM() {
    gsm = $("#gsm").val().trim();

    let pattern = new RegExp("^(([0-9]{3})([ /-]?)([0-9]{3})([ /-]?)([0-9]{3}))$");

    if (pattern.test(gsm)) {
        return true;
    } else {
        errors.push("Neveljavna telefonska številka");
        return false;
    }
}

function validCasPrek() {
    casPrekinitve = $("input[name=prekinitev]:checked").val();
}

function showErrors() {
    const x = document.getElementById("error");
    x.className = "error-show";

    if (errors.length > 0) {
        let errorList = $("#error-list");
        errorList.empty();
        for (let i in errors) {
            let li = "<li>" + errors[i] + "</li>";
            errorList.append(li);
        }
    } else {
        x.className = "error-hide";
    }
}

function validateFormSportnik() {
    validName();
    validVpisna();
    validLetnikVpisa();
    validLetnik();
    validNaslov();
    validProgram();
    validPrilogaVrsta();

    if (errors.length === 0) {
        $("#error").attr("class", "error-hide");
        let prosnja = {
            "name": name,
            "email": email,
            "vpisnaSt": vpisnaSt,
            "letnikVpisa": letnikVpisa,
            "letnik": letnik,
            "naslov": naslov,
            "program": program,
            "vrstaPriloge": vrstaPriloge
        };

        if (typeof(Storage) !== "undefined") {
            localStorage.setItem("prosnja", JSON.stringify(prosnja));
            console.log(JSON.stringify(prosnja));
        } else {
            alert("Sorry! No Web Storage support");
        }
    } else {
        showErrors();
    }
    errors = [];
}

function validateFormNad() {
    validGSM();
    validName();
    validEmail();
    validVpisna();
    validLetnikVpisa();
    validNaslov();
    validProgram();
    validCasPrek();

    if (errors.length === 0) {
        $("#error").attr("class", "error-hide");

        let prosnja = {
            "gsm": gsm,
            "name": name,
            "email": email,
            "vpisnaSt": vpisnaSt,
            "letnikVpisa": letnikVpisa,
            "naslov": naslov,
            "program": program,
            "casPrekinitve": casPrekinitve
        };

        if (typeof(Storage) !== "undefined") {
            localStorage.setItem("prosnja", JSON.stringify(prosnja));
        } else {
            alert("Sorry! No Web Storage support");
        }
    } else {
        showErrors();
    }
    errors = [];
}

function validateFormIzpis() {
    validName();
    validVpisna();
    validNaslov();
    validLetnikVpisa();
    validProgram();


    if (errors.length === 0) {
        $("#error").attr("class", "error-hide");
        let prosnja = {
            "name": name,
            "vpisnaSt": vpisnaSt,
            "letnikVpisa": letnikVpisa,
            "naslov": naslov,
            "program": program,
        };

        if (typeof(Storage) !== "undefined") {
            localStorage.setItem("prosnja", JSON.stringify(prosnja));
        } else {
            alert("Sorry! No Web Storage support");
        }
    } else {
        showErrors();
    }
    errors = [];
}

