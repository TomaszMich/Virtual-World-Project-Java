package VirtualWorld.Plants;

import VirtualWorld.Organism;
import VirtualWorld.Plant;
import VirtualWorld.World;

public class Hogweed extends Plant {

    public Hogweed(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        this.strength = 10;
    }

    @Override
    public void action() {
        int up =world.findOrganism(positionX, positionY - 1);
        int down = world.findOrganism(positionX, positionY + 1);
        int left = world.findOrganism(positionX - 1, positionY);
        int right = world.findOrganism(positionX + 1, positionY);

        if (up != -1)
            world.removeOrganism(world.getOneOrganism(up));
        if (down != -1)
            world.removeOrganism(world.getOneOrganism(down));
        if (left != -1)
            world.removeOrganism(world.getOneOrganism(left));
        if (right != -1)
            world.removeOrganism(world.getOneOrganism(right));

        super.action();
    }

    @Override
    public boolean collision(Organism org) {
        world.removeOrganism(org);
        return true;
    }
}
