package shared;

import java.io.Serializable;

public class TechResources implements Serializable{
    private static final long serialVersionUID = 19L;
    private int techStash;

    public TechResources(int startingTech){
        this.techStash = startingTech;
    }
    public int getTech(){
        return this.techStash;
    }
    public void addTech(int tech){
        this.techStash = techStash + tech;
    }
    public void useTech(int tech){
        this.techStash = techStash - tech;
    }
}
