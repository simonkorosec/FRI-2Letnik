let canvas;
let ctx;
let pozX = 0;
let pozY = 0;
let tocke = [];

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
    let x = event.offsetX;
    let y = event.offsetY;
    pozX = x;
    pozY = y;
    tocke.push(new tocka(x, y));
    //console.log(tocke.length % 4);
    if (tocke.length % 4 === 1 || tocke.length % 4 === 0)
        ctx.strokeRect(x - 3, y - 3, 6, 6);
    else {
        ctx.beginPath();
        ctx.arc(x, y, 4, 0, 2 * Math.PI);
        ctx.stroke();
    }
    //console.log("x coords: " + pozX + ", y coords: " + pozY);

    if (tocke.length % 4 === 0)
        izrisKrivulje();

}

function izrisKrivulje() {
    let p0 = tocke[tocke.length - 4];
    let p1 = tocke[tocke.length - 3];
    let p2 = tocke[tocke.length - 2];
    let p3 = tocke[tocke.length - 1];
    let bezMat = [[1, -3, 3, -1],
                  [0, 3, -6, 3],
                  [0, 0, 3, -3],
                  [0, 0, 0, 1]];

    let pMat = [[p0.x,p1.x,p2.x,p3.x],
                [p0.y,p1.y,p2.y,p3.y]];

    let priprava = zmnoziMatriki(pMat, bezMat);


    let prejsnaTocka = p0;
    let novaTocka;


    for (let t = 0; t <= 1 ; t += 0.02) {
        ctx.beginPath();
        let tMat = [[1], [t], [Math.pow(t, 2)], [Math.pow(t, 3)]];
        let tmp = zmnoziMatriki(priprava, tMat);
        novaTocka = new tocka(tmp[0][0], tmp[1][0]);
        ctx.moveTo(prejsnaTocka.x, prejsnaTocka.y);
        ctx.lineTo(novaTocka.x, novaTocka.y);
        ctx.stroke();
        prejsnaTocka = novaTocka;
    }
}

function zmnoziMatriki(input1, input2) {

    let nova = [];
    for (let i = 0; i < input1.length; i++) {
        nova[i] = [];
        for (let j = 0; j < input2[i].length; j++) {
            let rez = 0;
            for (let k = 0; k < input1[i].length; k++) {
                rez += input1[i][k] * input2[k][j];
            }
            nova[i].push(rez);
        }
    }
    return nova;
}