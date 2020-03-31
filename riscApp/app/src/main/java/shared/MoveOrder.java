package shared;

public class MoveOrder extends SourceDestinationOrder {
    //this class defines how to execute a move order operation on the board
    private static final long serialVersionUID = 9L; //is there a more intuitive numbering we could use?

    public MoveOrder(Region s, Region d, Unit u){
        this.source = s;
        this.destination = d;
        this.units = u;
    }
    @Override
    public int getPriority(){
        return Constants.MOVE_PRIORITY;
    }

    @Override
    //TODO -- this should not return a new Unit bc that would clear all bonuses
    public String doSourceAction() {
        //remove units from source region
        //  source.setUnits(new Unit(source.getUnits().getTotalUnits() - this.units.getTotalUnits()));
        return "";
    }

    //player would need to know all their units and bonuses
    //

    @Override
    public String doDestinationAction(){
        //add units to destination region
        //TODO --WARNING: this line will erase all bonuses
        // destination.setUnits(new Unit(destination.getUnits().getUnits()+this.units.getUnits()));
        // TODO: will need to change units.getUnits() to number of units moved
        int cost = source.findShortestPath(destination).getTotalCost() * units.getTotalUnits();
        // updates player to food cost
        //destination.getOwner().useFood(cost);
        destination.getOwner().getResources().getFoodResource().useFood(cost);
        return (destination.getOwner().getName() + " moved " + units.getUnits() + " units from " + source.getName() + " to " + destination.getName() + "\n");
    }


}
