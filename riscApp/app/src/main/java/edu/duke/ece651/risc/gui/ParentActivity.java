package edu.duke.ece651.risc.gui;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Connection;
import edu.duke.ece651.risc.shared.HumanPlayer;

public final class ParentActivity {
    private static Connection connection;
    private static Board board;
    private static HumanPlayer player;

    final static Board getBoard(){return board;}
    public void setBoard(Board b){this.board=b;}

    final static HumanPlayer getPlayer(){return player;}
    public void setPlayer(HumanPlayer p){this.player=p;}


    final static Connection getConnection(){
        return connection;
    }
    public void setConnection(Connection connection){
        this.connection = connection;
    }
}
