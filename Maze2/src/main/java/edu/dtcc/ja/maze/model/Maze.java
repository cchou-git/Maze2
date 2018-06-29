package edu.dtcc.ja.maze.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Stream;

public class Maze {
	private int numberOfRows = -1;
	private int numberOfColumns = -1;
	private ArrayList<ArrayList<Cell>> theMaze;
	
	Cell startingCell; // always the northwest cell (1,1)
	Cell targetCell;   // always the southeast cell (row - 2, col - 2)
	
	public int getNumberOfRows() {
		return numberOfRows;
	}

	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public int getNumberOfColumns() {
		return numberOfColumns;
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}
	
	public Cell getCellAt(int row, int column) {
		return theMaze.get(row).get(column);
	}
	
	public int getCellID(int row, int column) {
		return getCellAt(row, column).getID();
	}
	
	public CellContent getCellContent(int row, int column) {
		Cell theCell = getCellAt(row, column);
		return theCell.getVal();
	}
	
	public CellTypeEnum getCellTypeAt(int row, int column) {
		if (numberOfRows < 0 || numberOfColumns < 0) {
			return CellTypeEnum.uninitialized;
		}
		if ((row < 0 || row >= numberOfRows) || 
			(column < 0 || column >= numberOfColumns)) {
			return CellTypeEnum.outOfBound;
		}
		if (row == 0 && column == 0) {
			return CellTypeEnum.topLeftCorner;
		} else if (row == (numberOfRows - 1) && column == (numberOfColumns - 1)) {
			return CellTypeEnum.bottomRightCorner;
		} else if (row == 0 && column == (numberOfColumns - 1)) {
			return CellTypeEnum.topRightCorner;
		} else if (row == (numberOfRows - 1) && column == 0) {
			return CellTypeEnum.bottomLeftCorner;
		} else if (row == 0) {
			return CellTypeEnum.topWall;
		} else if (row == (numberOfRows - 1)) {
			return CellTypeEnum.bottomWall;
		} else if (column == 0) {
			return CellTypeEnum.leftWall;
		} else if (column == (numberOfColumns - 1)) {
			return CellTypeEnum.rightWall;
		} else {
			Cell requestedCell  = this.getCellAt(row, column);
			if (requestedCell.getVal() == CellContent.wall) {
				return CellTypeEnum.horizontalLine;
			} else {
				return CellTypeEnum.empty;
			} 
		} 
	}

	public Cell getStartingCell() {
		return startingCell;
	}

	private void setStartingCell(Cell startingCell) {
		this.startingCell = startingCell;
	}

	public Cell getTargetCell() {
		return targetCell;
	}

	private void setTargetCell(Cell targetCell) {
		this.targetCell = targetCell;
	}

	/**
	 * Each line contains 1 or 0 separated by one space
	 * 1 - wall
	 * 0 - empty
	 * @param fileName
	 * @return
	 */
	public Maze readData(String fileName) {
		Stream<String> ss;
		try {
	    		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
	    		  BufferedReader reader = new BufferedReader(
	    			        new InputStreamReader(is));
	    		  ss = reader.lines();
	    		  Object [] rows = ss.toArray(); 
	    		  int counter = 0;
	    		  for (int i = 0; i < rows.length; i++) {
	    			  String cellString = (String) rows[i]; 
	    			  String [] cells = cellString.split(" ");
	    			  this.setNumberOfColumns(cells.length);
	    			  for (int j = 0; j < cells.length; j++) {
	    				  Cell newCell = new Cell(i, j, cells[j].equals("1") ? CellContent.wall : CellContent.empty, counter);
	    				  counter ++;
	    				  if (theMaze == null) {
	    					  theMaze = new ArrayList<ArrayList<Cell>>();
	    				  }
	    				  ArrayList <Cell> rowList = null;
	    				  if (theMaze.size() > i) 
	    				    rowList = theMaze.get(i);
	    				  else {
	    					  rowList = new ArrayList<Cell>();
	    					  theMaze.add(rowList);
	    				  }
	    				  rowList.add(newCell);
	    			  }
	    		  }  
	    		  this.setNumberOfRows((int) rows.length);
	    		  this.setStartingCell(this.getCellAt(1, 1));
	    		  this.setTargetCell(this.getCellAt(this.numberOfRows - 2, this.numberOfColumns - 2)); 
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    
		return this;
	}
	
	public void printMaze() {
		if (theMaze == null) {
			return;
		}
		
		for (ArrayList<Cell> eachRow : theMaze) {
			for (int i = 0; i < eachRow.size(); i++) {
				Cell eachCell = eachRow.get(i);
				if (eachCell.getVal() == CellContent.empty) {
					if (i == 0) {
						System.out.print("\n 0");	
					} else {
						System.out.print(" 0");
					}
					
				} else {
					if (i == 0) {
						System.out.print("\n 1");	
					} else {
						System.out.print(" 1");
					}
					 
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		Maze aMaze = new Maze();
		aMaze.readData("data.txt");
		aMaze.printMaze();
	}
	 
}


