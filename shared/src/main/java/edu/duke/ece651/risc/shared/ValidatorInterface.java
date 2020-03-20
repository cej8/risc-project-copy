package edu.duke.ece651.risc.shared;

import java.util.List;
public interface ValidatorInterface<T extends OrderInterface> {

  public boolean validateOrders(List<T> orders);
  public boolean validateUnits(List<T> orders);
  public boolean validateRegions(List<T> orders);
}
