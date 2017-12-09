let canvas;
let ctx;
let dropDwn;
let tockeZlepka = [];
let vseTocke =  [];
let krivulje = [];
let zlepki = [];
let stZlepkov = 1;
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

    }

    draw() {
        let pMat = [[this.p0.x, this.p1.x, this.p2.x, this.p3.x],
            [this.p0.y, this.p1.y, this.p2.y, this.p3.y]];

        let priprava = zmnoziMatriki(pMat, bezMat);

        let prejsnaTocka = this.p0;
        let novaTocka;

        for (let t = 0; t <= 1; t += 0.001) {
            ctx.beginPath();
            let tMat = [[1], [t], [Math.pow(t, 2)], [Math.pow(t, 3)]];
            let tmp = zmnoziMatriki(priprava, tMat);
            novaTocka = new Tocka(tmp[0][0], tmp[1][0]);
            ctx.moveTo(prejsnaTocka.x, prejsnaTocka.y);
            ctx.lineTo(novaTocka.x, novaTocka.y);
            ctx.stroke();
            prejsnaTocka = novaTocka;
        }

        // Poskrbimo da so točke res vidne
        this.p0.vidna = true;
        this.p1.vidna = true;
        this.p2.vidna = true;
        this.p3.vidna = true;

    }

    odstraniK() {
        this.p0.vidna = false;
        this.p1.vidna = false;
        this.p2.vidna = false;
        this.p3.vidna = false;
        this.vidna = false;
    }

}

class Zlepek {
    constructor() {
        this.deli = [];
        this.barva = "#000000";
        this.vidna = true;
    }

    dodajKrivuljo(krivulja) {
        this.deli.push(krivulja);
    }

    odstraniZ() {
        this.vidna = false;
        for (let i = 0; i < this.deli.length; i++) {
            this.deli[i].odstraniK()
        }
    }

    drawZlepek() {
        ctx.strokeStyle = this.barva;
        for (let i = 0; i < this.deli.length; i++) {
            this.deli[i].draw();
        }
        ctx.strokeStyle = "#000000";
    }

    spremeniBarvo(barva) {
        this.barva = barva;
    }

    popraviZveznost() {
        let prvaK = this.deli[this.deli.length - 2];
        let drugaK = this.deli[this.deli.length - 1];

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

}

function priprava() {
    dropDwn = document.getElementById("izborKrivulj");
    canvas = document.getElementById("myCanvas");
    ctx = canvas.getContext("2d");
    ctx.strokeStyle = "#000000";
}

function izrisTocke(toc) {
    if (tockeZlepka.length % 3 === 1)
        toc.izrisKvadrata();
    else
        toc.izrisKroga();
}

function vnosTock(event) {

    if (zlepki.length !== stZlepkov)
        zlepki.push(new Zlepek());

    let x = event.offsetX;
    let y = event.offsetY;
    let toc = new Tocka(x, y);
    tockeZlepka.push(toc);
    vseTocke.push(toc);
    izrisTocke(toc);

    if (tockeZlepka.length > 3 && tockeZlepka.length % 3 === 1)
        izrisKrivulje();

}

function dotProduct(v1, v2) {
    let rez = 0.0;
    for (let i = 0; i < v1.length; i++) {
        rez += v1[i] * v2[i];
    }
    return rez;
}

function izrisKrivulje() {
    let p0 = tockeZlepka[tockeZlepka.length - 4];
    let p1 = tockeZlepka[tockeZlepka.length - 3];
    let p2 = tockeZlepka[tockeZlepka.length - 2];
    let p3 = tockeZlepka[tockeZlepka.length - 1];
    let b = new BezierKrivulja(p0, p1, p2, p3);
    b.draw();
    krivulje.push(b);
    zlepki[stZlepkov - 1].dodajKrivuljo(b);

    if (krivulje.length > 1) {
        zlepki[stZlepkov - 1].popraviZveznost();
    }

    posodobiDropDown();

}

function posodobiDropDown() {
    dropDwn.innerHTML = "";
    let opt = document.createElement("option");
    opt.innerHTML = "Izberite krivuljo";
    opt.disabled = true;
    dropDwn.appendChild(opt);

    for (let i = 0; i < zlepki.length; i++) {
        let option = document.createElement("option");
        option.innerHTML = "Zlepek " + i;
        option.value = i;
        dropDwn.appendChild(option);
    }
}

function ponovniIzris() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);

    // Ponoven izris krivulj
    for (let i = 0; i < zlepki.length;) {
        if (zlepki[i].vidna) {
            zlepki[i].drawZlepek();
            i++;
        }
        else {
            zlepki.splice(i, 1);
        }
    }

    // Ponoven izris točk
    for (let i = 0; i < vseTocke.length;) {
        let tocke2 = vseTocke[i];
        if (tocke2.vidna) {
            if (tocke2.interpolerana) {
                tocke2.izrisKvadrata();
            } else {
                tocke2.izrisKroga();
            }
            i++;
        } else {
            vseTocke.splice(i, 1);
        }

    }
}

function odstraniZlepek() {
    let indeks = dropDwn.value;
    zlepki[indeks].odstraniZ();
    if (zlepki.length === 1){
        tockeZlepka = [];
        vseTocke =  [];
        krivulje = [];
        zlepki = [];
        stZlepkov = 1;
    }
    ponovniIzris();
    posodobiDropDown();
}

function novZlepek() {
    stZlepkov++;
    krivulje = [];
    tockeZlepka = [];
}

function spremeniBarvoZlepka() {
    let indeks = dropDwn.value;
    let barva = document.getElementById("colorSelect");
    zlepki[indeks].spremeniBarvo(barva.value);
    //zlepki[indeks].spremeniBarvo("#0ff0a5");
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