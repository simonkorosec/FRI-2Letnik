var canvas;
var gl;
var shaderProgram;

var worldVertexPositionBuffer = null;
var worldVertexTextureCoordBuffer = null;
var doorVertexPositionBuffer = null;
var doorVertexTextureCoordBuffer = null;

var keyVertexPositionBuffer;
var keyVertexTextureCoordBuffer;
var keyVertexIndexBuffer;
var keyVertexPositionBuffer2;
var keyVertexTextureCoordBuffer2;
var keyVertexIndexBuffer2;

var healthVertexPositionBuffer;
var healthVertexTextureCoordBuffer;
var healthVertexIndexBuffer;
var healthVertexPositionBuffer2;
var healthVertexTextureCoordBuffer2;
var healthVertexIndexBuffer2;

var laserVertexPositionBuffer;
var laserVertexTextureCoordBuffer;
var laserVertexIndexBuffer;

var mvMatrixStack = [];
var mvMatrix = mat4.create();
var pMatrix = mat4.create();
var rotationCubeX = 0;
var rotationCubeY = 0;

var wallTexture;
var doorTexture;
var boxesTexture;
var redTexture;
var texturesLoaded = 0;
var numTextures = 4;

var currentlyPressedKeys = {};

var playingBump = false;
var currAudio = [];
var yStanding = 0.45;
var yCrouch = 0.2;
var yUP = 0.8;
var healthPoints = 100;

var pitch = 0;
var pitchRate = 0;
var yaw = 0;
var yawRate = 0;
var xPosition = -0.0;
var yPosition = 0.45;
var zPosition = -0.0;
var speed = 0;
var joggingAngle = 0;
var playerWidth = 0.14;
var playerHeight = 0.4;

var lastTime = 0;

var wallPositions = [];
var doorPositions = [];
var boxPositions = [];
var laserArray = [];

var gameStart = false;


function Wall() {
    this.points = [];

    this.isFull = function () {
        if (this.points.length >= 12) {
            this.points = this.points.slice(0, 4); // Only keep first 4 points rest are identical
            return true; // becuse we dont look at the Y axias with walls
        }
        return false;
    };

    this.checkCollision = function (playerBox) {
        var odmik = 0.13; // So the player can't see throught the wall when he turns near a wall

        var minX = Math.min(this.points[0], this.points[2]) - odmik;
        var maxX = Math.max(this.points[0], this.points[2]) + odmik;
        var minZ = Math.min(this.points[1], this.points[3]) - odmik;
        var maxZ = Math.max(this.points[1], this.points[3]) + odmik;

        return (playerBox.minZ <= maxZ && playerBox.maxZ >= minZ) && (playerBox.minX <= maxX && playerBox.maxX >= minX);
    };
}

function Door(keyNum) {
    this.points = [];
    this.keyNum = keyNum;

    this.isFull = function () {
        if (this.points.length >= 12) {
            this.points = this.points.slice(0, 4); // Only keep first 4 points rest are identical
            return true; // becuse we dont look at the Y axias with doors
        }
        return false;
    };

    this.checkCollision = function (playerBox) {
        var odmik = 0.13; // So the player can't see throught the wall when he turns near a wall

        var minX = Math.min(this.points[0], this.points[2]) - odmik;
        var maxX = Math.max(this.points[0], this.points[2]) + odmik;
        var minZ = Math.min(this.points[1], this.points[3]) - odmik;
        var maxZ = Math.max(this.points[1], this.points[3]) + odmik;

        var coll = (playerBox.minZ <= maxZ && playerBox.maxZ >= minZ) && (playerBox.minX <= maxX && playerBox.maxX >= minX);

        if (coll && boxPositions[this.keyNum].pickedUp) {
            return false;
        } else {
            return coll;
        }
    };
}

function Key(x, y, z, o) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.size = o;
    this.pickedUp = false;

    this.checkCollision = function (playerBox) {
        var odmik = 0.0;

        var minX = Math.min(this.x - this.size, this.x + this.size) - odmik;
        var maxX = Math.max(this.x - this.size, this.x + this.size) + odmik;
        var minZ = Math.min(this.z - this.size, this.z + this.size) - odmik;
        var maxZ = Math.max(this.z - this.size, this.z + this.size) + odmik;
        var minY = this.y - this.size - odmik;
        var maxY = this.y + this.size + odmik;

        this.pickedUp = (playerBox.minZ <= maxZ && playerBox.maxZ >= minZ) &&
            (playerBox.minX <= maxX && playerBox.maxX >= minX) &&
            (playerBox.minY <= maxY && playerBox.maxY >= minY);

        if (this.pickedUp) {
            playPickUp();
        }
    };
}

function HealthBox(x, y, z, o) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.size = o;
    this.pickedUp = false;

    this.checkCollision = function (playerBox) {
        var odmik = 0.0;

        var minX = Math.min(this.x - this.size, this.x + this.size) - odmik;
        var maxX = Math.max(this.x - this.size, this.x + this.size) + odmik;
        var minZ = Math.min(this.z - this.size, this.z + this.size) - odmik;
        var maxZ = Math.max(this.z - this.size, this.z + this.size) + odmik;
        var minY = this.y - this.size - odmik;
        var maxY = this.y + this.size + odmik;

        this.pickedUp = (playerBox.minZ <= maxZ && playerBox.maxZ >= minZ) &&
            (playerBox.minX <= maxX && playerBox.maxX >= minX) &&
            (playerBox.minY <= maxY && playerBox.maxY >= minY);

        if (this.pickedUp) {
            healthPoints += 15;
            playPickUp();
        }

    };
}

function Laser(x, y, z, s) {
    // X in Z koordinati morata biti negirani
    this.x = -x;
    this.y = y;
    this.z = -z;
    this.s = s;

    this.checkCollision = function (playerBox) {
        var odmik = 0.03;
        var minX;
        var maxX;
        var minZ;
        var maxZ;

        if (this.s === 0) {
            minX = Math.min(this.x, this.x) - 0.5;
            maxX = Math.max(this.x, this.x) + 0.5;
            minZ = Math.min(this.z, this.z) - odmik;
            maxZ = Math.max(this.z, this.z) + odmik;
        } else {
            minX = Math.min(this.x, this.x) - odmik;
            maxX = Math.max(this.x, this.x) + odmik;
            minZ = Math.min(this.z, this.z) - 0.5;
            maxZ = Math.max(this.z, this.z) + 0.5;
        }

        var minY = this.y - odmik;
        var maxY = this.y + odmik;

        return (playerBox.minZ <= maxZ && playerBox.maxZ >= minZ) &&
            (playerBox.minX <= maxX && playerBox.maxX >= minX) &&
            (playerBox.minY <= maxY && playerBox.maxY >= minY);
    };

}

function mvPushMatrix() {
    var copy = mat4.create();
    mat4.set(mvMatrix, copy);
    mvMatrixStack.push(copy);
}

function mvPopMatrix() {
    if (mvMatrixStack.length === 0) {
        throw "Invalid popMatrix!";
    }
    mvMatrix = mvMatrixStack.pop();
}

function degToRad(degrees) {
    return degrees * Math.PI / 180;
}

function initGL(canvas) {
    var gl = null;
    try {
        gl = canvas.getContext("webgl") || canvas.getContext("experimental-webgl");
        gl.viewportWidth = canvas.width;
        gl.viewportHeight = canvas.height;
    } catch (e) {
    }

    if (!gl) {
        alert("Unable to initialize WebGL. Your browser may not support it.");
    }
    return gl;
}

function getShader(gl, id) {
    var shaderScript = document.getElementById(id);

    if (!shaderScript) {
        return null;
    }

    var shaderSource = "";
    var currentChild = shaderScript.firstChild;
    while (currentChild) {
        if (currentChild.nodeType === 3) {
            shaderSource += currentChild.textContent;
        }
        currentChild = currentChild.nextSibling;
    }

    var shader;
    if (shaderScript.type === "x-shader/x-fragment") {
        shader = gl.createShader(gl.FRAGMENT_SHADER);
    } else if (shaderScript.type === "x-shader/x-vertex") {
        shader = gl.createShader(gl.VERTEX_SHADER);
    } else {
        return null;
    }

    gl.shaderSource(shader, shaderSource);

    gl.compileShader(shader);

    if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
        alert(gl.getShaderInfoLog(shader));
        return null;
    }

    return shader;
}

function initShaders() {
    var fragmentShader = getShader(gl, "shader-fs");
    var vertexShader = getShader(gl, "shader-vs");

    shaderProgram = gl.createProgram();
    gl.attachShader(shaderProgram, vertexShader);
    gl.attachShader(shaderProgram, fragmentShader);
    gl.linkProgram(shaderProgram);

    if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
        alert("Unable to initialize the shader program.");
    }

    gl.useProgram(shaderProgram);
    shaderProgram.vertexPositionAttribute = gl.getAttribLocation(shaderProgram, "aVertexPosition");
    gl.enableVertexAttribArray(shaderProgram.vertexPositionAttribute);
    shaderProgram.textureCoordAttribute = gl.getAttribLocation(shaderProgram, "aTextureCoord");
    gl.enableVertexAttribArray(shaderProgram.textureCoordAttribute);

    shaderProgram.pMatrixUniform = gl.getUniformLocation(shaderProgram, "uPMatrix");
    shaderProgram.mvMatrixUniform = gl.getUniformLocation(shaderProgram, "uMVMatrix");
    shaderProgram.samplerUniform = gl.getUniformLocation(shaderProgram, "uSampler");

    shaderProgram.ambientColorUniform = gl.getUniformLocation(shaderProgram, "uAmbientColor");
}

function setMatrixUniforms() {
    gl.uniformMatrix4fv(shaderProgram.pMatrixUniform, false, pMatrix);
    gl.uniformMatrix4fv(shaderProgram.mvMatrixUniform, false, mvMatrix);
}

function initTextures() {
    wallTexture = gl.createTexture();
    wallTexture.image = new Image();
    wallTexture.image.onload = function () {
        handleTextureLoaded(wallTexture)
    };
    wallTexture.image.src = "./assets/wall.jpg";

    doorTexture = gl.createTexture();
    doorTexture.image = new Image();
    doorTexture.image.onload = function () {
        handleTextureLoaded(doorTexture)
    };
    doorTexture.image.src = "./assets/door.jpg";

    boxesTexture = gl.createTexture();
    boxesTexture.image = new Image();
    boxesTexture.image.onload = function () {
        handleTextureLoaded(boxesTexture)
    };
    boxesTexture.image.src = "./assets/crate.jpg";

    redTexture = gl.createTexture();
    redTexture.image = new Image();
    redTexture.image.onload = function () {
        handleTextureLoaded(redTexture)
    };
    redTexture.image.src = "./assets/red.jpg";
}

function handleTextureLoaded(texture) {
    gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);

    gl.bindTexture(gl.TEXTURE_2D, texture);
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, texture.image);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
    gl.generateMipmap(gl.TEXTURE_2D);
    gl.bindTexture(gl.TEXTURE_2D, null);

    texturesLoaded += 1;
}

function handleLoadedWorld(data) {
    var lines = data.split("\n");
    var vertexCount = 0;
    var vertexPositions = [];
    var vertexTextureCoords = [];
    var numWalls = 0;
    wallPositions.push(new Wall());


    for (var i in lines) {
        //console.log(i);
        if (wallPositions[numWalls].isFull()) {
            wallPositions.push(new Wall());
            numWalls++;
        }

        var vals = lines[i].trim().replace(/^\s+/, "").split(/\s+/);
        if (vals.length === 5 && vals[0] !== "//") {
            vertexPositions.push(parseFloat(vals[0])); // X
            vertexPositions.push(parseFloat(vals[1])); // Y
            vertexPositions.push(parseFloat(vals[2])); // Z

            wallPositions[numWalls].points.push(parseFloat(vals[0]), parseFloat(vals[2]));

            vertexTextureCoords.push(parseFloat(vals[3])); // T1
            vertexTextureCoords.push(parseFloat(vals[4])); // T2

            vertexCount += 1;
        }
    }

    worldVertexPositionBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, worldVertexPositionBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertexPositions), gl.STATIC_DRAW);
    worldVertexPositionBuffer.itemSize = 3;
    worldVertexPositionBuffer.numItems = vertexCount;

    worldVertexTextureCoordBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, worldVertexTextureCoordBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertexTextureCoords), gl.STATIC_DRAW);
    worldVertexTextureCoordBuffer.itemSize = 2;
    worldVertexTextureCoordBuffer.numItems = vertexCount;

    document.getElementById("loadingtext").textContent = "";
}

function handleLoadedDoors(data) {
    var lines = data.split("\n");
    var vertexCount = 0;
    var vertexPositions = [];
    var vertexTextureCoords = [];
    var numDoors = 0;
    doorPositions.push(new Door(numDoors));


    for (var i in lines) {
        if (doorPositions[numDoors].isFull()) {
            numDoors++;
            doorPositions.push(new Door(numDoors));
        }


        var vals = lines[i].trim().replace(/^\s+/, "").split(/\s+/);
        if (vals.length === 5 && vals[0] !== "//") {
            vertexPositions.push(parseFloat(vals[0])); // X
            vertexPositions.push(parseFloat(vals[1])); // Y
            vertexPositions.push(parseFloat(vals[2])); // Z

            doorPositions[numDoors].points.push(parseFloat(vals[0]), parseFloat(vals[2]));

            vertexTextureCoords.push(parseFloat(vals[3])); // T1
            vertexTextureCoords.push(parseFloat(vals[4])); // T2

            vertexCount += 1;
        }
    }

    doorVertexPositionBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, doorVertexPositionBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertexPositions), gl.STATIC_DRAW);
    doorVertexPositionBuffer.itemSize = 3;
    doorVertexPositionBuffer.numItems = vertexCount;

    doorVertexTextureCoordBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, doorVertexTextureCoordBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertexTextureCoords), gl.STATIC_DRAW);
    doorVertexTextureCoordBuffer.itemSize = 2;
    doorVertexTextureCoordBuffer.numItems = vertexCount;
}

function initLaserPositions() {
    //laserArray.push(new Laser(0, 0.3, 1, 0)); // test
// X in Z koordinati morata biti negirani
    laserArray.push(new Laser(2, 0.3, 2, 0));
    laserArray.push(new Laser(8, 0.6, 2, 0));
    laserArray.push(new Laser(1, 0.3, 8, 1));
    laserArray.push(new Laser(9, 0.3, 4, 1));
    laserArray.push(new Laser(7, 0.6, 8, 1));
    laserArray.push(new Laser(9, 0.3, 10, 1));
}

function loadWorld() {
    var request = new XMLHttpRequest();
    request.open("GET", "./assets/world.txt");
    request.onreadystatechange = function () {
        if (request.readyState === 4) {
            handleLoadedWorld(request.responseText);
        }
    };
    request.send();
}

function loadDoors() {
    var request = new XMLHttpRequest();
    request.open("GET", "./assets/doors.txt");
    request.onreadystatechange = function () {
        if (request.readyState === 4) {
            handleLoadedDoors(request.responseText);
        }
    };
    request.send();
}

function readyLaser() {
    /*
    * Use a cube then scale to make it seem like a laser
    *
    * */

    laserVertexPositionBuffer = gl.createBuffer();

    gl.bindBuffer(gl.ARRAY_BUFFER, laserVertexPositionBuffer);

    var vertices = [
        // Front face
        -1.0, -1.0, 1.0,
        1.0, -1.0, 1.0,
        1.0, 1.0, 1.0,
        -1.0, 1.0, 1.0,

        // Back face
        -1.0, -1.0, -1.0,
        -1.0, 1.0, -1.0,
        1.0, 1.0, -1.0,
        1.0, -1.0, -1.0,

        // Top face
        -1.0, 1.0, -1.0,
        -1.0, 1.0, 1.0,
        1.0, 1.0, 1.0,
        1.0, 1.0, -1.0,

        // Bottom face
        -1.0, -1.0, -1.0,
        1.0, -1.0, -1.0,
        1.0, -1.0, 1.0,
        -1.0, -1.0, 1.0,

        // Right face
        1.0, -1.0, -1.0,
        1.0, 1.0, -1.0,
        1.0, 1.0, 1.0,
        1.0, -1.0, 1.0,

        // Left face
        -1.0, -1.0, -1.0,
        -1.0, -1.0, 1.0,
        -1.0, 1.0, 1.0,
        -1.0, 1.0, -1.0
    ];

    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
    laserVertexPositionBuffer.itemSize = 3;
    laserVertexPositionBuffer.numItems = 24;

    laserVertexTextureCoordBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, laserVertexTextureCoordBuffer);

    var textureCoordinates = [
        // Front
        0.0, 0.0,
        1.0, 0.0,
        1.0, 1.0,
        0.0, 1.0,
        // Back
        0.0, 0.0,
        1.0, 0.0,
        1.0, 1.0,
        0.0, 1.0,
        // Top
        0.0, 0.0,
        1.0, 0.0,
        1.0, 1.0,
        0.0, 1.0,
        // Bottom
        0.0, 0.0,
        1.0, 0.0,
        1.0, 1.0,
        0.0, 1.0,
        // Right
        0.0, 0.0,
        1.0, 0.0,
        1.0, 1.0,
        0.0, 1.0,
        // Left
        0.0, 0.0,
        1.0, 0.0,
        1.0, 1.0,
        0.0, 1.0
    ];

    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(textureCoordinates), gl.STATIC_DRAW);
    laserVertexTextureCoordBuffer.itemSize = 2;
    laserVertexTextureCoordBuffer.numItems = 24;

    laserVertexIndexBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, laserVertexIndexBuffer);

    var laserVertexIndices = [
        0, 1, 2, 0, 2, 3,    // Front face
        4, 5, 6, 4, 6, 7,    // Back face
        8, 9, 10, 8, 10, 11,  // Top face
        12, 13, 14, 12, 14, 15, // Bottom face
        16, 17, 18, 16, 18, 19, // Right face
        20, 21, 22, 20, 22, 23  // Left face
    ];

    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(laserVertexIndices), gl.STATIC_DRAW);
    laserVertexIndexBuffer.itemSize = 1;
    laserVertexIndexBuffer.numItems = 36;

}

/**
 * Draw the laser at x, y, z.
 *
 * @param x X pos
 * @param y Y pos
 * @param z Z pos
 * @param s How the laser is "turned"
 */
function drawLaser(x, y, z, s) {

    // Save the current matrix, then rotate before we draw.
    mvPushMatrix();

    mat4.translate(mvMatrix, [x, y, z]);

    if (s === 1) {
        mat4.scale(mvMatrix, [0.02, 0.02, 0.49]);  // Dolžina
    } else if (s === 0) {
        mat4.scale(mvMatrix, [0.499, 0.02, 0.02]);  // Širina
    }

    gl.bindBuffer(gl.ARRAY_BUFFER, laserVertexPositionBuffer);
    gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, laserVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);

    gl.bindBuffer(gl.ARRAY_BUFFER, laserVertexTextureCoordBuffer);
    gl.vertexAttribPointer(shaderProgram.textureCoordAttribute, laserVertexTextureCoordBuffer.itemSize, gl.FLOAT, false, 0, 0);

    gl.activeTexture(gl.TEXTURE0 + 4);
    gl.bindTexture(gl.TEXTURE_2D, redTexture);
    gl.uniform1i(shaderProgram.samplerUniform, 4);

    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, laserVertexIndexBuffer);

    setMatrixUniforms();
    gl.drawElements(gl.TRIANGLES, laserVertexIndexBuffer.numItems, gl.UNSIGNED_SHORT, 0);

    // Restore the original matrix
    mvPopMatrix();
}

function drawWalls() {
    gl.activeTexture(gl.TEXTURE0);
    gl.bindTexture(gl.TEXTURE_2D, wallTexture);
    gl.uniform1i(shaderProgram.samplerUniform, 0);

    gl.bindBuffer(gl.ARRAY_BUFFER, worldVertexTextureCoordBuffer);
    gl.vertexAttribPointer(shaderProgram.textureCoordAttribute, worldVertexTextureCoordBuffer.itemSize, gl.FLOAT, false, 0, 0);

    gl.bindBuffer(gl.ARRAY_BUFFER, worldVertexPositionBuffer);
    gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, worldVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);

    setMatrixUniforms();
    gl.drawArrays(gl.TRIANGLES, 0, worldVertexPositionBuffer.numItems);

}

function drawDoors() {
    gl.activeTexture(gl.TEXTURE0 + 1);
    gl.bindTexture(gl.TEXTURE_2D, doorTexture);
    gl.uniform1i(shaderProgram.samplerUniform, 1);

    gl.bindBuffer(gl.ARRAY_BUFFER, doorVertexTextureCoordBuffer);
    gl.vertexAttribPointer(shaderProgram.textureCoordAttribute, doorVertexTextureCoordBuffer.itemSize, gl.FLOAT, false, 0, 0);

    gl.bindBuffer(gl.ARRAY_BUFFER, doorVertexPositionBuffer);
    gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, doorVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);

    setMatrixUniforms();
    gl.drawArrays(gl.TRIANGLES, 0, doorVertexPositionBuffer.numItems);

}

function drawKey1() {
    mvPushMatrix();

    gl.bindBuffer(gl.ARRAY_BUFFER, keyVertexPositionBuffer);
    gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, keyVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);
    gl.bindBuffer(gl.ARRAY_BUFFER, keyVertexTextureCoordBuffer);
    gl.vertexAttribPointer(shaderProgram.textureCoordAttribute, keyVertexTextureCoordBuffer.itemSize, gl.FLOAT, false, 0, 0);

    gl.activeTexture(gl.TEXTURE0 + 3);
    gl.bindTexture(gl.TEXTURE_2D, boxesTexture);
    gl.uniform1i(shaderProgram.samplerUniform, 3);

    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, keyVertexIndexBuffer);
    setMatrixUniforms();
    gl.drawElements(gl.TRIANGLES, keyVertexIndexBuffer.numItems, gl.UNSIGNED_SHORT, 0);

    mvPopMatrix();
}

function drawKey2() {
    mvPushMatrix();

    gl.bindBuffer(gl.ARRAY_BUFFER, keyVertexPositionBuffer2);
    gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, keyVertexPositionBuffer2.itemSize, gl.FLOAT, false, 0, 0);
    gl.bindBuffer(gl.ARRAY_BUFFER, keyVertexTextureCoordBuffer2);
    gl.vertexAttribPointer(shaderProgram.textureCoordAttribute, keyVertexTextureCoordBuffer2.itemSize, gl.FLOAT, false, 0, 0);

    gl.activeTexture(gl.TEXTURE0 + 3);
    gl.bindTexture(gl.TEXTURE_2D, boxesTexture);
    gl.uniform1i(shaderProgram.samplerUniform, 3);

    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, keyVertexIndexBuffer2);
    setMatrixUniforms();
    gl.drawElements(gl.TRIANGLES, keyVertexIndexBuffer2.numItems, gl.UNSIGNED_SHORT, 0);

    mvPopMatrix();
}

function drawHP1() {
    mvPushMatrix();

    gl.bindBuffer(gl.ARRAY_BUFFER, healthVertexPositionBuffer);
    gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, healthVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);
    gl.bindBuffer(gl.ARRAY_BUFFER, healthVertexTextureCoordBuffer);
    gl.vertexAttribPointer(shaderProgram.textureCoordAttribute, healthVertexTextureCoordBuffer.itemSize, gl.FLOAT, false, 0, 0);

    gl.activeTexture(gl.TEXTURE0 + 3);
    gl.bindTexture(gl.TEXTURE_2D, boxesTexture);
    gl.uniform1i(shaderProgram.samplerUniform, 3);

    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, healthVertexIndexBuffer);
    setMatrixUniforms();
    gl.drawElements(gl.TRIANGLES, healthVertexIndexBuffer.numItems, gl.UNSIGNED_SHORT, 0);

    mvPopMatrix();
}

function drawHP2() {
    mvPushMatrix();

    gl.bindBuffer(gl.ARRAY_BUFFER, healthVertexPositionBuffer2);
    gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, healthVertexPositionBuffer2.itemSize, gl.FLOAT, false, 0, 0);
    gl.bindBuffer(gl.ARRAY_BUFFER, healthVertexTextureCoordBuffer);
    gl.vertexAttribPointer(shaderProgram.textureCoordAttribute, healthVertexTextureCoordBuffer2.itemSize, gl.FLOAT, false, 0, 0);

    gl.activeTexture(gl.TEXTURE0 + 3);
    gl.bindTexture(gl.TEXTURE_2D, boxesTexture);
    gl.uniform1i(shaderProgram.samplerUniform, 3);

    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, healthVertexIndexBuffer2);
    setMatrixUniforms();
    gl.drawElements(gl.TRIANGLES, healthVertexIndexBuffer2.numItems, gl.UNSIGNED_SHORT, 0);

    mvPopMatrix();
}

function drawScene() {
    gl.viewport(0, 0, gl.viewportWidth, gl.viewportHeight);
    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

    if (worldVertexTextureCoordBuffer === null || worldVertexPositionBuffer === null ||
        doorVertexTextureCoordBuffer === null || doorVertexPositionBuffer === null) {
        return;
    }

    mat4.perspective(45, gl.viewportWidth / gl.viewportHeight, 0.1, 100.0, pMatrix);
    mat4.identity(mvMatrix);
    mat4.rotate(mvMatrix, degToRad(-pitch), [1, 0, 0]);
    mat4.rotate(mvMatrix, degToRad(-yaw), [0, 1, 0]);
    mat4.translate(mvMatrix, [-xPosition, -yPosition, -zPosition]);

    gl.uniform3f(shaderProgram.ambientColorUniform, 0.45, 0.45, 0.45);

    drawWalls();
    drawDoors();
    for (var i = 0; i < laserArray.length; i++) {
        var l = laserArray[i];
        drawLaser(l.x, l.y, l.z, l.s);
    }


    if (!boxPositions[0].pickedUp) {
        drawKey1();
    }
    if (!boxPositions[1].pickedUp) {
        drawKey2();
    }
    if (!boxPositions[2].pickedUp) {
        drawHP1();
    }
    if (!boxPositions[3].pickedUp) {
        drawHP2();
    }

}

function checkCollision() {
    // Check collision for each wall in array
    var p = getPlayerBox();
    for (var i = 0; i < wallPositions.length; i++) {
        if (wallPositions[i].checkCollision(p)) {
            return true;
        }
    }
    for (i = 0; i < 2; i++) { // we have 2 keys
        if (doorPositions[i].checkCollision(p)) {
            return true;
        }
    }

    return false;
}

function checkBoxPickUp() {
    var p = getPlayerBox();

    for (var i = 0; i < boxPositions.length; i++) {
        if (!boxPositions[i].pickedUp) {
            boxPositions[i].checkCollision(p);
        }
    }
}

function getPlayerBox() {
    var x1 = xPosition - playerWidth / 2;
    var x2 = xPosition + playerWidth / 2;
    var z1 = zPosition - playerWidth / 2;
    var z2 = zPosition + playerWidth / 2;
    var y1 = yPosition - playerHeight * 0.75;
    var y2 = yPosition + playerHeight / 2;
    return {
        minX: Math.min(x1, x2),
        maxX: Math.max(x1, x2),
        minZ: Math.min(z1, z2),
        maxZ: Math.max(z1, z2),
        minY: Math.max(Math.min(y1, y2), 0),
        maxY: Math.min(Math.max(y1, y2), 1)
    };
}

function animate() {
    var timeNow = new Date().getTime();
    if (lastTime !== 0) {
        var elapsed = timeNow - lastTime;

        if (speed !== 0) {
            var prevX = xPosition;
            var prevY = yPosition;
            var prevZ = zPosition;

            xPosition -= Math.sin(degToRad(yaw)) * speed * elapsed;
            zPosition -= Math.cos(degToRad(yaw)) * speed * elapsed;

            joggingAngle += elapsed * 0.6;
            yPosition = Math.sin(degToRad(joggingAngle)) / 20 + 0.4;

            // If collision revert move to previous position
            if (checkCollision()) {
                xPosition = prevX;
                yPosition = prevY;
                zPosition = prevZ;

                playBump();
            }
        }

        yaw += yawRate * elapsed;
        pitch += pitchRate * elapsed;

        rotationCubeX += (90 * elapsed) / 1000.0;
        rotationCubeY += (90 * elapsed) / 1000.0;
    }
    lastTime = timeNow;
}

function restart() {
    pitch = 0;
    pitchRate = 0;
    yaw = 0;
    yawRate = 0;
    xPosition = 0;
    yPosition = 0.45;
    zPosition = 0;
    speed = 0;
    healthPoints = 100;
    for (var i = 0; i < boxPositions.length; i++) {
        boxPositions[i].pickedUp = false;
    }
}

function finish() {
    document.getElementById("gameFinishScreen").style.display = "block";
    // restart();
    gameStart = false;
}

function checkFinish() {
    if (healthPoints === 0) {
        gameOver();
    }
    if (xPosition > -10.5 && xPosition < -9.5 && zPosition > -0.5 && zPosition < 0.5) {
        finish();
    }
}

function playBump() {
    var audio = new Audio("./assets/bump-sound.mp3");
    if (!playingBump) {
        audio.play();
        playingBump = true;
        currAudio.push(audio);
    }

    if (currAudio[0].ended) {
        playingBump = false;
        currAudio.pop();
    }
}

function playPickUp() {
    var audio = new Audio("./assets/pickUp-sound.mp3");
    audio.play();
}

function handleKeyDown(event) {
    //console.log(event.keyCode);
    currentlyPressedKeys[event.keyCode] = true;
}

function handleKeyUp(event) {
    currentlyPressedKeys[event.keyCode] = false;
}

function handleKeys() {
    if (currentlyPressedKeys[38]) {
        // Up
        pitchRate = 0.1;
    } else if (currentlyPressedKeys[40]) {
        // Down
        pitchRate = -0.1;
    } else {
        pitchRate = 0;
    }

    if (currentlyPressedKeys[65] || currentlyPressedKeys[37]) {
        // A | left 
        yawRate = 0.1;
    } else if (currentlyPressedKeys[68] || currentlyPressedKeys[39]) {
        // D | right
        yawRate = -0.1;
    } else {
        yawRate = 0;
    }

    if (currentlyPressedKeys[87]) {
        // W
        speed = 0.003;
    } else if (currentlyPressedKeys[83]) {
        // S
        speed = -0.003;
    } else {
        speed = 0;
    }

    if (currentlyPressedKeys[67]) {
        // C
        yPosition = yCrouch;
    } else if (currentlyPressedKeys[32]) {
        // SPACE
        yPosition = yUP;
    } else {
        yPosition = yStanding;
    }
}

function playGame() {
    restart();
    gameStart = true;
    document.getElementById("splashScreen").style.display = "none";
    document.getElementById("gameOverScreen").style.display = "none";
    document.getElementById("gameFinishScreen").style.display = "none";
}

function gameOver() {
    gameStart = false;
    document.getElementById("gameOverScreen").style.display = "block";
}

function updateHUD() {
    document.getElementById("hud").textContent = healthPoints.toString() + " HP";
}

function checkHitLaser() {
    var p = getPlayerBox();

    for (var i = 0; i < laserArray.length; i++) {
        if (laserArray[i].checkCollision(p)) {
            healthPoints -= 2;
        }
    }
}

function start() {
    canvas = document.getElementById("glcanvas");

    gl = initGL(canvas);

    if (gl) {

        gl.clearColor(0.0, 0.0, 0.0, 1.0);
        gl.clearDepth(1.0);
        gl.enable(gl.DEPTH_TEST);
        gl.depthFunc(gl.LEQUAL);

        initShaders();
        initTextures();
        initLaserPositions();
        readyLaser();
        loadWorld();
        loadDoors();
        initKey1();
        initKey2();
        initHealth1();
        initHealth2();

        document.onkeydown = handleKeyDown;
        document.onkeyup = handleKeyUp;

        setInterval(function () {
            if (gameStart && texturesLoaded === numTextures) {
                requestAnimationFrame(animate);
                handleKeys();
                drawScene();
                checkFinish();
                checkBoxPickUp();
                updateHUD();
                checkHitLaser();
            }
        }, 15);
    }
}

function initKey1() {
    var x = -2.0;
    var y = 0.2;
    var z = -7.0;
    var o = 0.025;

    boxPositions.push(new Key(x, y, z, o));

    var vertices = [
        x - o, y - o, z + o,
        x + o, y - o, z + o,
        x + o, y + o, z + o,
        x - o, y + o, z + o,

        x - o, y - o, z - o,
        x - o, y + o, z - o,
        x + o, y + o, z - o,
        x + o, y - o, z - o,

        x - o, y + o, z - o,
        x - o, y + o, z + o,
        x + o, y + o, z + o,
        x + o, y + o, z - o,

        x - o, y - o, z - o,
        x + o, y - o, z - o,
        x + o, y - o, z + o,
        x - o, y - o, z + o,

        x + o, y - o, z - o,
        x + o, y + o, z - o,
        x + o, y + o, z + o,
        x + o, y - o, z + o,

        x - o, y - o, z - o,
        x - o, y - o, z + o,
        x - o, y + o, z + o,
        x - o, y + o, z - o
    ];

    keyVertexPositionBuffer = gl.createBuffer();

    gl.bindBuffer(gl.ARRAY_BUFFER, keyVertexPositionBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
    keyVertexPositionBuffer.itemSize = 3;
    keyVertexPositionBuffer.numItems = 24;

    var textureCoordinates = [
        0.0, 0.0,
        0.5, 0.0,
        0.5, 0.5,
        0.0, 0.5,

        0.0, 0.0,
        0.5, 0.0,
        0.5, 0.5,
        0.0, 0.5,

        0.0, 0.0,
        0.5, 0.0,
        0.5, 0.5,
        0.0, 0.5,

        0.0, 0.0,
        0.5, 0.0,
        0.5, 0.5,
        0.0, 0.5,

        0.0, 0.0,
        0.5, 0.0,
        0.5, 0.5,
        0.0, 0.5,

        0.0, 0.0,
        0.5, 0.0,
        0.5, 0.5,
        0.0, 0.5
    ];

    keyVertexTextureCoordBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, keyVertexTextureCoordBuffer);

    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(textureCoordinates), gl.STATIC_DRAW);
    keyVertexTextureCoordBuffer.itemSize = 2;
    keyVertexTextureCoordBuffer.numItems = 24;
    keyVertexIndexBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, keyVertexIndexBuffer);

    var cubeVertexIndices = [
        0, 1, 2, 0, 2, 3,    // front
        4, 5, 6, 4, 6, 7,    // back
        8, 9, 10, 8, 10, 11,   // top
        12, 13, 14, 12, 14, 15,   // bottom
        16, 17, 18, 16, 18, 19,   // right
        20, 21, 22, 20, 22, 23    // left
    ];

    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(cubeVertexIndices), gl.STATIC_DRAW);
    keyVertexIndexBuffer.itemSize = 1;
    keyVertexIndexBuffer.numItems = 36;
}

function initKey2() {
    var x = -5.0;
    var y = 0.2;
    var z = -3.0;
    var o = 0.025;
    boxPositions.push(new Key(x, y, z, o));


    var vertices = [
        x - o, y - o, z + o,
        x + o, y - o, z + o,
        x + o, y + o, z + o,
        x - o, y + o, z + o,

        x - o, y - o, z - o,
        x - o, y + o, z - o,
        x + o, y + o, z - o,
        x + o, y - o, z - o,

        x - o, y + o, z - o,
        x - o, y + o, z + o,
        x + o, y + o, z + o,
        x + o, y + o, z - o,

        x - o, y - o, z - o,
        x + o, y - o, z - o,
        x + o, y - o, z + o,
        x - o, y - o, z + o,

        x + o, y - o, z - o,
        x + o, y + o, z - o,
        x + o, y + o, z + o,
        x + o, y - o, z + o,

        x - o, y - o, z - o,
        x - o, y - o, z + o,
        x - o, y + o, z + o,
        x - o, y + o, z - o
    ];

    keyVertexPositionBuffer2 = gl.createBuffer();

    gl.bindBuffer(gl.ARRAY_BUFFER, keyVertexPositionBuffer2);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
    keyVertexPositionBuffer2.itemSize = 3;
    keyVertexPositionBuffer2.numItems = 24;

    var textureCoordinates = [
        0.5, 0.0,
        1.0, 0.0,
        1.0, 0.5,
        0.5, 0.5,

        0.5, 0.0,
        1.0, 0.0,
        1.0, 0.5,
        0.5, 0.5,

        0.5, 0.0,
        1.0, 0.0,
        1.0, 0.5,
        0.5, 0.5,

        0.5, 0.0,
        1.0, 0.0,
        1.0, 0.5,
        0.5, 0.5,

        0.5, 0.0,
        1.0, 0.0,
        1.0, 0.5,
        0.5, 0.5,

        0.5, 0.0,
        1.0, 0.0,
        1.0, 0.5,
        0.5, 0.5
    ];

    keyVertexTextureCoordBuffer2 = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, keyVertexTextureCoordBuffer2);

    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(textureCoordinates), gl.STATIC_DRAW);
    keyVertexTextureCoordBuffer2.itemSize = 2;
    keyVertexTextureCoordBuffer2.numItems = 24;
    keyVertexIndexBuffer2 = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, keyVertexIndexBuffer2);

    var cubeVertexIndices = [
        0, 1, 2, 0, 2, 3,    // front
        4, 5, 6, 4, 6, 7,    // back
        8, 9, 10, 8, 10, 11,   // top
        12, 13, 14, 12, 14, 15,   // bottom
        16, 17, 18, 16, 18, 19,   // right
        20, 21, 22, 20, 22, 23    // left
    ];

    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(cubeVertexIndices), gl.STATIC_DRAW);
    keyVertexIndexBuffer2.itemSize = 1;
    keyVertexIndexBuffer2.numItems = 36;
}

function initHealth1() {
    var x = -7.0;
    var y = 0.2;
    var z = -1.0;
    var o = 0.025;

    boxPositions.push(new HealthBox(x, y, z, o));

    var vertices = [
        x - o, y - o, z + o,
        x + o, y - o, z + o,
        x + o, y + o, z + o,
        x - o, y + o, z + o,

        x - o, y - o, z - o,
        x - o, y + o, z - o,
        x + o, y + o, z - o,
        x + o, y - o, z - o,

        x - o, y + o, z - o,
        x - o, y + o, z + o,
        x + o, y + o, z + o,
        x + o, y + o, z - o,

        x - o, y - o, z - o,
        x + o, y - o, z - o,
        x + o, y - o, z + o,
        x - o, y - o, z + o,

        x + o, y - o, z - o,
        x + o, y + o, z - o,
        x + o, y + o, z + o,
        x + o, y - o, z + o,

        x - o, y - o, z - o,
        x - o, y - o, z + o,
        x - o, y + o, z + o,
        x - o, y + o, z - o
    ];

    healthVertexPositionBuffer = gl.createBuffer();

    gl.bindBuffer(gl.ARRAY_BUFFER, healthVertexPositionBuffer);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
    healthVertexPositionBuffer.itemSize = 3;
    healthVertexPositionBuffer.numItems = 24;

    var textureCoordinates = [
        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0,

        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0,

        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0,

        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0,

        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0,

        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0
    ];

    healthVertexTextureCoordBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, healthVertexTextureCoordBuffer);

    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(textureCoordinates), gl.STATIC_DRAW);
    healthVertexTextureCoordBuffer.itemSize = 2;
    healthVertexTextureCoordBuffer.numItems = 24;
    healthVertexIndexBuffer = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, healthVertexIndexBuffer);

    var cubeVertexIndices = [
        0, 1, 2, 0, 2, 3,    // front
        4, 5, 6, 4, 6, 7,    // back
        8, 9, 10, 8, 10, 11,   // top
        12, 13, 14, 12, 14, 15,   // bottom
        16, 17, 18, 16, 18, 19,   // right
        20, 21, 22, 20, 22, 23    // left
    ];

    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(cubeVertexIndices), gl.STATIC_DRAW);
    healthVertexIndexBuffer.itemSize = 1;
    healthVertexIndexBuffer.numItems = 36;
}

function initHealth2() {
    var x = -9.0;
    var y = 0.2;
    var z = -8.0;
    var o = 0.025;
    boxPositions.push(new HealthBox(x, y, z, o));


    var vertices = [
        x - o, y - o, z + o,
        x + o, y - o, z + o,
        x + o, y + o, z + o,
        x - o, y + o, z + o,

        x - o, y - o, z - o,
        x - o, y + o, z - o,
        x + o, y + o, z - o,
        x + o, y - o, z - o,

        x - o, y + o, z - o,
        x - o, y + o, z + o,
        x + o, y + o, z + o,
        x + o, y + o, z - o,

        x - o, y - o, z - o,
        x + o, y - o, z - o,
        x + o, y - o, z + o,
        x - o, y - o, z + o,

        x + o, y - o, z - o,
        x + o, y + o, z - o,
        x + o, y + o, z + o,
        x + o, y - o, z + o,

        x - o, y - o, z - o,
        x - o, y - o, z + o,
        x - o, y + o, z + o,
        x - o, y + o, z - o
    ];

    healthVertexPositionBuffer2 = gl.createBuffer();

    gl.bindBuffer(gl.ARRAY_BUFFER, healthVertexPositionBuffer2);
    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
    healthVertexPositionBuffer2.itemSize = 3;
    healthVertexPositionBuffer2.numItems = 24;

    var textureCoordinates = [
        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0,

        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0,

        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0,

        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0,

        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0,

        0.0, 0.5,
        0.5, 0.5,
        0.5, 1.0,
        0.0, 1.0
    ];

    healthVertexTextureCoordBuffer2 = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, healthVertexTextureCoordBuffer2);

    gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(textureCoordinates), gl.STATIC_DRAW);
    healthVertexTextureCoordBuffer2.itemSize = 2;
    healthVertexTextureCoordBuffer2.numItems = 24;
    healthVertexIndexBuffer2 = gl.createBuffer();
    gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, healthVertexIndexBuffer2);

    var cubeVertexIndices = [
        0, 1, 2, 0, 2, 3,    // front
        4, 5, 6, 4, 6, 7,    // back
        8, 9, 10, 8, 10, 11,   // top
        12, 13, 14, 12, 14, 15,   // bottom
        16, 17, 18, 16, 18, 19,   // right
        20, 21, 22, 20, 22, 23    // left
    ];

    gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(cubeVertexIndices), gl.STATIC_DRAW);
    healthVertexIndexBuffer2.itemSize = 1;
    healthVertexIndexBuffer2.numItems = 36;
}
