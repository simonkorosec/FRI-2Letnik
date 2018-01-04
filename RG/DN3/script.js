let canvas;
let ctx;
let width;
let height;
let numberOfBalls;
let balls;
let tree;

class Ball {
    constructor() {
        this.r = 5;
        this.x = randomNumber(this.r, width - this.r);
        this.y = randomNumber(this.r, height - this.r);
        this.vX = randomNumber(0, 3) + 1;
        this.vY = randomNumber(0, 3) + 1;

        if (randomNumber(-1, 1) > 0) {
            this.vX *= -1;
        }

        if (randomNumber(-1, 1) < 0) {
            this.vY *= -1;
        }
    }

    draw() {
        ctx.fillStyle = "#9fa09e";
        ctx.beginPath();
        ctx.arc(this.x, this.y, this.r, 0, 2 * Math.PI);
        ctx.fill();

        this.move();
    }

    move() {
        this.x += this.vX;
        this.y += this.vY;

        if (this.x < this.r || this.x > width - this.r) {
            this.vX *= -1;
        }
        if (this.y < this.r || this.y > height - this.r) {
            this.vY *= -1;
        }
    }
}

function randomNumber(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
}

function drawFrame() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    tree.draw(ctx);
    tree.clear();
    //console.log(tree);

    for (let ball of balls) {
        ball.draw();
        tree.insertBall(ball);
    }
    //console.log(tree);
}

function addBalls() {
    //console.log(numberOfBalls);
    //console.log(balls.length);

    for (let i = 0; i < numberOfBalls; i++) {
        let b = new Ball();
        balls.push(b);
        //  console.log(i);
    }
    //console.log(balls);
}

function start() {
    canvas = document.getElementById("myCanvas");
    ctx = canvas.getContext("2d");
    ctx.strokeStyle = "#000000";

    width = canvas.width;
    height = canvas.height;
    tree = new QuadTree(1, new Square(0, 0, width, height));
    //tree.max_balls = 1000;

    numberOfBalls = 100;
    balls = [];

    addBalls();


    setInterval(function () {
        drawFrame();
    }, 15);

}
