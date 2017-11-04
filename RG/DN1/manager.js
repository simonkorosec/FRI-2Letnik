function PointManager() {

    this.vektorji = [];

    this.preberi = function() {
        // Dobim tekst iz polja in ga ločim po novih vrsticah
        var vnos = document.getElementById("text1").value.split("\n");
        //console.log(vnos);

        for (var i = 0; i < vnos.length; i++) {
            var vrstica = vnos[i].split(" ");
            if (vrstica.length === 4 && vrstica[0] === "v") {
                var x = parseFloat(vrstica[1]);
                var y = parseFloat(vrstica[2]);
                var z = parseFloat(vrstica[3]);

                if (isNaN(x) || isNaN(y) || isNaN(z)) {
                    alert("Vektor sprejema samo številke.");
                    return null;
                } else {
                    var v = new Vector4f(x, y, z, 1);
                    this.vektorji.push(v);
                }
                //console.log(x);

            }
        }
        //console.log(this.vektorji);
    }

    this.izpis = function() {
        var besedilo = "";
        //console.log(vektorji.length);
        for (var i = 0; i < this.vektorji.length; i++) {
            var str = "v " + this.vektorji[i].koordinati[0] + " " +
                this.vektorji[i].koordinati[1] + " " +
                this.vektorji[i].koordinati[2] + "\n";
            besedilo = besedilo.concat(str);
        }
        document.getElementById("text2").value = besedilo;
    }

}