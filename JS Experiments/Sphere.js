let sphere1;
let turnFraction;
let fractionNeg;
let t;
let inclination;
let azimuth;
function setup(){
  createCanvas(1280, 800, WEBGL);
  sphere1 = new Sphere();
  for (let i = 0; i < 800; i++) {
    let p = new Point();
    sphere1.addPoint(p);
  }
  turnFraction = 0.2;
  fractionNeg = true;
  setAttributes('perPixelLighting', true);
}

function draw(){
  background(0);
  //rotateX(millis() / 1000)
  let dirX = (mouseX / width - 0.5) * 2;
  let dirY = (mouseY / height - 0.5) * 2;
  //ambientLight(50);
  lightFalloff(0.8, 0.01, 0);
  pointLight(250, 250, 250, -dirX, -dirY, -1);
  if(turnFraction > 0.2 && fractionNeg == true)
    turnFraction = turnFraction - 0.00001;
  else {
    if(turnFraction == 0.4)
    {
      turnFraction = turnFraction - 0.00001;
      fractionNeg = true;
    }
    else {
      turnFraction = turnFraction + 0.00001
      fractionNeg = false;
    }
  }
  sphere1.run(turnFraction);
}

function Sphere(){
  this.points = [];
}

Sphere.prototype.run = function(turnFraction)
{
  for (let i = 0; i < this.points.length; i++) {
    t = i/(this.points.length/2);
    inclination = Math.acos(1 - 2 * t);
    //inclination = 1 - 2 * t;
    azimuth = 2 * Math.PI * turnFraction * i;

    x = Math.sin(inclination) * Math.cos(azimuth) * 320;
    y = Math.sin(inclination) * Math.sin(azimuth) * 320;
    z = Math.sin(inclination);

    this.points[i].run(x,y,z);
  }
}

Sphere.prototype.addPoint = function(p) {
  this.points.push(p);
}

function Point(){

}

Point.prototype.run = function(points){
  this.render(x,y,z);
}

Point.prototype.render = function(x,y,z){
  translate(x,y,z);
  push();
  noStroke();
  specularMaterial(250);
  sphere(2);
  pop();
}
