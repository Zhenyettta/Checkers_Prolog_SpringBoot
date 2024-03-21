package com.zhenyokandvityok.prologfront.service;

import com.zhenyokandvityok.prologfront.pojo.BoardState;
import com.zhenyokandvityok.prologfront.pojo.Move;
import org.jpl7.Query;
import org.jpl7.Term;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PrologService {

    public List<Move> getMoveOptions(String checkerId, BoardState boardState) {
        String query = String.format("move_options(%s, %s)", checkerId, convertBoardStateToPrologTerm(boardState));

        Query q = new Query(query);
        List<Move> moveOptions = new ArrayList<>();
        while (q.hasMoreSolutions()) {
            Map<String, Term> solution = q.nextSolution();

            int row = solution.get("Row").intValue();
            int col = solution.get("Col").intValue();
            moveOptions.add(new Move(row, col));
        }

        return moveOptions;
    }

    private String convertBoardStateToPrologTerm(BoardState boardState) {
        StringBuilder prologTerm = new StringBuilder("[");
        for (Map.Entry<String, Move> entry : boardState.getCheckers().entrySet()) {
            Move position = entry.getValue();
            prologTerm.append(String.format("(%s,%s),", position.getRow(), position.getCol()));
        }

        if (prologTerm.charAt(prologTerm.length() - 1) == ',') {
            prologTerm.deleteCharAt(prologTerm.length() - 1);
        }
        prologTerm.append("]");
        return prologTerm.toString();
    }
}
