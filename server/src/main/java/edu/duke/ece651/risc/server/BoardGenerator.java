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
    regionA.setSize(1);
    regionA.setFoodProduction(1);
    regionA.setTechProduction(1);
    Region regionB = new Region();
    regionB.setName("B");
    regionB.setSize(1);
    regionB.setFoodProduction(1);
    regionB.setTechProduction(1);
    Region regionC = new Region();
    regionC.setName("C");
    regionC.setSize(1);
    regionC.setFoodProduction(1);
    regionC.setTechProduction(1);
    Region regionD = new Region();
    regionD.setName("D");
    regionD.setSize(1);
    regionD.setFoodProduction(1);
    regionD.setTechProduction(1);
    Region regionE = new Region();
    regionE.setName("E");
    regionE.setSize(1);
    regionE.setFoodProduction(1);
    regionE.setTechProduction(1);
    Region regionF = new Region();
    regionF.setName("F");
    regionF.setSize(1);
    regionF.setFoodProduction(1);
    regionF.setTechProduction(1);
    Region regionG = new Region();
    regionG.setName("G");
    regionG.setSize(1);
    regionG.setFoodProduction(1);
    regionG.setTechProduction(1);
    Region regionH = new Region();
    regionH.setName("H");
    regionH.setSize(1);
    regionH.setFoodProduction(1);
    regionH.setTechProduction(1);
    Region regionI = new Region();
    regionI.setName("I");
    regionI.setSize(1);
    regionI.setFoodProduction(1);
    regionI.setTechProduction(1);
    Region regionJ = new Region();
    regionJ.setName("J");
    regionJ.setSize(1);
    regionJ.setFoodProduction(1);
    regionJ.setTechProduction(1);
    Region regionK = new Region();
    regionK.setName("K");
    regionK.setSize(1);
    regionK.setFoodProduction(1);
    regionK.setTechProduction(1);
    Region regionL = new Region();
    regionL.setName("L");
    regionL.setSize(1);
    regionL.setFoodProduction(1);
    regionL.setTechProduction(1);
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
