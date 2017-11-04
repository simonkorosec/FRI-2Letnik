function Vector4f(x, y, z, h) {
    this.koordinati = [x, y, z, h];

    this.negate = function(input) {
        var x = -input.koordinati[0];
        var y = -input.koordinati[1];
        var z = -input.koordinati[2];
        return new Vector4f(x, y, z, input.koordinati[3]);
    }

    this.add = function(input1, input2) {
        var x = input1.koordinati[0] + input2.koordinati[0];
        var y = input1.koordinati[1] + input2.koordinati[1];
        var z = input1.koordinati[2] + input2.koordinati[2];
        return new Vector4f(x, y, z, input1.koordinati[3]);
    }

    this.scalarProduct = function(input1, input2) {
        var x = input1 * input2.koordinati[0];
        var y = input1 * input2.koordinati[1];
        var z = input1 * input2.koordinati[2];
        return new Vector4f(x, y, z, input2.koordinati[3]);
    }

    this.dotProduct = function(input1, input2) {
        var rezultat = 0.0;
        for (var i = 0; i < input1.koordinati.length - 1; i++) {
            rezultat += input1.koordinati[i] * input2.koordinati[i];
        }
        return rezultat;
    }

    this.crossProduct = function(input1, input2) {
        var a = input1.koordinati;
        var b = input2.koordinati;

        var x = a[1] * b[2] - a[2] * b[1];
        var y = -(a[0] * b[2] - a[2] * b[0]);
        var z = a[0] * b[1] - a[1] * b[0];

        return new Vector4f(x, y, z, input1.koordinati[3]);
    }

    this.length = function(input) {
        var rezultat = 0.0;
        for (var i = 0; i < input.koordinati.length; i++) {
            rezultat += input.koordinati[i] * input.koordinati[i];
        }
        return Math.sqrt(rezultat);
    }

    this.normalize = function(input) {
        var dolzina = input.length(input);
        var x = input.koordinati[0] / dolzina;
        var y = input.koordinati[1] / dolzina;
        var z = input.koordinati[2] / dolzina;

        return new Vector4f(x, y, z, input.koordinati[3]);
    }

    this.project = function(input1, input2) {
        var temp = new Vector4f().dotProduct(input1, input2);
        temp = temp / Math.pow(new Vector4f().length(input2), 2);

        return new Vector4f().scalarProduct(temp, input2);
    }

    this.cosPhi = function(input1, input2) {
        var dol1 = input1.length(input1);
        var dol2 = input1.length(input2);

        var kot = input1.dotProduct(input1, input2) / (dol1 * dol2);
        return Math.acos(kot);
    }

}
