package com.zhenyokandvityok.prologfront.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;


@Data
public class BoardState {
    private Map<String, CheckerPosition> checkers;

    // Конструктор
    public BoardState() {
        this.checkers = new HashMap<>();
    }

    // Метод для додавання позиції шашки до дошки
    public void addCheckerPosition(String checkerId, int row, int col) {
        checkers.put(checkerId, new CheckerPosition(row, col));
    }

    // Метод для отримання позиції шашки за її ідентифікатором
    public CheckerPosition getCheckerPosition(String checkerId) {
        return checkers.get(checkerId);
    }

    // Метод для видалення позиції шашки з дошки
    public void removeCheckerPosition(String checkerId) {
        checkers.remove(checkerId);
    }
}
