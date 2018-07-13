package VirtualWorld.Animals;

import VirtualWorld.Animal;
import VirtualWorld.World;

public class Wolf extends Animal {

    public Wolf(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        strength = 9;
        initiative = 5;
    }
}
