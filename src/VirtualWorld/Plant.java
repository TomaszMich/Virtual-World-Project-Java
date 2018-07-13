package VirtualWorld;

import VirtualWorld.Tools.Possibility;

import java.util.Random;

public abstract class Plant extends Organism{

    protected Plant(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        this.initiative = 0;
    }

    @Override
    public void action() {
        int i = world.checkForCollision(this);
        Random generator = new Random();
        if ((i != -1 && collision(world.getOneOrganism(i))) || i == -1)
        {
            if (generator.nextInt(100) < Possibility.plant())
                reproduce();
        }
    }

    public void movement(int direction, int step)
    {}
}
