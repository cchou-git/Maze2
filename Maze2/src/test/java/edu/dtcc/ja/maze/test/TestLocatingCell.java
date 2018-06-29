package edu.dtcc.ja.maze.test;

import org.junit.Test;

import edu.dtcc.ja.maze.model.CellContent;
import edu.dtcc.ja.maze.model.Maze;
import edu.dtcc.ja.maze.model.MazeManager;

public class TestLocatingCell {

	@Test
	public void test() {
		
		MazeManager aManager = new MazeManager();
		aManager.populate("data.txt");
		Maze theMaze = aManager.getMaze();
		int row = 1;
		int col = 6;
		CellContent content  = theMaze.getCellContent(row, col);
		if (content == CellContent.empty) {
			System.out.println("Cell content at (" + row + "," + col + ") = empty");
		} else {
			System.out.println("Cell content at (" + row + "," + col + ") = wall");
		}
		 
	}

}
