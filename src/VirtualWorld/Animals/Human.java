package VirtualWorld.Animals;

import VirtualWorld.Animal;
import VirtualWorld.Organism;
import VirtualWorld.World;

public class Human extends Animal {

    public Human(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
        strength = 5;
        initiative = 4;
    }

    @Override
    public void action() {
        int direction = world.getHumanDirection();
        int i = world.checkForCollision(this);
        if (i != -1) {
            if (collision(world.getOneOrganism(i)))
                movement(direction, step);
        }
        else {
            movement(direction, step);
        }
        world.setHumanDirection(-1);
    }

    @Override
    public boolean collision(Organism org) {
        if (world.getAbility() >= 0 && world.getAbility() < 5)
        {
            if (this.strength > org.getStrength()) {
                world.removeOrganism(org);
                return true;
            }
            else
                movementOnFree(step);
                return false;
        }
        else
            return super.collision(org);
    }
}
