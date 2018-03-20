"use strict";

const participans = [];


function domRemoveParticipant(event) {
    const name = event.target.parentElement.children[0].innerHTML;
    const last = event.target.parentElement.children[1].innerHTML;
    const r = confirm("Do you wish to remove " + name + " " + last);
    if (r) {
        $(event.target.parentElement).remove();
    }
}

function domAddParticipant(participant) {
    $('#participant-table').find('tr:last').after('<tr class="participant-row"><td>' + participant.first + '</td><td>' + participant.last + '</td><td>' + participant.role + '</td></tr>');
    $(".participant-row").dblclick(domRemoveParticipant)
}

function addParticipant() {
    const first = $("#first").val();
    const last = $("#last").val();
    const role = $("#role").find(":selected").text();

    $("#first").val("");
    $("#last").val("");
    $("#role").val("Student");

    // Create participant object
    const participant = {
        first: first,
        last: last,
        role: role
    };

    //participans.push({id: num})


    // Add participant to the HTML
    domAddParticipant(participant);

    // Move cursor to the first name input field
    document.getElementById("first").focus();
}

//
// document.addEventListener("DOMContentLoaded", () => {
//     // This function is run after the page contents have been loaded
//     // Put your initialization code here
//     document.getElementById("addButton").onclick = addParticipant;
// });

//The jQuery way of doing it
$(document).ready(() => {
    $("#addButton").click(addParticipant)
});

