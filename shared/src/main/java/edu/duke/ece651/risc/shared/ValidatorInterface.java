package edu.duke.ece651.risc.shared;

import java.util.List;

public interface ValidatorInterface<T> {
  //this interface will be used to validate moves on regions and units are valid
   public boolean validateOrders(List<T> orders);
}
