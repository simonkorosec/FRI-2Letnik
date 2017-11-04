function Transformation() {
	var trMatrika = new Matrix4f([
		[1, 0, 0, 0],
		[0, 1, 0, 0],
		[0, 0, 1, 0],
		[0, 0, 0, 1]
	]);

	this.translate = function(input) {
		var tX = input.koordinati[0];
		var tY = input.koordinati[1];
		var tZ = input.koordinati[2];
		var trans = [
			[1, 0, 0, tX],
			[0, 1, 0, tY],
			[0, 0, 1, tZ],
			[0, 0, 0, 1]
		];

		trMatrika = Matrix4f.multiply(trMatrika, new Matrix4f(trans));
		//console.log(trMatrika);

	}

	this.scale = function(input) {
		var sX = input.koordinati[0];
		var sY = input.koordinati[1];
		var sZ = input.koordinati[2];
		var scl = [
			[sX, 0, 0, 0],
			[0, sY, 0, 0],
			[0, 0, sZ, 0],
			[0, 0, 0, 1]
		];

		trMatrika = Matrix4f.multiply(trMatrika, new Matrix4f(scl));
		//console.log(trMatrika);
	}

	this.rotateX = function(input) {
		var kot = input;
		var cK = Math.cos(kot);
		var sK = Math.sin(kot);
		var rot = [
			[1, 0, 0, 0],
			[0, cK, -sK, 0],
			[0, sK, cK, 0],
			[0, 0, 0, 1]
		];

		trMatrika = Matrix4f.multiply(trMatrika, new Matrix4f(rot));
		//console.log(trMatrika);
	}

	this.rotateY = function(input) {
		var kot = input;
		var cK = Math.cos(kot);
		var sK = Math.sin(kot);
		var rot = [
			[cK, 0, sK, 0],
			[0, 1, 0, 0],
			[-sK, 0, cK, 0],
			[0, 0, 0, 1]
		];

		trMatrika = Matrix4f.multiply(trMatrika, new Matrix4f(rot));
		//console.log(trMatrika);
	}

	this.rotateZ = function(input) {
		var kot = input;
		var cK = Math.cos(kot);
		var sK = Math.sin(kot);
		var rot = [
			[cK, -sK, 0, 0],
			[sK, cK, 0, 0],
			[0, 0, 1, 0],
			[0, 0, 0, 1]
		];

		trMatrika = Matrix4f.multiply(trMatrika, new Matrix4f(rot));
		//console.log(trMatrika);
	}

	this.transformPoint = function(input) {
		var nove = [0, 0, 0, 0];
		var temp = 0;
		for (var i = 0; i < trMatrika.matrika.length; i++) {
			temp = 0;
			for (var j = 0; j < trMatrika.matrika[i].length; j++) {
				temp += trMatrika.matrika[i][j] * input.koordinati[j]
			}
			nove[i] = temp;
		}

		return new Vector4f(nove[0], nove[1], nove[2], nove[3]);
	}

}
