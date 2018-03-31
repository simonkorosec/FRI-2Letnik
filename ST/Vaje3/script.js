"use strict";

const participants = [];
let id = 0;

function domRemoveParticipant(event) {
    let index = $("#participant-table").find("tr").index(event.target.parentElement);

    const name = event.target.parentElement.children[0].innerHTML;
    const last = event.target.parentElement.children[1].innerHTML;
    const r = confirm("Do you wish to remove " + name + " " + last);
    if (r) {
        console.log(r);
        $(event.target.parentElement).remove();
        participants.splice(index-1, 1);
        saveToStorage();
    } else {
        return alert("User was not deleted.");
    }

}

function domAddParticipant(participant) {
    $('#participant-table').find('tr:last').after('<tr class="participant-row"><td>' + participant.first + '</td><td>' + participant.last + '</td><td>' + participant.role + '</td></tr>');
    $(".participant-row").dblclick(domRemoveParticipant);

    participants.push(participant);
    id++;
}

function addParticipant() {
    const f = $("#first");
    const l = $("#last");
    const r = $("#role");

    const first = f.val();
    const last = l.val();
    const role = r.find(":selected").text();

    f.val("");
    l.val("");
    r.val("Student");

    // Create participant object
    const participant = {
        id: id,
        first: first,
        last: last,
        role: role
    };

    // Add participant to the HTML
    domAddParticipant(participant);

    saveToStorage();

    // Move cursor to the first name input field
    document.getElementById("first").focus();
}

function saveToStorage() {
    if (typeof(Storage) !== "undefined") {
        localStorage.setItem("participants", JSON.stringify(participants));
    } else {
        alert("Sorry! No Web Storage support");
    }
}

function loadFromStorage() {
    if (typeof(Storage) !== "undefined") {
        const str = JSON.parse(localStorage.getItem("participants"));
        participants.splice(0, participants.length);
        for (let e of str) {
            domAddParticipant(e);
        }
    } else {
        alert("Sorry! No Web Storage support");
    }
}


//The jQuery way of doing it
$(document).ready(() => {
    $("#addButton").click(addParticipant);
    loadFromStorage()
});

