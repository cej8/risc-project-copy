package edu.duke.ece651.risc.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import edu.duke.ece651.risc.shared.*;

import org.junit.jupiter.api.Test;

public class AttackValidatorTest {
  @Test
  public void test_attackValidator(){
    Board board = new Board();
    AbstractPlayer p1 = new HumanPlayer("p1");
    AbstractPlayer p2 = new HumanPlayer("p2");
    Unit u1 = new Unit(10);
    Unit u2 = new Unit(10);
    Unit u3 = new Unit(10);
    Region r1 = new Region(p1, u1);
    Region r2 = new Region(p2, u2);
    Region r3 = new Region(p2, u3);
    r1.setName("r1");
    r2.setName("r2");
    r3.setName("r3");
    r1.setAdjRegions(Arrays.asList(r2));
    r2.setAdjRegions(Arrays.asList(r1, r3));
    r3.setAdjRegions(Arrays.asList(r2));
    board.setRegions(Arrays.asList(r1, r2, r3));
    board.initializeSpies(Arrays.asList("p1", "p2"));

    
    ValidatorHelper vh1;
    ValidatorHelper vh2;
    List<OrderInterface> p1Orders;
    List<OrderInterface> p2Orders;

    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Can't start from another region
    p1Orders.add(new AttackMove(r2, r1, new Unit(1)));
    assert(!vh1.allOrdersValid(p1Orders));
    //Can't attack own region
    p2Orders.add(new AttackMove(r2, r3, new Unit(1)));
    assert(!vh2.allOrdersValid(p2Orders));

    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Can't attack with too many
    p1Orders.add(new AttackMove(r1, r2, new Unit(10)));
    assert(!vh1.allOrdersValid(p1Orders));
    //Can't attack not adjacent
    p2Orders.add(new AttackMove(r3, r1, new Unit(1)));
    assert(!vh2.allOrdersValid(p2Orders));

    p2.setPlayerResource(new PlayerResources(5, 100));

    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Can't attack negative
    p1Orders.add(new AttackMove(r1, r2, new Unit(-1)));
    assert(!vh1.allOrdersValid(p1Orders));
    //Overdraw on food
    p2Orders.add(new AttackMove(r2, r1, new Unit(6)));
    assert(!vh2.allOrdersValid(p2Orders));


    vh1 = new ValidatorHelper(p1, board);
    vh2 = new ValidatorHelper(p2, board);
    p1Orders = new ArrayList<OrderInterface>();
    p2Orders = new ArrayList<OrderInterface>();

    //Valid
    p1Orders.add(new AttackMove(r1, r2, new Unit(1)));
    assert(vh1.allOrdersValid(p1Orders));
    //Valid
    p2Orders.add(new AttackMove(r2, r1, new Unit(1)));
    assert(vh2.allOrdersValid(p2Orders));
  }

}
