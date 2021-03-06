package org.coronabounce.models;

import org.coronabounce.mvcconnectors.Controllable;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Field where CoquilleBilles move.
 * It has an independent timer to manage movements.
 */
public class Zone  {
    /** Current controller contains all parameters **/
    private Controllable controller;
    /** Population that is in the Zone **/
    private Population p;
    /** Width of Zone **/
    private double width;
    /** Height of Zone **/
    private double height;
    /** Timer of Zone **/
    private Timer timer;
    /** TimerTask of Zone **/
    private TimerTask timerTask;


    // CONSTRUCTORS ------------------------------------------------------------
    public Zone (Controllable controller,boolean isLockDown, boolean isWall, boolean isRestrictionMovement){
        if (this.timer != null) { this.stopTimerTask(true);}
        timer=new Timer();
        this.controller = controller;
        this.width = controller.getSpaceSize()[0];
        this.height = controller.getSpaceSize()[1];
        if(isLockDown) {
            int nbpersons=controller.getPersonsCount();
            this.p = new Population(controller,nbpersons-nbpersons/6,nbpersons/6,0,isLockDown,isWall,isRestrictionMovement);
        }else{
            this.p = new Population(controller, controller.getPersonsCount(),isLockDown, isWall, isRestrictionMovement);
        }
    }

    // GET SET -----------------------------------------------------------------
    public double getWidth() { return width; }
    public void setWidth(double w){ if(w>=1){width = w;}}
    public double getHeight() { return height; }
    public void setHeight(double h) { if(h>=1){height = h;}}
    public Controllable getController() { return controller; }
    public Population getPopulation() {return p;}


    // FUNCTIONS ---------------------------------------------------------------
    /**
     * Stop timer task.
     * @param stopTimerTo If true also stop the timer itself.
     */
    public void stopTimerTask(boolean stopTimerTo){
      if (null != this.timerTask){
        if (!this.timerTask.cancel()){
          System.out.println("Can't cancel task!");
        }
        this.timerTask = null;
        this.timer.purge();
      }
      if (stopTimerTo){
        this.timer.cancel();
        this.timer = null;
      }
    }

    /**
     * {@summary Make CoquilleBille move every 33 ms. }
     */
    public void moving(){
        // stopTimerTask(false); //Stop last timer task.
        this.timer.schedule(this.timerTask=new TimerTask() {
            @Override
            public void run(){
                if (controller.getState() == Controllable.eState.Working){
                    p.Contacts();
                    p.makeBilleMove();
                }
            }
        },0,33);

    }
}
