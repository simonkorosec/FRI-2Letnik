// Global variable definitionvar canvas;
var canvas;
var gl;
var shaderProgram;

// Buffers
var worldVertexPositionBuffer;
var cubeVertexNormalBuffer;
var worldVertexTextureCoordBuffer;
var cubeVertexIndexBuffer;

// Model-view and projection matrix and model-view matrix stack
var mvMatrixStack = [];
var mvMatrix = mat4.create();
var pMatrix = mat4.create();

// Variables for storing textures
var cubeTexture;

// Variable that stores  loading state of textures.
var texturesLoaded = false;

// Keyboard handling helper variable for reading the status of keys
var currentlyPressedKeys = {};

// Variables for storing current position of cube
var positionCubeZ = -7.0;

// Variables for storing current rotation of cube
var rotationCubeX = 0;
var rotationCubeY = 0;
var rotationCubeZ = 0;

// Variables for rotation velocity of cube
var rotationVelocityCubeX = 0;
var rotationVelocityCubeY = 0;

// Helper variable for animation
var lastTime = 0;

//
// Matrix utility functions
//
// mvPush   ... push current matrix on matrix stack
// mvPop    ... pop top matrix from stack
// degToRad ... convert degrees to radians
//
function mvPushMatrix() {
  var copy = mat4.create();
  mat4.set(mvMatrix, copy);
  mvMatrixStack.push(copy);
}

function mvPopMatrix() {
  if (mvMatrixStack.length == 0) {
    throw "Invalid popMatrix!";
  }
  mvMatrix = mvMatrixStack.pop();
}

function degToRad(degrees) {
  return degrees * Math.PI / 180;
}

//
// initGL
//
// Initialize WebGL, returning the GL context or null if
// WebGL isn't available or could not be initialized.
//
function initGL(canvas) {
  var gl = null;
  try {
    // Try to grab the standard context. If it fails, fallback to experimental.
    gl = canvas.getContext("webgl") || canvas.getContext("experimental-webgl");
    gl.viewportWidth = canvas.width;
    gl.viewportHeight = canvas.height;
  } catch(e) {}

  // If we don't have a GL context, give up now
  if (!gl) {
    alert("Unable to initialize WebGL. Your browser may not support it.");
  }
  return gl;
}

//
// getShader
//
// Loads a shader program by scouring the current document,
// looking for a script with the specified ID.
//
function getShader(gl, id) {
  var shaderScript = document.getElementById(id);

  // Didn't find an element with the specified ID; abort.
  if (!shaderScript) {
    return null;
  }
  
  // Walk through the source element's children, building the
  // shader source string.
  var shaderSource = "";
  var currentChild = shaderScript.firstChild;
  while (currentChild) {
    if (currentChild.nodeType == 3) {
        shaderSource += currentChild.textContent;
    }
    currentChild = currentChild.nextSibling;
  }
  
  // Now figure out what type of shader script we have,
  // based on its MIME type.
  var shader;
  if (shaderScript.type == "x-shader/x-fragment") {
    shader = gl.createShader(gl.FRAGMENT_SHADER);
  } else if (shaderScript.type == "x-shader/x-vertex") {
    shader = gl.createShader(gl.VERTEX_SHADER);
  } else {
    return null;  // Unknown shader type
  }

  // Send the source to the shader object
  gl.shaderSource(shader, shaderSource);

  // Compile the shader program
  gl.compileShader(shader);

  // See if it compiled successfully
  if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
    alert(gl.getShaderInfoLog(shader));
    return null;
  }

  return shader;
}

//
// initShaders
//
// Initialize the shaders, so WebGL knows how to light our scene.
//
function initShaders() {
  var fragmentShader = getShader(gl, "shader-fs");
  var vertexShader = getShader(gl, "shader-vs");
  
  // Create the shader program
  shaderProgram = gl.createProgram();
  gl.attachShader(shaderProgram, vertexShader);
  gl.attachShader(shaderProgram, fragmentShader);
  gl.linkProgram(shaderProgram);
  
  // If creating the shader program failed, alert
  if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
    alert("Unable to initialize the shader program.");
  }
  
  // start using shading program for rendering
  gl.useProgram(shaderProgram);
  
  // store location of aVertexPosition variable defined in shader
  shaderProgram.vertexPositionAttribute = gl.getAttribLocation(shaderProgram, "aVertexPosition");
  
  // turn on vertex position attribute at specified position
  gl.enableVertexAttribArray(shaderProgram.vertexPositionAttribute);

  // store location of aVertexNormal variable defined in shader
  shaderProgram.vertexNormalAttribute = gl.getAttribLocation(shaderProgram, "aVertexNormal");

  // turn on vertex normal attribute at specified position
  gl.enableVertexAttribArray(shaderProgram.vertexNormalAttribute);

  // store location of aTextureCoord variable defined in shader
  shaderProgram.textureCoordAttribute = gl.getAttribLocation(shaderProgram, "aTextureCoord");

  // turn on vertex texture coordinates attribute at specified position
  gl.enableVertexAttribArray(shaderProgram.textureCoordAttribute);

  // store location of uPMatrix variable defined in shader - projection matrix 
  shaderProgram.pMatrixUniform = gl.getUniformLocation(shaderProgram, "uPMatrix");

  // store location of uMVMatrix variable defined in shader - model-view matrix 
  shaderProgram.mvMatrixUniform = gl.getUniformLocation(shaderProgram, "uMVMatrix");

  // store location of uNMatrix variable defined in shader - normal matrix 
  shaderProgram.nMatrixUniform = gl.getUniformLocation(shaderProgram, "uNMatrix");

  // store location of uSampler variable defined in shader
  shaderProgram.samplerUniform = gl.getUniformLocation(shaderProgram, "uSampler");

  // store location of uUseLighting variable defined in shader
  shaderProgram.useLightingUniform = gl.getUniformLocation(shaderProgram, "uUseLighting");

  // store location of uAmbientColor variable defined in shader
  shaderProgram.ambientColorUniform = gl.getUniformLocation(shaderProgram, "uAmbientColor");

  // store location of uLightingDirection variable defined in shader
  shaderProgram.lightingDirectionUniform = gl.getUniformLocation(shaderProgram, "uLightingDirection");

  // store location of uDirectionalColor variable defined in shader
  shaderProgram.directionalColorUniform = gl.getUniformLocation(shaderProgram, "uDirectionalColor");
}

//
// setMatrixUniforms
//
// Set the uniform values in shaders for model-view,  projection and normal matrix.
//
function setMatrixUniforms() {
  gl.uniformMatrix4fv(shaderProgram.pMatrixUniform, false, pMatrix);
  gl.uniformMatrix4fv(shaderProgram.mvMatrixUniform, false, mvMatrix);

  var normalMatrix = mat3.create();
  mat4.toInverseMat3(mvMatrix, normalMatrix);
  mat3.transpose(normalMatrix);
  gl.uniformMatrix3fv(shaderProgram.nMatrixUniform, false, normalMatrix);
}

//
// initTextures
//
// Initialize the textures we'll be using, then initiate a load of
// the texture images. The handleTextureLoaded() callback will finish
// the job; it gets called each time a texture finishes loading.
//
function initTextures() {
  cubeTexture = gl.createTexture();
  cubeTexture.image = new Image();
  cubeTexture.image.onload = function () {
    handleTextureLoaded(cubeTexture)
  }; // async loading
  cubeTexture.image.src = "./assets/crate.gif";
}

function handleTextureLoaded(texture) {
  gl.pixelStorei(gl.UNPACK_FLIP_Y_WEBGL, true);

  // Third texture usus Linear interpolation approximation with nearest Mipmap selection
  gl.bindTexture(gl.TEXTURE_2D, texture);
  gl.texImage2D(gl.TEXTURE_2D, 0, gl.RGBA, gl.RGBA, gl.UNSIGNED_BYTE, texture.image);
  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MAG_FILTER, gl.LINEAR);
  gl.texParameteri(gl.TEXTURE_2D, gl.TEXTURE_MIN_FILTER, gl.LINEAR_MIPMAP_NEAREST);
  gl.generateMipmap(gl.TEXTURE_2D);

  gl.bindTexture(gl.TEXTURE_2D, null);

  // when texture loading is finished we can draw scene.
  texturesLoaded = true;
}

//
// initBuffers
//
// Initialize the buffers we'll need. For this demo, we just have
// one object -- a simple two-dimensional cube.
//
function initBuffers() {
  // Create a buffer for the cube's vertices.
  worldVertexPositionBuffer = gl.createBuffer();
  
  // Select the worldVertexPositionBuffer as the one to apply vertex
  // operations to from here out.
  gl.bindBuffer(gl.ARRAY_BUFFER, worldVertexPositionBuffer);
  
  // Now create an array of vertices for the cube.
  vertices = [0.202008,2.418635,-4.07776,0.202007,1.329363,-2.124945,0.397098,2.39944,-4.078627,0.584691,2.342592,-4.081196,0.757578,2.250277,-4.085368,0.909114,2.126041,-4.090982,1.033477,1.974659,-4.097824,1.125887,1.801948,-4.105629,1.182793,1.614546,-4.114098,1.202008,1.419655,-4.122906,1.182793,1.224764,-4.131713,1.125887,1.037362,-4.140182,1.033477,0.864651,-4.147987,0.909114,0.713269,-4.154829,0.757578,0.589033,-4.160443,0.584691,0.496717,-4.164615,0.397098,0.43987,-4.167184,0.202007,0.420675,-4.168052,0.006917,0.43987,-4.167184,-0.180676,0.496718,-4.164615,-0.353563,0.589034,-4.160443,-0.5051,0.71327,-4.154829,-0.629463,0.864652,-4.147987,-0.721872,1.037363,-4.140182,-0.778778,1.224765,-4.131713,-0.797992,1.419656,-4.122906,-0.778778,1.614548,-4.114098,-0.721871,1.80195,-4.105629,-0.629461,1.97466,-4.097824,-0.505098,2.126042,-4.090982,-0.353562,2.250278,-4.085368,-0.180674,2.342593,-4.081196,0.006919,2.399441,-4.078627];
  
  // Now pass the list of vertices into WebGL to build the shape. We
  // do this by creating a Float32Array from the JavaScript array,
  // then use it to fill the current vertex buffer.
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);
  worldVertexPositionBuffer.itemSize = 3;
  worldVertexPositionBuffer.numItems = vertices.length;

  // Map the normals onto the cube's vertices.
  cubeVertexNormalBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexNormalBuffer);

  // Now create an array of vertex normals for the cube.


  // Map the texture onto the cube's faces.
  worldVertexTextureCoordBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, worldVertexTextureCoordBuffer);
  
  // Now create an array of vertex texture coordinates for the cube.
  var textureCoordinates = [
    // Front
    0.0,  0.0,
    1.0,  0.0,
    1.0,  1.0,
    0.0,  1.0,
    // Back
    0.0,  0.0,
    1.0,  0.0,
    1.0,  1.0,
    0.0,  1.0,
    // Top
    0.0,  0.0,
    1.0,  0.0,
    1.0,  1.0,
    0.0,  1.0,
    // Bottom
    0.0,  0.0,
    1.0,  0.0,
    1.0,  1.0,
    0.0,  1.0,
    // Right
    0.0,  0.0,
    1.0,  0.0,
    1.0,  1.0,
    0.0,  1.0,
    // Left
    0.0,  0.0,
    1.0,  0.0,
    1.0,  1.0,
    0.0,  1.0
  ];

  // Pass the texture coordinates into WebGL
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(textureCoordinates), gl.STATIC_DRAW);
  worldVertexTextureCoordBuffer.itemSize = 2;
  worldVertexTextureCoordBuffer.numItems = 24;

  // Build the element array buffer; this specifies the indices
  // into the vertex array for each face's vertices.
  cubeVertexIndexBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, cubeVertexIndexBuffer);
  
  // This array defines each face as two triangles, using the
  // indices into the vertex array to specify each triangle's
  // position.
  var cubeVertexIndices = [0,1,2,2,1,3,3,1,4,4,1,5,5,1,6,6,1,7,7,1,8,8,1,9,9,1,10,10,1,11,11,1,12,12,1,13,13,1,14,14,1,15,15,1,16,16,1,17,17,1,18,18,1,19,19,1,20,20,1,21,21,1,22,22,1,23,23,1,24,24,1,25,25,1,26,26,1,27,27,1,28,28,1,29,29,1,30,30,1,31,31,1,32,32,1,0,0,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32];
  
  // Now send the element array to GL
  gl.bufferData(gl.ELEMENT_ARRAY_BUFFER, new Uint16Array(cubeVertexIndices), gl.STATIC_DRAW);
  cubeVertexIndexBuffer.itemSize = 1;
  cubeVertexIndexBuffer.numItems = 33;
}

//
// drawScene
//
// Draw the scene.
//
function drawScene() {
  // set the rendering environment to full canvas size
  gl.viewport(0, 0, gl.viewportWidth, gl.viewportHeight);
  // Clear the canvas before we start drawing on it.
  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
  
  // Establish the perspective with which we want to view the
  // scene. Our field of view is 45 degrees, with a width/height
  // ratio and we only want to see objects between 0.1 units
  // and 100 units away from the camera.
  mat4.perspective(45, gl.viewportWidth / gl.viewportHeight, 0.1, 100.0, pMatrix);

  // Set the drawing position to the "identity" point, which is
  // the center of the scene.
  mat4.identity(mvMatrix);

  // Now move the drawing position a bit to where we want to start
  // drawing the cube.
  mat4.translate(mvMatrix, [0.0, 0.0, positionCubeZ]);

  // Rotate before we draw.
  mat4.rotate(mvMatrix, degToRad(rotationCubeX), [1, 0, 0]);
  mat4.rotate(mvMatrix, degToRad(rotationCubeY), [0, 1, 0]);
  mat4.rotate(mvMatrix, degToRad(rotationCubeZ), [0, 0, 1]);

  // Draw the cube by binding the array buffer to the cube's vertices
  // array, setting attributes, and pushing it to GL.
  gl.bindBuffer(gl.ARRAY_BUFFER, worldVertexPositionBuffer);
  gl.vertexAttribPointer(shaderProgram.vertexPositionAttribute, worldVertexPositionBuffer.itemSize, gl.FLOAT, false, 0, 0);
  
  // Set the normals attribute for vertices.
  gl.bindBuffer(gl.ARRAY_BUFFER, cubeVertexNormalBuffer);
  gl.vertexAttribPointer(shaderProgram.vertexNormalAttribute, cubeVertexNormalBuffer.itemSize, gl.FLOAT, false, 0, 0);

  // Set the texture coordinates attribute for the vertices.
  gl.bindBuffer(gl.ARRAY_BUFFER, worldVertexTextureCoordBuffer);
  gl.vertexAttribPointer(shaderProgram.textureCoordAttribute, worldVertexTextureCoordBuffer.itemSize, gl.FLOAT, false, 0, 0);

  // Specify the texture to map onto the faces.
  gl.activeTexture(gl.TEXTURE0);
  gl.bindTexture(gl.TEXTURE_2D, cubeTexture);
  gl.uniform1i(shaderProgram.samplerUniform, 0);

  // Ligthing
  var lighting = document.getElementById("lighting").checked;

  // set uniform to the value of the checkbox.
  gl.uniform1i(shaderProgram.useLightingUniform, lighting);

  // set uniforms for lights as defined in the document
  if (lighting) {
    gl.uniform3f(
      shaderProgram.ambientColorUniform,
      parseFloat(document.getElementById("ambientR").value),
      parseFloat(document.getElementById("ambientG").value),
      parseFloat(document.getElementById("ambientB").value)
    );

    var lightingDirection = [
      parseFloat(document.getElementById("lightDirectionX").value),
      parseFloat(document.getElementById("lightDirectionY").value),
      parseFloat(document.getElementById("lightDirectionZ").value)
    ];
    var adjustedLD = vec3.create();
    vec3.normalize(lightingDirection, adjustedLD);
    vec3.scale(adjustedLD, -1);
    gl.uniform3fv(shaderProgram.lightingDirectionUniform, adjustedLD);

    gl.uniform3f(
      shaderProgram.directionalColorUniform,
      parseFloat(document.getElementById("directionalR").value),
      parseFloat(document.getElementById("directionalG").value),
      parseFloat(document.getElementById("directionalB").value)
    );
  }

  // Draw the cube.
  gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, cubeVertexIndexBuffer);
  setMatrixUniforms();
  gl.drawElements(gl.TRIANGLES, cubeVertexIndexBuffer.numItems, gl.UNSIGNED_SHORT, 0);
}

//
// animate
//
// Called every time before redeawing the screen.
//
function animate() {
  var timeNow = new Date().getTime();
  if (lastTime != 0) {
    var elapsed = timeNow - lastTime;

    // rotate the cube for a small amount
    rotationCubeX += (rotationVelocityCubeX * elapsed) / 1000.0;
    rotationCubeY += (rotationVelocityCubeY * elapsed) / 1000.0;
  }
  lastTime = timeNow;
}

//
// Keyboard handling helper functions
//
// handleKeyDown    ... called on keyDown event
// handleKeyUp      ... called on keyUp event
//
function handleKeyDown(event) {
  // storing the pressed state for individual key
  currentlyPressedKeys[event.keyCode] = true;
}

function handleKeyUp(event) {
  // reseting the pressed state for individual key
  currentlyPressedKeys[event.keyCode] = false;
}

//
// handleKeys
//
// Called every time before redeawing the screen for keyboard
// input handling. Function continuisly updates helper variables.
//
function handleKeys() {
  if (currentlyPressedKeys[33]) {
    // Page Up
    positionCubeZ -= 0.05;
  }
  if (currentlyPressedKeys[34]) {
    // Page Down
    positionCubeZ += 0.05;
  }
  if (currentlyPressedKeys[37]) {
    // Left cursor key
    rotationVelocityCubeY -= 1;
  }
  if (currentlyPressedKeys[39]) {
    // Right cursor key
    rotationVelocityCubeY += 1;
  }
  if (currentlyPressedKeys[38]) {
    // Up cursor key
    rotationVelocityCubeX -= 1;
  }
  if (currentlyPressedKeys[40]) {
    // Down cursor key
    rotationVelocityCubeX += 1;
  }
}

//
// start
//
// Called when the canvas is created to get the ball rolling.
//
function start() {
  canvas = document.getElementById("glcanvas");

  gl = initGL(canvas);      // Initialize the GL context

  // Only continue if WebGL is available and working
  if (gl) {
    gl.clearColor(0.0, 0.0, 0.0, 1.0);                      // Set clear color to black, fully opaque
    gl.clearDepth(1.0);                                     // Clear everything
    gl.enable(gl.DEPTH_TEST);                               // Enable depth testing
    gl.depthFunc(gl.LEQUAL);                                // Near things obscure far things

    // Initialize the shaders; this is where all the lighting for the
    // vertices and so forth is established.
    initShaders();
    
    // Here's where we call the routine that builds all the objects
    // we'll be drawing.
    initBuffers();
    
    // Next, load and set up the textures we'll be using.
    initTextures();

    // Bind keyboard handling functions to document handlers
    document.onkeydown = handleKeyDown;
    document.onkeyup = handleKeyUp;
    
    // Set up to draw the scene periodically.
    setInterval(function() {
      if (texturesLoaded) { // only draw scene and animate when textures are loaded.
        requestAnimationFrame(animate);
        handleKeys();
        drawScene();
      }
    }, 15);
  }
}