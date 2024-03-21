package com.zhenyokandvityok.prologfront.controller;

import com.zhenyokandvityok.prologfront.pojo.BoardState;
import com.zhenyokandvityok.prologfront.pojo.Move;
import com.zhenyokandvityok.prologfront.service.PrologService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MoveOptionsController {

    private final PrologService prologService;

    public MoveOptionsController(PrologService prologService) {
        this.prologService = prologService;
    }

    @PostMapping("/move-options")
    public List<Move> getMoveOptions(@RequestBody String checkerId, @RequestBody BoardState boardState) {
        return prologService.getMoveOptions(checkerId, boardState);
    }
}
