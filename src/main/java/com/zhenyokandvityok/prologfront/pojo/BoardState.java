package com.zhenyokandvityok.prologfront.pojo;

import lombok.*;

import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardState {
    private Map<String, CellState> boardState;
}
