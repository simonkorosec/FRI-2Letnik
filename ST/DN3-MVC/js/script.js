"use strict";

function menuDropDown() {
    const x = document.getElementById("myNavBar");
    if (x.className === "navBar") {
        x.className += " responsive";
    } else {
        x.className = "navBar";
    }
}

function showDetails(event) {
    let id = JSON.parse(event.target.getAttribute("data-json-data"));
    window.location.href = "mountainDetails?id=" + id;
}

/*
function readMntDetails() {
    if (typeof(Storage) !== "undefined") {
        return JSON.parse(sessionStorage.getItem("mountainData"));
    } else {
        alert("Sorry! No Web Storage support");
    }
}
*/

function displayMountains(args) {
    /* Sort by mountain range id */
    if (args == null) {
        return;
    }
    args.sort((a, b) => a.range_id - b.range_id);


    const table = document.getElementById("tableList");
    let currRange = null;

    for (let i = 0; i < args.length; i++) {
        let mount = args[i];

        if (currRange === null || currRange !== mount.range_id) {
            const row = table.insertRow(-1);
            currRange = mount.range_id;
            row.classList.add("rangeName");
            const cell = row.insertCell(0);
            cell.innerHTML = mount.range_name;

        }
        const row = table.insertRow(-1);
        if (i % 2 === 0) {
            row.classList.add("row1")
        }
        else {
            row.classList.add("row2")
        }
        const cell = row.insertCell(0);
        cell.setAttribute("data-json-data", JSON.stringify(mount.id));
        cell.addEventListener("dblclick", showDetails);
        cell.innerHTML = mount.mountain_name;
    }
}

function walkTime(walkTime) {
    let h = String(Math.floor(walkTime / 60));
    let min = String(walkTime % 60);

    return "" + h + " h " + min + " min";
}

function displayMountainDetails(mnt) {
    const mountain = mnt[0];
    const table = document.getElementById("dtTable");

    document.getElementById("pgTitle").innerHTML = mountain.mountain_name;

    /* Gorovje */
    let row = table.insertRow(-1);
    row.classList.add("row1");
    let cell = row.insertCell(0);
    cell.classList.add("tdDetName");
    cell.innerHTML = "Gorovje:";
    cell = row.insertCell(1);
    cell.classList.add("tdDetContent");
    cell.innerHTML = mountain.range_name;

    /* Čas Hoje */
    row = table.insertRow(-1);
    row.classList.add("row2");
    cell = row.insertCell(0);
    cell.classList.add("tdDetName");
    cell.innerHTML = "Čas Hoje:";
    cell = row.insertCell(1);
    cell.classList.add("tdDetContent");
    cell.innerHTML = walkTime(mountain.walk_time);

    /* Višina Gore */
    row = table.insertRow(-1);
    row.classList.add("row1");
    cell = row.insertCell(0);
    cell.classList.add("tdDetName");
    cell.innerHTML = "Višina:";
    cell = row.insertCell(1);
    cell.classList.add("tdDetContent");
    cell.innerHTML = mountain.height + " m";

    /* Opis Gore */
    row = table.insertRow(-1);
    row.classList.add("row2");
    cell = row.insertCell(0);
    cell.classList.add("tdDetContent");
    cell.colSpan = 2;
    cell.innerHTML = mountain.description + "";

}

