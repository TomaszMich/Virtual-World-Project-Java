package VirtualWorld.Plants;

import VirtualWorld.Organism;
import VirtualWorld.Plant;
import VirtualWorld.World;

public class DeadlyNightshade extends Plant {

    public DeadlyNightshade(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        strength = 99;
    }

    @Override
    public boolean collision(Organism org) {
        world.removeOrganism(org);
        return true;
    }
}
