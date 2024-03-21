:- dynamic(cell/4).

% Initial board setup
cell(0, 1, black, normal).
cell(0, 3, black, normal).
cell(0, 5, black, normal).
cell(0, 7, black, normal).
cell(1, 0, black, normal).
cell(1, 2, black, normal).
cell(1, 4, black, normal).
cell(1, 6, black, normal).
cell(2, 1, black, normal).
cell(2, 3, black, normal).
cell(2, 5, black, normal).
cell(2, 7, black, normal).

cell(5, 0, white, normal).
cell(5, 2, white, normal).
cell(5, 4, white, normal).
cell(5, 6, white, normal).
cell(6, 1, white, normal).
cell(6, 3, white, normal).
cell(6, 5, white, normal).
cell(6, 7, white, normal).
cell(7, 0, white, normal).
cell(7, 2, white, normal).
cell(7, 4, white, normal).
cell(7, 6, white, normal).

% Validating coordinates
valid_coordinate(X) :- between(0, 7, X).

% Checking if a cell is free
is_free(X, Y) :-
    valid_coordinate(X),
    valid_coordinate(Y),
    \+ cell(X, Y, _, _).

% Possible moves for normal checkers
possible_move(X, Y, NewX, NewY, Player, normal) :-
    move_direction(Player, Dir),
    NewX is X + Dir,
    (NewY is Y + 1; NewY is Y - 1),
    is_free(NewX, NewY).

% Possible captures for normal checkers
possible_capture(X, Y, NewX, NewY, CaptureX, CaptureY, Player, normal) :-
    move_direction(Player, Dir),
    NewX is X + (2 * Dir),
    (NewY is Y + 2; NewY is Y - 2),
    CaptureX is X + Dir,
    (CaptureY is Y + 1; CaptureY is Y - 1),
    is_free(NewX, NewY),
    opponent(Player, Opponent),
    cell(CaptureX, CaptureY, Opponent, _),
    % Verify that the capturing move is indeed possible
    cell(X, Y, Player, _).

% Direction of movement based on checker color
move_direction(white, -1).
move_direction(black, 1).

% Determining the opponent
opponent(white, black).
opponent(black, white).

% Gathering possible moves
get_possible_moves_for_checker(X, Y, Player, Type, Moves) :-
    findall((NewX-NewY), possible_move(X, Y, NewX, NewY, Player, Type), NormalMoves),
    findall((NewX-NewY-CaptureX-CaptureY), possible_capture(X, Y, NewX, NewY, CaptureX, CaptureY, Player, Type), CaptureMoves),
    (   CaptureMoves \= [] ->
        Moves = CaptureMoves ;
        Moves = NormalMoves
    ).

% Updating the board with new facts
update_board_state(NewFacts) :-
    retractall(cell(_, _, _, _)),
    assert_facts(NewFacts).

assert_facts([]).
assert_facts([cell(X, Y, State, Type) | Rest]) :-
    assertz(cell(X, Y, State, Type)),
    assert_facts(Rest).
