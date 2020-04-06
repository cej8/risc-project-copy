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

    Region regionA = createRegion("Caprica");
    Region regionB = createRegion("Hoth");
    Region regionC = createRegion("Worlon");
    Region regionD = createRegion("Dagobah");
    Region regionE = createRegion("Ego");
    Region regionF = createRegion("Terra Prime");
    Region regionG = createRegion("Arda");
    Region regionH = createRegion("Dune");
    Region regionI = createRegion("Solaris");
    Region regionJ = createRegion("Gallifrey");
    Region regionK = createRegion("Cybertron");
    Region regionL = createRegion("Krypton");
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
    Region regionA = new Region(name);
    regionA.setSize(10);
    regionA.setFuelProduction(20);
    regionA.setTechProduction(15);
    return regionA;
  }

}
