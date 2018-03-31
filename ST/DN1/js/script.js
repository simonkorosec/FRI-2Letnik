"use strict";

class Mountain {

    constructor(range, name, height, walkTime, description) {
        this.range = range;
        this.name = name;
        this.height = height;
        this.walkTime = walkTime;
        this.description = description;
    }

}

let mountains = [];
let mountainRanges = {
    1: "Goriško, Notranjsko in Snežniško hribovje",
    2: "Julijske Alpe",
    3: "Kamniško Savinjske Alpe",
    4: "Karavanke",
    5: "Pohorje in ostala severovzhodna Slovenija",
    6: "Polhograjsko hribovje in Ljubljana",
    7: "Škofjeloško, Cerkljansko hribovje in Jelovica",
    8: "Zasavsko - Posavsko hribovje in Dolenjska"
};


function readMountains() {
    if (typeof(Storage) !== "undefined") {
        mountains = JSON.parse(localStorage.getItem("mountains"));
    } else {
        alert("Sorry! No Web Storage support");
    }

}

function saveMountains() {
    if (typeof(Storage) !== "undefined") {
        localStorage.setItem("mountains", JSON.stringify(mountains));
    } else {
        alert("Sorry! No Web Storage support");
    }

}

function saveBasicSearch() {
    sessionStorage.setItem("mountainName", document.getElementById("mountainName").value);
}

function inputNewMountain() {
    const range = Number(document.getElementById("mountainRangeId").value);
    const name = document.getElementById("mountainName").value;
    let height = document.getElementById("mountainHeight").value;
    let walkTime = document.getElementById("mountainWalkTime").value;
    const description = document.getElementById("hillDescription").value;
    if (range === 0 || name === "" || height === 0 || walkTime === "" || description === "") {
        return;
    }

    if (height.includes("m")) {

        height = height.replace('m', '');
    }
    walkTime = walkTime.split(":");
    walkTime = Number(walkTime[0]) * 60 + Number(walkTime[1]);

    readMountains();
    mountains.push(new Mountain(range, name, height, walkTime, description));
    saveMountains();

    alert("Gora uspešno dodana.")
}

function displayMountains() {
    readMountains();
    mountains.sort((a, b) => a.range - b.range);
    /* Sort by mountain range id */

    const table = document.getElementById("tableList");
    let currRange = null;

    for (let i = 0; i < mountains.length; i++) {
        let mount = mountains[i];
        //if (mount instanceof Mountain) {
            if (currRange === null || currRange !== mount.range) {
                const row = table.insertRow(-1);
                currRange = mount.range;
                row.classList.add("rangeName");
                const cell = row.insertCell(0);
                cell.innerHTML = mountainRanges[currRange];

            }
            const row = table.insertRow(-1);
            if (i % 2 === 0) {
                row.classList.add("row1")
            }
            else {
                row.classList.add("row2")
            }

            const cell = row.insertCell(0);
            cell.innerHTML = mount.name;
        //}
    }

}


function saveData() {
    sessionStorage.setItem("neki", document.getElementById("neki").value);
}

function myFunction() {
    const x = document.getElementById("myNavBar");
    if (x.className === "navBar") {
        x.className += " responsive";
    } else {
        x.className = "navBar";
    }
}