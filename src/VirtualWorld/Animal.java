package VirtualWorld;

import VirtualWorld.Tools.Direction;

import java.util.Random;

public abstract class Animal extends Organism{


    protected Animal(int positionX, int positionY, World world)
    {
        super(positionX, positionY, world);
    }

    @Override
    public void action()
    {
        Random generator = new Random();
        int direction = generator.nextInt(4);
        int i = world.checkForCollision(this);
        if (i != -1) {
            if (collision(world.getOneOrganism(i)))
                movement(direction, step);
        }
        else {
            movement(direction, step);
        }
    }

    @Override
    public boolean collision(Organism org)
    {
        if (org.getClass().equals(this.getClass()))
        {
            goBack(org);
            reproduce();
            return false;
        }
        else
            return super.collision(org);
    }

    protected void goBack(Organism org)
    {
        switch (org.getMovedTo())
        {
            case 0:
                org.movement(Direction.Down(), step);
                break;
            case 1:
                org.movement(Direction.Up(), step);
                break;
            case 2:
                org.movement(Direction.Right(), step);
                break;
            case 3:
                org.movement(Direction.Left(), step);
                break;
        }
    }

    protected void movement(int direction, int step)
    {
        int x = positionX;
        int y = positionY;
        int fail = 0;

        while (x == positionX && y == positionY && fail < 4) {
            if (direction == Direction.Up() && positionY != 0 && positionY != step - 1) {
                positionY -= step;
                movedTo = Direction.Up();
            } else if (direction == Direction.Down() && positionY != world.getSizeY() - 1 && positionY != world.getSizeY() - step) {
                positionY += step;
                movedTo = Direction.Down();
            } else if (direction == Direction.Left() && positionX != 0 && positionX != step - 1) {
                positionX -= step;
                movedTo = Direction.Left();
            } else if (direction == Direction.Right() && positionX != world.getSizeX() - 1 && positionX != world.getSizeX() - step) {
                positionX += step;
                movedTo = Direction.Right();
            } else if (direction == -1){
                break;
            }
            fail++;
            direction = (direction + 1) % 4;
        }
    }

    protected void movementOnFree(int step) {
        Random generator = new Random();
        int direction = generator.nextInt(4);
        int x = positionX;
        int y = positionY;
        int fail = 0;

        while (x == positionX && y == positionY && fail < 4)
        {
            if (direction == Direction.Up() && positionY != 0 && positionY != step - 1 && !world.isThereOrganism(x, y - step))
            {
                positionY -= step;
                movedTo = Direction.Up();
            }
            else if (direction == Direction.Down() && positionY != world.getSizeY() - 1 && positionY != world.getSizeY() - step && !world.isThereOrganism(x, y + step))
            {
                positionY += step;
                movedTo = Direction.Down();
            }
            else if (direction == Direction.Left() && positionX != 0 && positionX != step - 1 && !world.isThereOrganism(x - step, y))
            {
                positionX -= step;
                movedTo = Direction.Left();
            }
            else if (direction == Direction.Right() && positionX != world.getSizeX() - 1 && positionX != world.getSizeX() - step && !world.isThereOrganism(x + step, y))
            {
                positionX += step;
                movedTo = Direction.Right();
            }
            fail++;
            direction = (direction + 1) % 4;
        }
    }

}