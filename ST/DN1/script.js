"use strict";

//The jQuery way of doing it
// $(document).ready(() => {
//
// });

function saveData() {
    sessionStorage.setItem("neki", document.getElementById("neki").value)
}

function myFunction() {
    const x = document.getElementById("myTopnav");
    if (x.className === "topnav") {
        x.className += " responsive";
    } else {
        x.className = "topnav";
    }
}