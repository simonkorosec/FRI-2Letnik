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

    if (name.split(" ").length !== 2) {
        errors.push("Vnesite ime in priimek");
        //$("#name").setCustomValidity("Vnesite ime in priimek");
        name = "";
    }
}

function validVpisna() {
    vpisnaSt = $("#vpisnaSt").val().trim();

    let pattern = new RegExp("^63([0-9]{2})([0-9]{4})$");
    if (pattern.test(vpisnaSt)) {
        //console.log(vpisnaSt);
    } else {
        errors.push("Vpisna številka ni v skladu s FRI pravili.");
        vpisnaSt = "";
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
        }
    } else {
        errors.push("Letnica vpisa in vpisna številka se ne ujemata.");
        letnikVpisa = "";
    }
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
        return;
    }

    let zacetek = name.split(" ")[0].charAt(0) + name.split(" ")[1].charAt(0);
    zacetek = zacetek.toLocaleLowerCase();
    let pattern = new RegExp("^" + zacetek + "[0-9]{4}");

    if (email.startsWith(zacetek) && pattern.test(email.split("@")[0]) && email.split("@")[1] === "student.uni-lj.si") {
        //console.log(email);
    } else {
        errors.push("Email se ne ujema s pravili FRI.");
    }

}

function validGSM() {
    gsm = $("#gsm").val().trim();

    let pattern = new RegExp("^(([0-9]{3})([ /-]?)([0-9]{3})([ /-]?)([0-9]{3}))$");

    if (pattern.test(gsm)) {
        console.log(gsm);
    } else {
        errors.push("Neveljavna telefonska številka");
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

    showErrors();
    if (errors.length === 0) {
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
        } else {
            alert("Sorry! No Web Storage support");
        }
    } else {
        errors = [];
        $("#error").attr("class", "error-hide");
    }
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
        // const x = document.getElementById("error");
        // x.className = "error-hide";

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

    showErrors();
    if (errors.length === 0) {
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
        $("#error").attr("class", "error-hide");
    }
    errors = [];
}

