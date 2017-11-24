let canvas;
let ctx;
let dropDwn;
let tocke = [];
let zlepki = [];
let bezMat = [[1, -3, 3, -1],
    [0, 3, -6, 3],
    [0, 0, 3, -3],
    [0, 0, 0, 1]];


class Tocka {
    constructor(x, y) {
        this.x = x;
        this.y = y;
        this.vidna = true;
        this.interpolerana = false;
    }

    izrisKvadrata() {
        ctx.strokeRect(this.x - 3, this.y - 3, 6, 6);
        this.interpolerana = true;
    }

    izrisKroga() {
        ctx.beginPath();
        ctx.arc(this.x, this.y, 4, 0, 2 * Math.PI);
        ctx.stroke();

    }
}

class BezierKrivulja {

    constructor(p0, p1, p2, p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.vidna = true;
        this.barva = "#000000";
    }

    draw() {
        let pMat = [[this.p0.x, this.p1.x, this.p2.x, this.p3.x],
            [this.p0.y, this.p1.y, this.p2.y, this.p3.y]];

        let priprava = zmnoziMatriki(pMat, bezMat);

        let prejsnaTocka = this.p0;
        let novaTocka;

        for (let t = 0; t <= 1; t += 0.02) {
            ctx.beginPath();
            let tMat = [[1], [t], [Math.pow(t, 2)], [Math.pow(t, 3)]];
            let tmp = zmnoziMatriki(priprava, tMat);
            novaTocka = new Tocka(tmp[0][0], tmp[1][0]);
            ctx.moveTo(prejsnaTocka.x, prejsnaTocka.y);
            ctx.lineTo(novaTocka.x, novaTocka.y);
            ctx.strokeStyle = this.barva;
            ctx.stroke();
            prejsnaTocka = novaTocka;
        }

        // Poskrbimo da so točke res vidne
        this.p0.vidna = true;
        this.p1.vidna = true;
        this.p2.vidna = true;
        this.p3.vidna = true;

    }

    odstrani() {
        this.p0.vidna = false;
        this.p1.vidna = false;
        this.p2.vidna = false;
        this.p3.vidna = false;
        this.vidna = false;
    }

    spremeniBarvo(b) {
        this.barva = b;
    }
}

function priprava() {
    dropDwn = document.getElementById("izborKrivulj");
    canvas = document.getElementById("myCanvas");
    ctx = canvas.getContext("2d");
    ctx.strokeStyle = "#000000";
}

function izrisTocke(toc) {
    if (tocke.length % 3 === 1 )
        toc.izrisKvadrata();
    else
        toc.izrisKroga();
}

function vnosTock(event) {
    let x = event.offsetX;
    let y = event.offsetY;
    let toc = new Tocka(x, y);
    tocke.push(toc);
    izrisTocke(toc);

    if (tocke.length > 3 && tocke.length % 3 === 1)
        izrisKrivulje();

}

function dotProduct(v1, v2) {
    let rez = 0.0;
    for(let i = 0; i < v1.length; i++) {
        rez += v1[i] * v2[i];
    }
    return rez;
}

function izrisZlepka() {
    let prvaK = zlepki[zlepki.length - 2];
    let drugaK = zlepki[zlepki.length - 1];

    let a = prvaK.p2;
    let b = drugaK.p1;
    let p = prvaK.p3;

    let AB = [b.x - a.x, b.y - a.y];
    let AP = [p.x - a.x, p.y - a.y];

    let tmp = dotProduct(AP, AB) / dotProduct(AB, AB);
    let premik = [0, 0];
    premik[0] = tmp * AB[0];
    premik[1] = tmp * AB[1];

    p.x = a.x + premik[0];
    p.y = a.y + premik[1];

    ponovniIzris();
}

function izrisKrivulje() {
    let p0 = tocke[tocke.length - 4];
    let p1 = tocke[tocke.length - 3];
    let p2 = tocke[tocke.length - 2];
    let p3 = tocke[tocke.length - 1];
    let b = new BezierKrivulja(p0, p1, p2, p3);
    b.draw();
    zlepki.push(b);

    if (zlepki.length > 1) {
        izrisZlepka();
    }

    posodobiDropDown();

}

function posodobiDropDown() {
    dropDwn.innerHTML = "";
    let opt = document.createElement("option");
    opt.innerHTML = "Izberite krivuljo";
    opt.selected = true;
    opt.disabled = true;
    dropDwn.appendChild(opt);

    for (let i = 0; i < zlepki.length; i++) {
        let option = document.createElement("option");
        option.innerHTML = "Krivulja " + i;
        option.value = i;
        dropDwn.appendChild(option);
    }
}

function ponovniIzris() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Ponoven izris krivulj
    for (let i = 0; i < zlepki.length;) {
        if (zlepki[i].vidna) {
            zlepki[i].draw();
            i++;
        }
        else {
            zlepki.splice(i, 1);
        }
    }

    // Ponoven izris točk
    for (let i = 0; i < tocke.length;) {
        let tocke2 = tocke[i];
        if (tocke2.vidna) {
            if (tocke2.interpolerana) {
                tocke2.izrisKvadrata();
            } else {
                tocke2.izrisKroga();
            }
            i++;
        } else {
            tocke.splice(i, 1);
        }

    }
}

function odstraniKrivuljo() {
    let indeks = dropDwn.value;
    let krivulja = zlepki[indeks];
    //console.log(zlepki[indeks]);

    krivulja.odstrani();
    ponovniIzris();

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