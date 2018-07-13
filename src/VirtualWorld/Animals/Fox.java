package VirtualWorld.Animals;

import VirtualWorld.Animal;
import VirtualWorld.World;

public class Fox extends Animal {

    public Fox(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        strength = 3;
        initiative = 7;
    }

    @Override
    public void action() {
        super.action();
        int i = world.checkForCollision(this);
        if (i != -1 && world.getOneOrganism(i).getStrength() > this.strength)
            goBack(this);
    }
}
