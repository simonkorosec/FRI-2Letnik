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

class Filter {
    constructor(range, name, heightMin, heightMax, walkTimeMin, walkTimeMax) {
        this.range = range;
        this.name = name;
        this.heightMin = heightMin;
        this.heightMax = heightMax;
        this.walkTimeMin = walkTimeMin;
        this.walkTimeMax = walkTimeMax;
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

function saveFilter(filter) {
    if (typeof(Storage) !== "undefined") {
        sessionStorage.setItem("filter", JSON.stringify(filter));
    } else {
        alert("Sorry! No Web Storage support");
    }
}

function readFilter() {
    if (typeof(Storage) !== "undefined") {
        return JSON.parse(sessionStorage.getItem("filter"));
    } else {
        alert("Sorry! No Web Storage support");
    }

}

function resetFilter() {
    if (typeof(Storage) !== "undefined") {
        sessionStorage.removeItem("filter");
    } else {
        alert("Sorry! No Web Storage support");
    }

}

function saveMntDetails(event) {
    if (typeof(Storage) !== "undefined") {
        sessionStorage.setItem("mountainData", event.target.getAttribute("data-json-data"));
        window.location.href = "mountainDetails.html";
    } else {
        alert("Sorry! No Web Storage support");
    }
}

function readMntDetails() {
    if (typeof(Storage) !== "undefined") {
        return JSON.parse(sessionStorage.getItem("mountainData"));
    } else {
        alert("Sorry! No Web Storage support");
    }
}

function fitsFilter(filter, mountain) {
    let fits = true;

    if (filter.range !== 0 && mountain.range !== filter.range) {
        return false;
    }
    if (filter.heightMin !== 0 && mountain.height <= filter.heightMin) {
        return false;
    }
    if (filter.heightMax !== 0 && mountain.height >= filter.heightMax) {
        return false;
    }
    if (filter.walkTimeMin !== 0 && mountain.walkTime <= filter.walkTimeMin) {
        return false;
    }
    if (filter.walkTimeMax !== 0 && mountain.walkTime >= filter.walkTimeMax) {
        return false;
    }
    if (filter.name !== "" && mountain.name.toLowerCase() !== filter.name.toLowerCase()){
        return false;
    }

    return fits;
}

function saveBasicSearch() {
    const name =document.getElementById("mountainName").value;
    saveFilter(new Filter(0, name, 0, 0, 0, 0));
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
        height = Number(height);

    }
    walkTime = walkTime.split(":");
    walkTime = Number(walkTime[0]) * 60 + Number(walkTime[1]);

    readMountains();
    mountains.push(new Mountain(range, name, height, walkTime, description));
    saveMountains();

    alert("Gora uspešno dodana.")
}

function displayMountains() {
    /* Sort by mountain range id */
    readMountains();
    mountains.sort((a, b) => a.range - b.range);

    /* Read filter if exists */
    const filter = readFilter();


    const table = document.getElementById("tableList");
    let currRange = null;

    for (let i = 0; i < mountains.length; i++) {
        let mount = mountains[i];

        if (filter !== null && fitsFilter(filter, mount) === false) {
            continue;
        }

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
        cell.setAttribute("data-json-data", JSON.stringify(mount));
        cell.addEventListener("dblclick", saveMntDetails);
        cell.innerHTML = mount.name;
    }
    resetFilter();
}

function newSearchFilter() {
    const range = Number(document.getElementById("mountainRangeId").value);
    const name = document.getElementById("mountainName").value;
    const heightMin = Number(document.getElementById("mountainHeightMin").value);
    const heightMax = Number(document.getElementById("mountainHeightMax").value);
    const walkTimeMin = Number(document.getElementById("mountainWalkMin").value);
    const walkTimeMax = Number(document.getElementById("mountainWalkMax").value);

    let filter = new Filter(range, name, heightMin, heightMax, walkTimeMin, walkTimeMax);
    saveFilter(filter);
}

function walkTime(walkTime) {
    let h = String(Math.floor(walkTime / 60));
    let min = String(walkTime % 60);

    return "" + h + " h " + min + " min";
}

function displayMountainDetails() {
    const mountain = readMntDetails();
    const table = document.getElementById("dtTable");

    document.getElementById("pgTitle").innerHTML = mountain.name;

    /* Gorovje */
    let row = table.insertRow(-1);
    row.classList.add("row1");
    let cell = row.insertCell(0);
    cell.classList.add("tdDetName");
    cell.innerHTML = "Gorovje:";
    cell = row.insertCell(1);
    cell.classList.add("tdDetContent");
    cell.innerHTML = mountainRanges[mountain.range];

    /* Čas Hoje */
    row = table.insertRow(-1);
    row.classList.add("row2");
    cell = row.insertCell(0);
    cell.classList.add("tdDetName");
    cell.innerHTML = "Čas Hoje:";
    cell = row.insertCell(1);
    cell.classList.add("tdDetContent");
    cell.innerHTML = walkTime(mountain.walkTime);

    /* Višina Gore */
    row = table.insertRow(-1);
    row.classList.add("row1");
    cell = row.insertCell(0);
    cell.classList.add("tdDetName");
    cell.innerHTML = "Višina:";
    cell = row.insertCell(1);
    cell.classList.add("tdDetContent");
    cell.innerHTML = mountain.height + "m";

    /* Opis Gore */
    row = table.insertRow(-1);
    row.classList.add("row2");
    cell = row.insertCell(0);
    cell.classList.add("tdDetContent");
    cell.colSpan = 2;
    cell.innerHTML = mountain.description + "";

}

function myFunction() {
    const x = document.getElementById("myNavBar");
    if (x.className === "navBar") {
        x.className += " responsive";
    } else {
        x.className = "navBar";
    }
}