package VirtualWorld.Plants;

import VirtualWorld.Organism;
import VirtualWorld.Plant;
import VirtualWorld.World;

public class Guarana extends Plant {

    public Guarana(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        this.strength = 0;
    }

    @Override
    public boolean collision(Organism org) {
        org.strengthPlus3();
        return super.collision(org);
    }
}
