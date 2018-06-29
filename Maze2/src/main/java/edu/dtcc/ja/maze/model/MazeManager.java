package edu.dtcc.ja.maze.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import javafx.application.Platform; 

public class MazeManager extends Observable {
	Maze theMaze;
	int delays;    // number of seconds between each move
	
	public int getDelays() {
		return delays;
	}

	public void setDelays(int delays) {
		this.delays = delays;
	}

	Cell lastVisitedCell = null; 
	Cell currentCell;
	List<Cell> traversedList;
	
	public MazeManager() { 
		traversedList = new ArrayList<Cell>();
	}
	
	public List<Cell> getTraversedList() {
		return traversedList;
	}

	public void populate(String filename) {
		theMaze = new Maze();
		theMaze.readData(filename);
	}
	
	public Maze getMaze() {
		return theMaze;
	} 
	
	public void go() {
		Cell startingCell = theMaze.getStartingCell();
		visitCell(startingCell, startingCell.getY(), startingCell.getX()); 
	}
	
	public void solveMaze() {
		MazeRunner aRunner = new MazeRunner(this);
		new Thread(aRunner).start();
	}
	
	private boolean canIVisitCell(Cell fromCell, int row, int column) {
		/**
		 * TO DO - must have a valid maze to work on 
		 */
		boolean itIsAValidCell = false;
		boolean itIsTheLastVisitedCell = false; 

		CellTypeEnum content = theMaze.getCellTypeAt(row, column);
		itIsAValidCell = (content == CellTypeEnum.empty);
		if (!itIsAValidCell) {
			return false;
		}
		
		if (fromCell != null) {
			itIsTheLastVisitedCell = (fromCell.getX() == column) && (fromCell.getY() == row);
		}
		if (itIsTheLastVisitedCell) {
			return false;
		}
		
		Cell candidate = theMaze.getCellAt(row, column);
		if (candidate.isVisitedFlag()) {
			return false;
		}
		
		return true;
	} 
	
	private void visitCell(Cell lastCell, int row, int column) {
		try {
			Thread.yield();
			Thread.sleep(delays * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Cell theCell = theMaze.getCellAt(row, column); 
		traversedList.add(theCell);
		currentCell = theCell;
		theCell.setVisitedFlag(true);
		System.out.println("Visiting row: " + row + " column: " + column); 
		this.setChanged();
		this.notifyObservers();
		if (theCell == theMaze.getTargetCell()) {
			System.out.println("Found a way out!"); 
		}
		else {
			// calculate potential cells to visit 
			// north (up)
			if (canIVisitCell(theCell, row - 1, column)) {
				visitCell(theCell, row - 1, column); 				
			} 
			if (canIVisitCell(theCell, row, column + 1)) { // east
				visitCell(theCell, row, column + 1);
			} 
			if (canIVisitCell(theCell, row, column - 1)) { // west
				visitCell(theCell, row, column - 1);
			} 
			if (canIVisitCell(theCell, row + 1, column)) { // south
				visitCell(theCell, row + 1, column);
			}  
		} 
	} 
	 
	public Cell getCurrentCell() {
		return currentCell;
	}

	public static void main(String args[]) {
		MazeManager aManager = new MazeManager();
		aManager.populate("data.txt");
		aManager.setDelays(1);
		aManager.solveMaze();
	}
}
