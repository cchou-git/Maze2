package edu.dtcc.ja.maze.model;

public class Cell {
	private int x; // column location
	private int y; // row location
	private CellContent val; // can be a wall or empty 
	private int id;
	private boolean visitedFlag;
	
	public boolean isVisitedFlag() {
		return visitedFlag;
	}

	public void setVisitedFlag(boolean visitedFlag) {
		this.visitedFlag = visitedFlag;
	}

	public Cell(int y, int x, CellContent cnt, int anId) {
		this.setX(x);
		this.setY(y);
		this.setVal(cnt);
		id = anId;
		visitedFlag = false;
	}
	
	public CellContent getVal() {
		return val;
	}
	public void setVal(CellContent val) {
		this.val = val;
	}
	public int getX() {
		return x;
	}
	private void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	private void setY(int y) {
		this.y = y;
	}
	
	public int getID() {
		return id;
	}
	

}
