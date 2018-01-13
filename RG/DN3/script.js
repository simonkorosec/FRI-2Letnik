let canvas;
let ctx;
let width;
let height;
let numberOfBalls;
let defaultNumBalls = 10;
let balls;
let tree;

class Ball {
    constructor() {
        this.r = 6;
        this.x = randomNumber(this.r, width - this.r);
        this.y = randomNumber(this.r, height - this.r);
        this.vX = randomNumber(1, 3);
        this.vY = randomNumber(1, 3);
        this.collision = 0;

        if (randomNumber(-1, 1) > 0) {
            this.vX *= -1;
        }

        if (randomNumber(-1, 1) < 0) {
            this.vY *= -1;
        }
    }

    draw() {
        if (this.collision === 0) {
            ctx.fillStyle = "#9fa09e";
        } else if (this.collision === 1) {
            ctx.fillStyle = "#ff0000";
        }
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

        this.collision = 0;
    }

    setX(x) {
        this.x = x;
        this.vX += randomNumber(-0.1, 0.1)
    }

    setY(y) {
        this.y = y;
        this.vY += randomNumber(-0.1, 0.1)
    }

}

function randomNumber(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
}

function drawFrame() {
    ctx.clearRect(0, 0, canvas.width, canvas.height);
    if (document.getElementById("showGrid").checked) {
        tree.draw(ctx);
    }

    for (let ball of balls) {
        let list = tree.possibleCollisions(ball);
        for (let b2 of list) {
            if (ball === b2 || ball.collision === 1 || b2.collision === 1) {
                continue;
            }

            let dx = ball.x - b2.x;
            let dy = ball.y - b2.y;
            let d = Math.sqrt(dx * dx + dy * dy);
            if (d < ball.r + b2.r) {
                ball.collision = 1;
                b2.collision = 1;
            }
        }
    }

    tree.clear();
    for (let ball of balls) {
        ball.draw();
        tree.insertBall(ball);
    }

}

function addBall(event) {
    let rect = canvas.getBoundingClientRect();
    let x = event.clientX - rect.left;
    let y = event.clientY - rect.top;
    let n = numberOfBalls.value;

    for (let i = 0; i < n; i++) {
        let b = new Ball();
        b.setX(x + randomNumber(-25, 25));
        b.setY(y + randomNumber(-25, 25));
        balls.push(b);
    }
}

function clearBalls() {
    balls = [];
}

function start() {
    canvas = document.getElementById("myCanvas");
    ctx = canvas.getContext("2d");
    ctx.strokeStyle = "#000000";
    canvas.onclick = addBall;
    numberOfBalls = document.getElementById("numOfBalls");

    width = canvas.width;
    height = canvas.height;
    tree = new QuadTree(1, new Square(0, 0, width, height));

    balls = [];

    for (let i = 0; i < defaultNumBalls; i++) {
        let b = new Ball();
        balls.push(b);
    }


    setInterval(function () {
        drawFrame();
    }, 15);

}
