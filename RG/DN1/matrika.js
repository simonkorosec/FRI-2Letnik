function Matrix4f(matrika) {
	this.matrika = matrika;

	Matrix4f.negate = function(input) {
		var nova = [];

		for (var i = 0; i < input.matrika.length; i++) {
			nova[i] = [];
			for (var j = 0; j < input.matrika[i].length; j++) {
				nova[i].push(-input.matrika[i][j]);
			}
		}

		return new Matrix4f(nova);
	}

	Matrix4f.add = function(input1, input2) {
		var nova = [];

		for (var i = 0; i < input1.matrika.length; i++) {
			nova[i] = [];
			for (var j = 0; j < input1.matrika[i].length; j++) {
				nova[i].push(input1.matrika[i][j] + input2.matrika[i][j]);
			}
		}

		return new Matrix4f(nova);
	}

	Matrix4f.transpose = function(input) {
		var nova = [];

		for (var i = 0; i < input.matrika.length; i++) {
			nova[i] = [];
			for (var j = 0; j < input.matrika[i].length; j++) {
				nova[i].push(input.matrika[j][i]);
			}
		}

		return new Matrix4f(nova);
	}

	Matrix4f.multiplyScalar = function(input1, input2) {
		var nova = [];

		for (var i = 0; i < input2.matrika.length; i++) {
			nova[i] = [];
			for (var j = 0; j < input2.matrika[i].length; j++) {
				nova[i].push(input1 * input2.matrika[i][j]);
			}
		}

		return new Matrix4f(nova);
	}

	Matrix4f.multiply = function(input1, input2) {
		var nova = [];

		for (var i = 0; i < input1.matrika.length; i++) {
			nova[i] = [];
			for (var j = 0; j < input2.matrika[i].length; j++) {
				var rez = 0;
				for (var t = 0; t < input1.matrika[i].length; t++) {
					rez += input1.matrika[i][t] * input2.matrika[t][j];
				}
				nova[i].push(rez);
			}
		}

		return new Matrix4f(nova);
	}
}
