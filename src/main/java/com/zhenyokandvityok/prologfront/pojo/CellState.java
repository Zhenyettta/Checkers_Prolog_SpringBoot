package com.zhenyokandvityok.prologfront.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CellState {
    private int row;
    private int col;
    private String state; // "black", "white", "empty"


}

