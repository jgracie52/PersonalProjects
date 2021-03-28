let fractal;
let canvasWidth;
let canvasHeight;
let panX;
let panY;
let magnification;
function setup(){
  canvasWidth = 1280;
  canvasHeight = 640;
  createCanvas(canvasWidth, canvasHeight);
  fractal = new Fractal();
  fractal.addPixel();
  magnification = 200;
  panX = 3.5;
  panY = 1.5;
  //fractal.run();
}

function draw(){
  background(51);
  fractal.run();
}

function checkBelong(x,y){
  var realComponentOfResult = x;
var imaginaryComponentOfResult = y;

for(var i = 0; i < 100; i++) {
     // Calculate the real and imaginary components of the result
     // separately
     var tempRealComponent = realComponentOfResult * realComponentOfResult
                             - imaginaryComponentOfResult * imaginaryComponentOfResult
                             + x;

     var tempImaginaryComponent = 2 * realComponentOfResult * imaginaryComponentOfResult
                             + y;

     realComponentOfResult = tempRealComponent;
     imaginaryComponentOfResult = tempImaginaryComponent;
}

if (realComponentOfResult * imaginaryComponentOfResult < 5)
    return true; // In the Mandelbrot set

return false; // Not in the set
}

function Fractal(){
  this.pixels = [];
  for(var i = 0; i < canvasWidth; i++)
  {
    this.pixels[i] = [];
  }
}

Fractal.prototype.run = function(){
  for(var x = 0; x < canvasWidth; x++)
  {
    for(var y = 0; y < canvasHeight; y++)
    {
      var setCheck = checkBelong(x/magnification - panX, y/magnification - panY);
      if(setCheck)
      {
        this.pixels[x][y].run(x,y, 51);
      }
    }
  }
}

Fractal.prototype.addPixel = function() {
  for (let i = 0; i < canvasWidth; i++) {
    for(let j = 0; j < canvasHeight; j++)
    {
      let p = new Pixel();
      this.pixels[i][j] = p;
    }
  }
}

function Pixel(){

}

Pixel.prototype.run = function(x,y){
  this.render(x,y);
}

Pixel.prototype.render = function(x,y,color){
  //stroke(200);
  noStroke();
  push();
  fill('black');
  //translate(x,y);
  rect(x,y,1,1);
  pop();
}
