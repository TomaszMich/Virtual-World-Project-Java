package VirtualWorld.Animals;

import VirtualWorld.Animal;
import VirtualWorld.World;

public class Sheep extends Animal {

    public Sheep(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        strength = 4;
        initiative = 4;
    }
}
