package edu.duke.ece651.risc.gui;

import edu.duke.ece651.risc.shared.Board;
import edu.duke.ece651.risc.shared.Connection;

public final class ParentActivity {
    private static Connection connection;
    private static Board board;
    final static Connection getConnection(){
        return connection;
    }
    public void setConnection(Connection connection){
        this.connection = connection;
    }
    final static Board getBoard(){return board;}
    public void setBoard(Board board){this.board = board;}
}
