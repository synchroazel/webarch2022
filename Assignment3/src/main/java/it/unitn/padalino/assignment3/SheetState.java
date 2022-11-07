package it.unitn.padalino.assignment3;

import java.sql.Timestamp;
import java.util.Map;

public class SheetState {
    public String timestamp;
    public Map<String, CellState> cells;

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp.toString();
    }

    public void setCells(Map<String, CellState> cells) {
        this.cells = cells;
    }

}
