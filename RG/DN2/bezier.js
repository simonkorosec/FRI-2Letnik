var canvas;
var ctx;
var pozX = 0;
var pozY = 0;
var tocke = [];

class tocka {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }

}

function priprava() {
    canvas = document.getElementById("myCanvas");
    ctx = canvas.getContext("2d");
    ctx.strokeStyle = "#000000";
}

function vnosTock(event) {
    var x = event.offsetX;
    var y = event.offsetY;
    pozX = x;
    pozY = y;
    tocke.push(new tocka(x, y));
    //console.log(tocke.length % 4);
    if (tocke.length % 4 == 1 || tocke.length % 4 == 0)
        ctx.strokeRect(x-3, y-3, 6, 6);
    else {
        ctx.beginPath();
        ctx.arc(x, y, 4, 0, 2 * Math.PI)
        ctx.stroke();
    }
    //console.log("x coords: " + pozX + ", y coords: " + pozY);

    if (tocke.length % 4 == 0)
        izrisKrivulje();

}

function izrisKrivulje() {
    let p0 = tocke[tocke.length - 4];
    let p1 = tocke[tocke.length - 3];
    let p2 = tocke[tocke.length - 2];
    let p3 = tocke[tocke.length - 1];

    ctx.beginPath();
    ctx.moveTo(p0.x, p0.y);
    ctx.bezierCurveTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
    ctx.stroke();
}