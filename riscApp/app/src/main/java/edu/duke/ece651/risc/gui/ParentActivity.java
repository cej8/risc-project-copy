package edu.duke.ece651.risc.gui;

import edu.duke.ece651.risc.shared.Connection;

public final class ParentActivity {
    private static Connection connection;
    final static Connection getConnection(){
        return connection;
    }
    public void setConnection(Connection connection){
        this.connection = connection;
    }
}
