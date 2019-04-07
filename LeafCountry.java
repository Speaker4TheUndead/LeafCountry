package Players.LeafCountry;

import Interface.Coordinate;
import Interface.PlayerModule;
import Interface.PlayerMove;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author: Johnse Chance & Darren Gardner
 * @date: 03/19/2017
 */

public class LeafCountry implements PlayerModule {
    private Board board;
    private int id;

    public boolean hasWonGame(int i) {
        return board.hasWon(i);
    }

    //Return a boolean as to whether or not the given board can result in a win for a specified player in a certain number of moves.
    //@param: playerId, player whose turn it starts on, and the number of turns to check for.
    //@return: the boolean described above.
    public boolean isWinnable(int playerId, int whoseTurn, int numMoves) {
        Board original = new Board(this.board);
        if (numMoves == 0){
            return this.hasWonGame(playerId);
        }
        else{
            ArrayList<Board> successors = this.getSuccessors(whoseTurn);
            numMoves -= 1;
            if (whoseTurn == 1){whoseTurn = 2;}
            else if (whoseTurn == 2){whoseTurn = 1;}
            for(Board e : successors){
                this.board = e;
                if (this.board.hasWon(playerId)){
                    this.board = original;
                    return true;
                }
                else if (isWinnable(playerId, whoseTurn, numMoves)){
                    this.board = original;
                    return true;
                }
            }
        }
        this.board = original;
        return false;
    }


    //Return an ArrayList of boards that are copies of the current board with one new move added in.
    public ArrayList<Board> getSuccessors(int playerId){
        ArrayList<Board> successors = new ArrayList<>();
        for (PlayerMove move : this.allLegalMoves(1)){
            if (playerId == 2){
                move = new PlayerMove(move.getCoordinate(), 2);
            }
            Board e = new Board(this.board);
            e.processMove(move);
            successors.add(e);
        }
        return successors;
    }

    public List<PlayerMove> allLegalMoves(int id) {
        List<PlayerMove> moves = new ArrayList<PlayerMove>();
        for (int y=1; y < board.getActualSize()-1; y++){
            for (int x=1; x < board.getActualSize()-1; x++){
                if (board.getNode(y, x).getId() == 0){
                    moves.add(new PlayerMove(new Coordinate(y, x), id));
                }
            }
        }
        return moves;
    }

    public int fewestSegmentsToVictory(int i) {
        List<PlayerMove> moves = allLegalMoves(1);
        DjiktrasNonsense nonsense = new DjiktrasNonsense(board, moves, i);
        return nonsense.calculateMoves();
    }

    public void initPlayer(int i, int i1) {
        board = new Board(i);
        id = i1;
    }

    public void lastMove(PlayerMove playerMove) {
        board.processMove(playerMove);
    }

    public void otherPlayerInvalidated() {
    //Nothing to Go Here
    }

    public PlayerMove move() {
        int opp;
        if (id == 1) {
            opp = 2;
        }else {
            opp = 1;
        }
        DjiktrasNonsense ourBoard = new DjiktrasNonsense(board, allLegalMoves(id), id);
        DjiktrasNonsense oppsBoard = new DjiktrasNonsense(board, allLegalMoves(opp), opp);
        ArrayList<PlayerMove> ourBestMoves = ourBoard.getMoves();
        ArrayList<Coordinate> oppsBestMoves = new ArrayList<>();
        oppsBoard.getMoves().forEach(m -> oppsBestMoves.add(((PlayerMove)m).getCoordinate()));
        ArrayList<PlayerMove> coincidingMoves = new ArrayList<>();
        for (PlayerMove m:
             ourBestMoves) {
            if(oppsBestMoves.contains(m.getCoordinate())){
                coincidingMoves.add(m);
            }
        }
        Random r = new Random();
        if(coincidingMoves.isEmpty()){
            return ourBestMoves.remove(r.nextInt(ourBestMoves.size()));
        }else {
            return coincidingMoves.remove(r.nextInt(coincidingMoves.size()));
        }
    }
}

