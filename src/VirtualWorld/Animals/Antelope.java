package VirtualWorld.Animals;

import VirtualWorld.Animal;
import VirtualWorld.Organism;
import VirtualWorld.World;

import java.util.Random;

public class Antelope extends Animal {

    public Antelope(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        this.strength = 4;
        this.initiative = 4;
        this.step = 2;
    }

    @Override
    public boolean collision(Organism org) {
        Random generator = new Random();
        if(generator.nextInt(100) < 50)
        {
            movementOnFree(step);
            return false;
        }
        else
            return super.collision(org);
    }
}
