package com.zhenyokandvityok.prologfront.service;

import com.zhenyokandvityok.prologfront.pojo.BoardState;
import com.zhenyokandvityok.prologfront.pojo.CellState;
import com.zhenyokandvityok.prologfront.pojo.Move;
import org.jpl7.*;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PrologService {

    public PrologService() {
        Query consultQuery = new Query("consult", new Term[]{new Atom("src/main/resources/prolog/checkers.pl")});
        if (!consultQuery.hasSolution()) {
            throw new RuntimeException("Failed to consult Prolog file.");
        }
    }

    public List<Move> getMoveOptions(CellState checkerPosition) {
        String queryStr = String.format(
                "get_possible_moves_for_checker(%d, %d, '%s', '%s', Moves)",
                checkerPosition.getRow(),
                checkerPosition.getCol(),
                checkerPosition.getState(),
                checkerPosition.getType()
        );

        Query query = new Query(queryStr);
        List<Move> moveOptions = new ArrayList<>();

        if (query.hasSolution()) {
            Map<String, Term>[] solutions = query.allSolutions();
            System.out.println(Arrays.toString(solutions));
            for (Map<String, Term> solution : solutions) {
                Term movesListTerm = solution.get("Moves");

                if (movesListTerm.isList()) {
                    Term[] movesList = movesListTerm.toTermArray();

                    for (Term moveTerm : movesList) {
                        parseMoveTerm(moveTerm, moveOptions);
                    }
                }
            }
        }
        System.out.println(moveOptions);
        return moveOptions;
    }

    private void parseMoveTerm(Term moveTerm, List<Move> moveOptions) {
        try {
            if (moveTerm.isCompound()) {
                Term innerTerm = moveTerm.arg(1); // Отримуємо внутрішній термін

                if (innerTerm.isAtom() || innerTerm.isInteger()) {
                    int endX = innerTerm.intValue();
                    int endY = moveTerm.arg(2).intValue();
                    moveOptions.add(new Move(endX, endY));
                } else if (innerTerm.isCompound()) {
                    parseMoveTerm(innerTerm, moveOptions);
                } else {
                    System.err.println("Error parsing move: " + moveTerm + " due to inner term not containing integer values");
                }
            }
        } catch (JPLException e) {
            System.err.println("Error parsing move: " + moveTerm + " due to " + e);
        }
    }








    public List<CellState> getInitialBoardState() {
        Query query = new Query("cell(X, Y, State, Type)");
        List<CellState> initialState = new ArrayList<>();

        while (query.hasMoreSolutions()) {
            Map<String, Term> solution = query.nextSolution();
            int x = solution.get("X").intValue();
            int y = solution.get("Y").intValue();
            String state = solution.get("State").toString();
            String type = solution.get("Type").toString();

            initialState.add(new CellState(x, y, state, type));
        }
        return initialState;
    }



    public void updateBoardState(BoardState boardState) {
        String facts = boardState.getBoardState().values().stream()
                .map(cell -> String.format("cell(%d, %d, '%s', '%s')", cell.getRow(), cell.getCol(), cell.getState(), cell.getType()))
                .collect(Collectors.joining(", "));

        String queryStr = String.format("update_board_state([%s])", facts);
        Query query = new Query(queryStr);

        if (!query.hasSolution()) {
            throw new RuntimeException("Failed to update Prolog board state.");
        }
    }
}
