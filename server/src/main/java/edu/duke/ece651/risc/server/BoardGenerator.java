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
    List<Region> adjRegion = new ArrayList<Region>();
    List<Region> regionList = new ArrayList<Region>();
    Region regionA = new Region();
    regionA.setName("A");
    Region regionB = new Region();
    regionB.setName("B");
    Region regionC = new Region();
    regionC.setName("C");
    Region regionD = new Region();
    regionD.setName("D");
    Region regionE = new Region();
    regionE.setName("E");
    Region regionF = new Region();
    regionF.setName("F");
    Region regionG = new Region();
    regionG.setName("G");
    Region regionH = new Region();
    regionH.setName("H");
    Region regionI = new Region();
    regionI.setName("I");
    Region regionJ = new Region();
    regionJ.setName("J");
    Region regionK = new Region();
    regionK.setName("K");
    Region regionL = new Region();
    regionL.setName("L");
    // A
    adjRegion.add(regionB);
    adjRegion.add(regionL);
    adjRegion.add(regionF);
    regionA.setAdjRegions(adjRegion);
    regionList.add(regionA);
    // B
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionA);
    adjRegion.add(regionF);
    adjRegion.add(regionC);
    regionB.setAdjRegions(adjRegion);
    regionList.add(regionB);
    // C
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionB);
    adjRegion.add(regionE);
    adjRegion.add(regionD);
    regionC.setAdjRegions(adjRegion);
    regionList.add(regionC);
    // D
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionC);
    adjRegion.add(regionE);
    adjRegion.add(regionI);
    regionD.setAdjRegions(adjRegion);
    regionList.add(regionD);
    // E
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionF);
    adjRegion.add(regionC);
    adjRegion.add(regionD);
    adjRegion.add(regionH);
    regionE.setAdjRegions(adjRegion);
    regionList.add(regionE);
    // F
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionA);
    adjRegion.add(regionB);
    adjRegion.add(regionE);
    adjRegion.add(regionG);
    regionF.setAdjRegions(adjRegion);
    regionList.add(regionF);
    // G
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionH);
    adjRegion.add(regionF);
    adjRegion.add(regionK);
    adjRegion.add(regionL); 
    regionG.setAdjRegions(adjRegion);
    regionList.add(regionG);
    // H
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionG);
    adjRegion.add(regionE);
    adjRegion.add(regionI);
    adjRegion.add(regionJ);
    regionH.setAdjRegions(adjRegion);
    regionList.add(regionH);
    // I
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionD);
    adjRegion.add(regionH);
    adjRegion.add(regionJ);
    regionI.setAdjRegions(adjRegion);
    regionList.add(regionI);
    // J
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionI);
    adjRegion.add(regionK);
    adjRegion.add(regionH);
    regionJ.setAdjRegions(adjRegion);
    regionList.add(regionJ);
    // K
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionG);
    adjRegion.add(regionL);
    adjRegion.add(regionJ);
    regionK.setAdjRegions(adjRegion);
    regionList.add(regionK);
    // L
    adjRegion = new ArrayList<Region>();
    adjRegion.add(regionG);
    adjRegion.add(regionK);
    adjRegion.add(regionA);
    regionL.setAdjRegions(adjRegion);
    regionList.add(regionL);
    // Add to board
    this.board = new Board(regionList);
  }

}
