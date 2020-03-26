package edu.duke.ece651.risc.server;
import edu.duke.ece651.risc.shared.*;

import java.net.*;
import java.util.*;
import java.io.*;

public class BoardGenerator {
  private Board board;

  public Board getBoard(){
    return this.board;
  }
  
  // Generate static board - generate tree to represent board
  public void createBoard(){
    List<Region> adjRegion = null;
    List<Region> regionList = new ArrayList<Region>();

    Region regionA = createRegion("A");
    Region regionB = createRegion("B");
    Region regionC = createRegion("C");
    Region regionD = createRegion("D");
    Region regionE = createRegion("E");
    Region regionF = createRegion("F");
    Region regionG = createRegion("G");
    Region regionH = createRegion("H");
    Region regionI = createRegion("I");
    Region regionJ = createRegion("J");
    Region regionK = createRegion("K");
    Region regionL = createRegion("L");
    // A
    adjRegion = createAdjRegions(regionB, regionF, regionL);
    addToRegionList(adjRegion, regionList, regionA);
    //B
    adjRegion = createAdjRegions(regionA, regionC, regionF);
    addToRegionList(adjRegion, regionList, regionB);
    //C
    adjRegion = createAdjRegions(regionB, regionD, regionE);
    addToRegionList(adjRegion, regionList, regionC);
    //D
    adjRegion = createAdjRegions(regionC, regionI, regionE);
    addToRegionList(adjRegion, regionList, regionD);
    //E
    adjRegion = createAdjRegions(regionF, regionD, regionC);
    adjRegion.add(regionH);
    addToRegionList(adjRegion, regionList, regionE);
    //F
    adjRegion = createAdjRegions(regionA, regionE, regionB);
    adjRegion.add(regionG);
    addToRegionList(adjRegion, regionList, regionF);
    //G
    adjRegion = createAdjRegions(regionH, regionK, regionF);
    adjRegion.add(regionL); 
    addToRegionList(adjRegion, regionList, regionG);
    //H
    adjRegion = createAdjRegions(regionG, regionI, regionE);
    adjRegion.add(regionJ);
    addToRegionList(adjRegion, regionList, regionH);
    //I
    adjRegion = createAdjRegions(regionD, regionJ, regionH);
    addToRegionList(adjRegion, regionList, regionI);
    //J
    adjRegion = createAdjRegions(regionI, regionH, regionK);
    addToRegionList(adjRegion, regionList, regionJ);
    //K
    adjRegion = createAdjRegions(regionG, regionJ, regionL);
    addToRegionList(adjRegion, regionList, regionK);
    //L
    adjRegion = createAdjRegions(regionG, regionA, regionK);
    addToRegionList(adjRegion, regionList, regionL);
    // Add to board
    this.board = new Board(regionList);
  }

  private void addToRegionList(List<Region> adjRegion, List<Region> regionList, Region regionA) {
    regionA.setAdjRegions(adjRegion);
    regionList.add(regionA);
  }

  private List<Region> createAdjRegions(Region regionB, Region regionF, Region regionL) {
    List<Region> adjRegion;
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionB);
    adjRegion.add(regionL);
    adjRegion.add(regionF);
    return adjRegion;
  }

  private Region createRegion(String name) {
    Region regionA = new Region();
    regionA.setName(name);
    regionA.setSize(1);
    regionA.setFoodProduction(1);
    regionA.setTechProduction(1);
    return regionA;
  }

}
