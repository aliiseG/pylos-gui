package com.example.demo;



abstract class Move {
    protected int moveSpace;
    protected Player currentPlayer;


    public Move(int moveSpace, Player currentPlayer){
        this.moveSpace = moveSpace;
        this.currentPlayer = currentPlayer;
    };

    public abstract void makeMove(Board board);
    public abstract String serialize();

    public static Move deserialize(String data, Player player){
        if (data.startsWith("PlaceNew:")){
            int moveSpace = Integer.parseInt(data.substring(9));
            return new PlaceNew(moveSpace, player);
        }else if (data.startsWith("MoveFrom:")){
            String[] splitString = data.substring(9).split(",");
            int fromSpace = Integer.parseInt(splitString[0]);
            int moveSpace = Integer.parseInt(splitString[1]);
            return new MoveExisting(fromSpace, moveSpace, player);
        }else if (data.startsWith("RemoveFrom:")){
            String[] splitString = data.substring(11).split(",");
            int moveSpace = Integer.parseInt(splitString[0]);
            int moveSpace2 = Integer.parseInt(splitString[1]);
            return new RemoveExisting(moveSpace, moveSpace2, player);
        }
        throw new IllegalArgumentException("Invalid move data: " + data);
    }
}


class PlaceNew extends Move {
    PlaceNew(int moveSpace, Player currentPlayer){
        super(moveSpace, currentPlayer);
    }

    @Override
    public void makeMove(Board board){
        board.putNewSphere(moveSpace, currentPlayer);
        currentPlayer.useSphere();
    };

    @Override
    public String serialize(){
        return "PlaceNew:" + moveSpace;
    }
}

class MoveExisting extends Move{
    private int fromSpace;
    MoveExisting( int fromSpace, int moveSpace, Player currentPlayer){
        super(moveSpace, currentPlayer);
        this.fromSpace = fromSpace;
    }

    @Override
    public void makeMove(Board board){
        board.removeSphere(fromSpace, currentPlayer);
        board.putNewSphere(moveSpace, currentPlayer);
    };
    @Override
    public String serialize(){
        return "MoveFrom:" + fromSpace + "," + moveSpace;
    }
}

class RemoveExisting extends Move{
    private int moveSpace2;
    RemoveExisting(int moveSpace, int moveSpace2, Player currentPlayer){
        super(moveSpace, currentPlayer);
        this.moveSpace2 = moveSpace2;
    }

    @Override
    public void makeMove(Board board){
        board.removeSphere(moveSpace, currentPlayer);
        currentPlayer.returnSphere();
        if (moveSpace2!=42){
            board.removeSphere(moveSpace2, currentPlayer);
            currentPlayer.returnSphere();
        }
    };
    @Override
    public String serialize(){
        if (moveSpace2!=42){
            return "RemoveFrom:" + moveSpace + "," + moveSpace2;
        } else return "RemoveFrom:" + moveSpace;

    }
}
