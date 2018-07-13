package VirtualWorld.Plants;

import VirtualWorld.Plant;
import VirtualWorld.World;

public class Grass extends Plant {

    public Grass(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        this.strength = 0;
    }
}
