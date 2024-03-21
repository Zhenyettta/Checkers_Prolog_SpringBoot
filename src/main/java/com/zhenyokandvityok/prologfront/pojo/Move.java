package com.zhenyokandvityok.prologfront.pojo;

import lombok.Data;

@Data
public class Move {
    private int endRow;
    private int endCol;
    // Optional fields for the piece being captured
    private Integer captureRow;
    private Integer captureCol;

    // Constructor for a move without capture
    public Move(int endRow, int endCol) {
        this.endRow = endRow;
        this.endCol = endCol;
        this.captureRow = null;
        this.captureCol = null;
    }

}