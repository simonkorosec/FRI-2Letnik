function TransformPoints() {

    this.demo = function() {
        var pm = new PointManager();
        var t = new Transformation();

        var temp = pm.preberi();
        if (temp === null) {
        	return null;
        }

        // Priprava translacijske matrike
        var pX = new Vector4f(1.25, 0, 0, 0);
        var rZ = Math.PI / 3;
        var pZ = new Vector4f(0, 0, 4.15, 0);
        var pY = new Vector4f(0, 3.14, 0, 0);
        var s = new Vector4f(1.12, 1.12, 1, 0);
        var rY = (5 * Math.PI) / 8

        t.rotateY(rY);
        t.scale(s);
        t.translate(pY);
        t.translate(pZ);
        t.rotateZ(rZ);
        t.translate(pX);

        for (var i = 0; i < pm.vektorji.length; i++) {
            pm.vektorji[i] = t.transformPoint(pm.vektorji[i]);
        }

        pm.izpis();

        //console.log(typeof [5,3]);
    }
}