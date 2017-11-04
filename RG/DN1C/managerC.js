class PointManager {

	static preberi() {
		// Dobim tekst iz polja in ga ločim po novih vrsticah
		var vnos = document.getElementById("text1").value.split("\n");
		//console.log(vnos);

		for (var i = 0; i < vnos.length; i++) {
			var vrstica = vnos[i].split(" ");
			if (vrstica.length === 4 && vrstica[0] === "v") {
				var x = parseFloat(vrstica[1]);
				var y = parseFloat(vrstica[2]);
				var z = parseFloat(vrstica[3]);

				//console.log(x);
				var v = new Vector4f(x, y, z);
				vektorji.push(v);

			}
		}
		//console.log("ja");
	}

	static izpis() {
		var besedilo = "";
		//console.log(vektorji.length);
		for (var i = 0; i < vektorji.length; i++) {
			var str = "v " + vektorji[i].koordinati[0] + " " + vektorji[i].koordinati[1] + " " + vektorji[i].koordinati[2] + "\n";
			besedilo = besedilo.concat(str);
		}
		document.getElementById("text2").value = besedilo;
	}

}

var vektorji = [];
