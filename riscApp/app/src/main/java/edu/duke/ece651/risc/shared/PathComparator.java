package edu.duke.ece651.risc.shared;

import java.util.*;
// Defines ordering of a collection to be sorted from smallest to largest by path cost
public class PathComparator implements Comparator<Path> {

	@Override
	public int compare(Path path1, Path path2) {
    //maintains ascndeing order of path cost (least to greatest)
    if(path1.getTotalCost()<path2.getTotalCost()){
        return -1;
      }
    else if(path1.getTotalCost()>path2.getTotalCost()){
        return 1;
      }
         
		return 0;
	}


}
