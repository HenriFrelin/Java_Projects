import greenfoot.*;

/**
 * Write a description of class Bird here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bird extends Actor
{
    double dy = 0.0;
    double g = .5;
    double BOOST_SPEED = -10;
    /**
     * Act - do whatever the Bird wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        setLocation(getX(),(int)(getY() + dy));
      
        if (Greenfoot.isKeyDown("space") == true) {
            dy = BOOST_SPEED;
            setRotation(-30);
        }
        if (Greenfoot.isKeyDown("space") == false){
            setRotation(0);
        }    
      
        
        if (dy > 5){
            setRotation(10);
        }
        if (dy > 5 && dy < 10){
            setRotation(20);
        }
        if (dy > 10 && dy < 15){
            setRotation(50);
        }
        if (dy > 15 && dy < 20){
            setRotation(75);
        }
        
        if (dy > 20){
            setRotation(90);
        }
        
        
        if (getY() < -5 || getY() > 405) {
            GameOver end = new GameOver();
            getWorld().addObject(end,getWorld().getWidth()/2,getWorld().getHeight()/2);
            Greenfoot.stop();
        }
        dy += g;
    }
}
