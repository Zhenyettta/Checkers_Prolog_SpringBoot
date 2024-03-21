package com.zhenyokandvityok.prologfront.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.zhenyokandvityok.prologfront.pojo.BoardState;
import com.zhenyokandvityok.prologfront.pojo.CellState;
import com.zhenyokandvityok.prologfront.pojo.Move;
import com.zhenyokandvityok.prologfront.service.PrologService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MoveOptionsController {

    private final PrologService prologService;

    public MoveOptionsController(PrologService prologService) {
        this.prologService = prologService;
    }

    @PostMapping("move-options")
    public ResponseEntity<?> getMoveOptions(@RequestBody JsonNode jsonNode) {
        JsonNode checkerPositionNode = jsonNode.get("checkerPosition");
        int checkerRow = checkerPositionNode.get("row").asInt();
        int checkerCol = checkerPositionNode.get("col").asInt();
        String state = checkerPositionNode.get("state").asText();
        String type = checkerPositionNode.get("type").asText();
        CellState checkerPosition = new CellState(checkerRow, checkerCol, state, type);

        JsonNode boardStateNode = jsonNode.get("boardState");
        Map<String, CellState> boardMap = new HashMap<>();
        boardStateNode.fields().forEachRemaining(entry -> {
            String checkerId = entry.getKey();
            int row = entry.getValue().get("row").asInt();
            int col = entry.getValue().get("col").asInt();
            String state1 = entry.getValue().get("state").asText();
            String type1 = entry.getValue().get("type").asText();
            boardMap.put(checkerId, new CellState(row, col, state1, type1));
        });

        List<Move> moves = prologService.getMoveOptions(checkerPosition);
        return ResponseEntity.ok(moves);

    }

    @GetMapping("/get-initial-board-state")
    public ResponseEntity<List<CellState>> getInitialBoardState() {
        List<CellState> initialState = prologService.getInitialBoardState();
        return ResponseEntity.ok(initialState);
    }

    @PostMapping("/update-board-state")
    public ResponseEntity<?> updateBoardState(@RequestBody BoardState boardState) {
        prologService.updateBoardState(boardState);
        return ResponseEntity.ok().build();
    }

}
