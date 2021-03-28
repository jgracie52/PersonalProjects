let hole;
let pow;
let turnFraction;
let powSet;
function setup(){
  createCanvas(1280, 640);
  hole = new Blackhole();
  hole2 = new Blackhole();
  // Add an initial set of boids into the system
  for (let i = 0; i < 800; i++) {
    let p = new Point();
    hole.addPoint(p);
    hole2.addPoint(p);
  }
  powSet = -2;
  pow = 1;
  turnFraction = 0.35;
  frameRate(30);
}

function draw() {
  background(0);
  powSet = powSet + 0.5;
  pow = pow - Math.pow(1.618, -powSet);
  turnFraction = turnFraction - 0.00001;
  hole.run(pow, turnFraction, 100);
}

function Blackhole(){
  this.points = [];
}

Blackhole.prototype.run = function(pow, turnFraction, radius)
{
  for (let i = 0; i < this.points.length; i++) {
    dst = Math.pow(i / (this.points.length - 1), pow) + radius;
    angle = 2 * Math.PI * turnFraction * i;

    x = dst * Math.cos(angle);
    y = dst * Math.sin(angle);

    this.points[i].run(x,y);
  }
}

Blackhole.prototype.addPoint = function(p) {
  this.points.push(p);
}

function Point(){

}

Point.prototype.run = function(points){
  this.render(x,y);
}

Point.prototype.render = function(x,y){
  //stroke(200);
  noStroke();
  push();
  var color = 255 - Math.abs(x/2) - Math.abs(y/2);
  if(color > 100)
    fill(color);
  else
    fill(color, 1);
  //translate(x,y);
  circle(x+640,y+320,2);
  pop();
}
