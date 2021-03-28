var grid = new Array(4);
var gridCellWidth;
var gridCellHeight;

var screenWidthCenter = $(window).width() / 2;
var screenHeightCenter = $(window).height() / 2;

var startPos;

$(document).ready(function()
{
  startPos = $("#cell-start").offset();
  gridCellWidth = $("#cell-start").width();
  gridCellHeight = $("#cell-start").height();
  instantiateGrid();
});

function instantiateGrid()
{
  for(var i = 0; i < 4; i++)
  {
    grid[i] = new Array(4);
  }
  for(var x = 0; x < 2; x++)
  {
    addTileRandom();
  }
}

class GridTile
{
  constructor(cellNumber, value, color, element)
  {
    this.cellNumber = cellNumber;
    this.value = value;
    this.color = color;
    this.element = element;
  }
}

function addTileRandom()
{
  var randrow = Math.floor(Math.random() * 4);
  var randcol = Math.floor(Math.random() * 4);

  var element = document.createElement('div');
  var elementid = randrow * 4 + randcol;
  element.setAttribute("id", elementid);
  var elementID = $("#elementid");
  element.setAttribute("class", "tile-cell");
  element.setAttribute("width", gridCellWidth);
  elementID.css("width", gridCellWidth + "px");
  element.setAttribute("height", gridCellHeight);
  elementID.css("height", gridCellHeight + "px");
  element.setAttribute("top", startPos.top + "px");
  elementID.css("top", startPos.top + "px");
  element.setAttribute("left", startPos.left + "px");
  elementID.css("left", startPos.left + "px");
  element.setAttribute("position", "absolute");
  element.setAttribute("margin", "3px");

  document.getElementById("tile-container").appendChild(element);

  grid[randrow][randcol] = new GridTile((randrow*4+randcol), 2, "#ff1", element);
}

document.onkeydown = function(event) {
    var i;
    var j;
        switch (event.keyCode) {
           case 37: // Left Key Pressed
              shiftLeft();
              break;
           case 38: // Up Key Pressed
              shiftUp();
              break;
           case 39: // Right Key Pressed
              shiftRight();
              break;
           case 40: // Down Key Pressed
               shiftDown();
              break;
        }
    };

    function shiftRight()
    {
      for(i = 1; i < 4; i++) // Loop from right to left
      {
        for(j = 1; j < 5; j++) // Loop from top to bottom
        {
          // Same as left
          grid[4 * j - i - 1] = grid[4 * j - i];
        }
      }
    }

    function shiftLeft()
    {
      for(i = 2; i >= 0; i--) // Loop from left to right
      {
        for(j = 1; j < 5; j++) // Loop from top to bottom
        {
          // Current cell is [4 * row# - cellsFromRight - 1] -> next cell is [4 - row# - cellsFromRight]
          grid[4 * j - i - 1] = grid[4 * j - i - 2];
        }
      }
    }

    function shiftUp()
    {
      for(i = 0; i < 4; i++) // Loop from right to left
      {
        for(j = 2; j < 5; j++) // Loop from top to bottom (row 2 -> row 4)
        {
          // Current cell is [4 * row# - cellsFromRight - 1] -> next cell is [4 * row# - cellsFromRight  + 3]
          grid[4 * j - i - 1] = grid[4 * j - i - 5];
        }
      }
    }

    function shiftDown()
    {
      for(i = 0; i < 4; i++) // Loop from right to left
      {
        for(j = 3; j >= 0; j--) // Loop from bottom to top (row 3 -> row 1)
        {
          // Same as up
          grid[4 * j - i - 1] = grid[4 * j - i + 3];
        }
      }
    }
