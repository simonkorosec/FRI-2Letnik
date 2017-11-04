class Vector4f {

	constructor(x, y, z) {
		this.koordinati = [x, y, z, 0.0];
	}

	static negate(input) {
		var x = -input.koordinati[0];
		var y = -input.koordinati[1];
		var z = -input.koordinati[2];
		return new Vector4f(x, y, z);
	}

	static add(input1, input2) {
		var x = input1.koordinati[0] + input2.koordinati[0];
		var y = input1.koordinati[1] + input2.koordinati[1];
		var z = input1.koordinati[2] + input2.koordinati[2];
		//var h = input1.koordinati[3] + input2.koordinati[3];

		return new Vector4f(x, y, z);;
	}

	static scalarProduct(input1, input2) {
		var x = input1 * input2.koordinati[0];
		var y = input1 * input2.koordinati[1];
		var z = input1 * input2.koordinati[2];
		return new Vector4f(x, y, z);
	}

	static dotProduct(input1, input2) {
		var rezultat = 0.0;
		for (var i = 0; i < input1.koordinati.length; i++) {
			rezultat += input1.koordinati[i] * input2.koordinati[i];
		}
		return rezultat;
	}

	static crossProduct(input1, input2) {
		var a = input1.koordinati;
		var b = input2.koordinati;

		var x = a[1] * b[2] - a[2] * b[1];
		var y = -(a[0] * b[2] - a[2] * b[0]);
		var z = a[0] * b[1] - a[1] * b[0];

		return new Vector4f(x, y, z);
	}

	static length(input) {
		var rezultat = 0.0;
		for (var i = 0; i < input.koordinati.length; i++) {
			rezultat += input.koordinati[i] * input.koordinati[i];
		}
		return Math.sqrt(rezultat);
	}

	static normalize(input) {
		var dolzina = Vector4f.length(input);
		var x = input.koordinati[0] / dolzina;
		var y = input.koordinati[1] / dolzina;
		var z = input.koordinati[2] / dolzina;

		return new Vector4f(x, y, z);
	}

	static project(input1, input2) {
		//TODO
	}

	static cosPhi(input1, input2) {
		var dol1 = Vector4f.length(input1);
		var dol2 = Vector4f.length(input2);

		var kot = input1.dotProduct(input1, input2) / (dol1 * dol2);
		return Math.acos(kot);
	}

}

var a = new Vector4f(1, 4, 8);
var b = new Vector4f(7, 2, 9);

var r = Vector4f.dotProduct(a, b);
console.log(r);