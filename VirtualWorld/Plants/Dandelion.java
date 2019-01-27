package VirtualWorld.Plants;

import VirtualWorld.Plant;
import VirtualWorld.Tools.Possibility;
import VirtualWorld.World;

import java.util.Random;

public class Dandelion extends Plant {

    public Dandelion(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        this.strength = 0;
    }

    @Override
    public void action()
    {
        super.action();
        Random generator = new Random();

        for (int i = 0; i < 2; i++)
        {
            if (generator.nextInt(100) < Possibility.plant())
                reproduce();
        }
    }

}
