import greenfoot.*;

/**
 * Write a description of class BackDrop here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class BackDrop extends World
{

    /**
     * Constructor for objects of class BackDrop.
     * 
     */
    public BackDrop()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(600, 400, 1, false); 
        Bird flappy = new Bird();
        addObject(flappy,100,getHeight()/2);

        prepare();
    }

    /**
     * Prepare the world for the start of the program. That is: create the initial
     * objects and add them to the world.
     */
    private void prepare()
    {
    }
}
