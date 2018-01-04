class QuadTree {

    constructor(level, square) {
        this.level = level;
        this.square = square;

        this.balls = [];
        this.children = [];
        this.max_balls = 5;
        this.max_levels = 5;
    }

    split() {
        let newLevel = this.level + 1;
        let x = Math.round(this.square.x);
        let y = Math.round(this.square.y);
        let width = Math.round(this.square.width / 2);
        let height = Math.round(this.square.height / 2);

        // Leva zgori
        this.children[0] = new QuadTree(newLevel, new Square(x, y, width, height));
        // Desna zgori
        this.children[1] = new QuadTree(newLevel, new Square(x + width, y, width, height));
        // Desna spodi
        this.children[2] = new QuadTree(newLevel, new Square(x + width, y + height, width, height));
        // Leva spodi
        this.children[3] = new QuadTree(newLevel, new Square(x, y + height, width, height));
    }

    getQuadrant(ball) {
        let quadrant = -1;

        let middleX = this.square.x + this.square.width / 2;
        let middleY = this.square.y + this.square.height / 2;

        // V keri kvadrat spada krog
        /*if (ball.x < middleX && ball.x + ball.r < middleX && ball.y < middleY && ball.y + ball.r < middleY) {
            quadrant = 0;
        } else if (ball.x > middleX && ball.x + ball.r > middleX && ball.y < middleY && ball.y + ball.r < middleY) {
            quadrant = 3;
        } else if (ball.x < middleX && ball.x + ball.r < middleX && ball.y > middleY && ball.y + ball.r > middleY) {
            quadrant = 1;
        } else if (ball.x > middleX && ball.x + ball.r > middleX && ball.y > middleY && ball.y + ball.r > middleY) {
            quadrant = 2;
        }*/

        let top = (ball.y < middleY + this.square.diff && ball.y + ball.r < middleY + this.square.diff);
        let bottom = (ball.y > middleY - this.square.diff);

        if (ball.x < middleX && ball.x + ball.r < middleX) {
            if (top) {
                quadrant = 0;
            } else if (bottom) {
                quadrant = 3;
            }
        } else if (ball.x > middleX) {
            if (top) {
                quadrant = 1;
            } else if (bottom) {
                quadrant = 2;
            }
        }

        return quadrant;
    }

    insertBall(ball) {
        let quadrant;

        if (typeof this.children[0] !== 'undefined') {
            quadrant = this.getQuadrant(ball);
            if (quadrant !== -1) {
                this.children[quadrant].insertBall(ball);
                return;
            }
        }

        this.balls.push(ball);

        if (this.balls.length > this.max_balls && this.level < this.max_levels) {
            if (typeof this.children[0] === 'undefined') {
                this.split();
            }

            for (let i = 0; i < this.balls.length;) {
                quadrant = this.getQuadrant(this.balls[i]);
                if (quadrant !== -1) {
                    this.children[quadrant].insertBall(this.balls.splice(i, 1)[0]);
                } else {
                    i = i + 1;
                }
            }
        }
    }

    possibleCollisions(ball) {
        let index = this.getQuadrant(ball);
        let collisions = this.balls;

        if (typeof this.children[0] !== 'undefined') {
            if (index !== -1) {
                collisions = collisions.concat(this.children[index].possibleCollisions(ball));
            } else {
                for (let i = 0; i < this.children.length; i++) {
                    collisions = collisions.concat(this.children[i].possibleCollisions(ball));
                }
            }
        }

        return collisions;
    }

    clear() {
        this.balls = [];

        for (let i = 0; i < this.children.length; i = i + 1) {
            this.children[i].clear();
        }

        this.children = [];
    }

    draw(ctx) {
        if (typeof this.children[0] !== 'undefined') {
            for (let i = 0; i < this.children.length; i++) {
                this.children[i].draw(ctx);
            }
        } else {
            ctx.strokeStyle = "#000000";
            ctx.strokeRect(this.square.x, this.square.y, this.square.width, this.square.height);
        }

    }
}

class Square {
    constructor(x, y, width, height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.diff = 5;
    }
}