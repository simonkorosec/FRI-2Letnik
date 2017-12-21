var canvas;
var gl;
var shaderProgram;

var worldVertexPositionBuffer = null;
var worldVertexTextureCoordBuffer = null;

var mvMatrixStack = [];
var mvMatrix = mat4.create();
var pMatrix = mat4.create();

var wallTexture;
var texturesLoaded = false;

var currentlyPressedKeys = {};

var pitch = 0;
var pitchRate = 0;
var yaw = 0;
var yawRate = 0;
var xPosition = 0;
var yPosition = 0.4;
var zPosition = 0;
var speed = 0;
var joggingAngle = 0;

var lastTime = 0;

var wallPositions = [];
var gameStart = false;

function Wall() {
    this.points = [];

    this.isFull = function() {
        if (this.points.length >= 12) {
            this.points = this.points.slice(0, 4); // Only keep first 4 points rest are identical
            return true; // becuse we dont look at the Y axias with walls
        }
        return false;
    };

    this.checkCollision = function() {
        var odmik = 0.13; // So the player can't see throught the wall when he turns near a wall

        var minX = Math.min(this.points[0], this.points[2]) - odmik;
        var maxX = Math.max(this.points[0], this.points[2]) + odmik;
        var minZ = Math.min(this.points[1], this.points[3]) - odmik;
        var maxZ = Math.max(this.points[1], this.points[3]) + odmik;

        return (zPosition > minZ && zPosition < maxZ) &&
            (xPosition > minX && xPosition < maxX);

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
    } catch (e) {}

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
}

function setMatrixUniforms() {
    gl.uniformMatrix4fv(shaderProgram.pMatrixUniform, false, pMatrix);
    gl.uniformMatrix4fv(shaderProgram.mvMatrixUniform, false, mvMatrix);
}


function initTextures() {
    wallTexture = gl.createTexture();
    wallTexture.image = new Image();
    wallTexture.image.onload = function() {
        handleTextureLoaded(wallTexture)
    };
    wallTexture.image.src = "./assets/wall.jpg";
}

function handleTextureLoaded(texture) {
    gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);

    gl.bindTexture(gl.TEXTURE_2D, texture);
    gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, texture.image);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
    gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR);
    gl.generateMipmap(gl.TEXTURE_2D);
    gl.bindTexture(gl.TEXTURE_2D, null);

    texturesLoaded = true;
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


function loadWorld() {
    var request = new XMLHttpRequest();
    request.open("GET", "./assets/world.txt");
    request.onreadystatechange = function() {
        if (request.readyState === 4) {
            handleLoadedWorld(request.responseText);
        }
    };
    request.send();
}


function drawScene() {
    gl.viewport(0, 0, gl.viewportWidth, gl.viewportHeight);
    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

    if (worldVertexTextureCoordBuffer === null || worldVertexPositionBuffer === null) {
        return;
    }

    mat4.perspective(45, gl.viewportWidth / gl.viewportHeight, 0.1, 100.0, pMatrix);
    mat4.identity(mvMatrix);

    mat4.rotate(mvMatrix, degToRad(-pitch), [1, 0, 0]);
    mat4.rotate(mvMatrix, degToRad(-yaw), [0, 1, 0]);
    mat4.translate(mvMatrix, [-xPosition, -yPosition, -zPosition]);

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


function checkCollision() {

    // Check collision for each wall in array
    for (var i = 0; i < wallPositions.length; i++) {
        if (wallPositions[i].checkCollision()) {
            return true;
        }
    }
    return false;
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

            // If collision reverte move to previous position
            if (checkCollision()) {
                xPosition = prevX;
                yPosition = prevY;
                zPosition = prevZ;

                playBump();
            }
        }

        yaw += yawRate * elapsed;
        pitch += pitchRate * elapsed;

    }
    lastTime = timeNow;
}

function checkFinish() {
    if (xPosition > -10.5 && xPosition < -9.5 && zPosition > -0.5 && zPosition < 0.5) {
        pitch = 0;
        pitchRate = 0;
        yaw = 0;
        yawRate = 0;
        xPosition = 0;
        yPosition = 0.4;
        zPosition = 0;
        speed = 0;

    }
}

var playingBump = false;
var currAudio = [];

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

function handleKeyDown(event) {
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
}

function playGame() {
    gameStart = true;
    var x = document.getElementById("splashScreen");
    if (x.style.display === "none") {
        x.style.display = "block";
    } else {
        x.style.display = "none";
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
        loadWorld();

        document.onkeydown = handleKeyDown;
        document.onkeyup = handleKeyUp;


        setInterval(function() {
            if (gameStart && texturesLoaded) {
                requestAnimationFrame(animate);
                handleKeys();
                drawScene();
                checkFinish();
            }
        }, 15);


    }
}