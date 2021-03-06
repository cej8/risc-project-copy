package edu.duke.ece651.risc.shared;

import java.util.*;

// Data structure to represent a path from one region to another (inclusive of start and end)
// Effectively list of regions in continuous path from first entry to last
public class Path {
  private List<Region> path;
  public Path(){
    path = new ArrayList<Region>();
  }
  public Path(Path copy){
     path = new ArrayList<Region>();
    for(Region r: copy.getPath()){
        this.add(r);
      }
  }
  public int getTotalCost(){
    int sum = 0;
    for(Region r: path){
      sum += r.getSize();
    }
    return sum;
  }
  public void add(Region r){
    path.add(r);
  }
public List<Region> getPath() {
	return path;
}


}
