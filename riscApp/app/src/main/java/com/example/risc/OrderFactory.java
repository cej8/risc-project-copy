package com.example.risc;
import shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class OrderFactory {
    public static OrderInterface getOrder(String unitKeyWord, Region source, Region destination, Unit units){
        OrderInterface order = null;
        switch (unitKeyWord) {
            case "move":
                order = new MoveOrder(source, destination, units);
                break;
            case "attack":
                order = new AttackOrder(source, destination, units);
                break;
            case "upgrade":
                // TODO: something to the effect of order = new UpgradeOrder(source);
                break;
            default: //TODO: what would this be?
                break;
        }
        return order;
    }


}
