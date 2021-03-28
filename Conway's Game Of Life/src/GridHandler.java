import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.Random;

@SuppressWarnings("serial")
public class GridHandler extends JPanel
{
	GridInfo [][] Grid;
	int generationCounter;
	
	GridHandler(int gridSize)
	{
		//instantiate the Grid matrix
		this.Grid = new GridInfo[gridSize][gridSize];
		this.generationCounter = 0;
	}
	
	void Instantiate()
	{
		Random random = new Random();
		int i = 0;
		int j = 0;
		boolean booleanVal;
		for(i = 0; i < this.Grid.length; i++)
			for(j = 0; j < this.Grid[i].length; j++)
			{
				booleanVal = false;
				double k = random.nextDouble();
				if(k < 0.05)
				{
					booleanVal = true;
					System.out.println(k);
					//System.out.println(booleanVal);
				}
				GridInfo info = new GridInfo(booleanVal, 0);
				this.Grid[i][j] = info;
				System.out.println(this.Grid[i][j].bool);
			}
		repaint();
	}
	
	void updateGrid()
	{
		//run sum stuffs
		int i = 0;
		int j = 0;
		int s = this.Grid.length;
		boolean[][] living = new boolean[s][s];
		
		for(i = 0; i < Grid.length; i++)
			for(j = 0; j < Grid.length; j++)
			{
				cellCheck(i, j, living);
			}
		
		for (int ii = 0; ii < s; ii++) {
            for (int jj = 0; jj < s; jj++) {
                this.Grid[ii][jj].bool = living[ii][jj];
            }
        }
	}
	
	void cellCheck(int i, int j, boolean [][] living)
	{
		//Create location checks
		int s = this.Grid.length;
		int neighbors = 0;
		
		int top = j > 0 ? j - 1 : s - 1;
		int bottom = j < s - 1 ? j + 1 : 0;
		int right = i > 0 ? i - 1 : s - 1;
		int left = i < s - 1 ? i + 1 : 0;
		
		// If chain to add up adjacent cells
		if(this.Grid[i][top].bool)
			neighbors++;
		
		if(this.Grid[i][bottom].bool)
			neighbors++;
		
		if(this.Grid[left][top].bool)
			neighbors++;
		
		if(this.Grid[left][bottom].bool)
			neighbors++;
		
		if(this.Grid[right][j].bool)
			neighbors++;
		
		if(this.Grid[left][j].bool)
			neighbors++;
		
		if(this.Grid[right][top].bool)
			neighbors++;
		
		if(this.Grid[right][bottom].bool)
			neighbors++;
		
		living[i][j] = lifeCheck(neighbors, i, j);
		
	}
	
	boolean lifeCheck(int neighbors, int i, int j)
	{
		// If chain to determine what rules apply to cell
		boolean alive = false;
		
		if(this.Grid[i][j].bool)
		{
			 if (neighbors < 2) {
	                alive = false;
	            } else if (neighbors == 2 || neighbors == 3) {
	                alive = true;
	            } else if (neighbors > 3) {
	                alive = false;
	            }
		}
		else
		{
			if(neighbors == 3)
			{
				alive = true;
			}
		}
		
		//System.out.println(this.Grid[i][j].bool);
		return alive;
	}
		
	void gridResize(int newSize)
	{
		GridInfo[][] gridTemp = Arrays.copyOf(this.Grid, newSize);
		this.Grid = Arrays.copyOf(gridTemp, gridTemp.length);
	}
	
	protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Color gColor = g.getColor();

        g.drawString("Generation: " + this.generationCounter++, 0, 10);
        for (int i = 0; i < this.Grid.length; i++) {
            for (int j = 0; j < this.Grid[i].length; j++) {
                if (this.Grid[i][j].bool == true) {
                    g.setColor(Color.red);
                    g.fillRect(i*4, j*4, 4, 4);
                }
            }
        }

        g.setColor(gColor);
    }

}
