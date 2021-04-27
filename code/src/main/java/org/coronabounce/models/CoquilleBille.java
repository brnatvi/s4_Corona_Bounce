package org.coronabounce.models;

import org.coronabounce.mvcconnectors.Controllable;

import java.util.Random;

public class CoquilleBille {

    /** A moving speed in x to move faster or slower. */
    private double movingSpeedX;
    /** A moving speed in y to move faster or slower. */
    private double movingSpeedY;
    private Population population;
    /**the individual that the Shell will contain**/
    private Individual individual;
    /** the current position the Shell**/
    private Position currentPosition;
     /**Shell identifier (in order to redefine equals)**/
    private final int id;
    /** the number of Shell already created**/
    private static int idCpt=0;
    private Random r = new Random();
    private double minReboundSpeed=3;
    /** Current controller contains all parameters **/
    private Controllable controller;
    private static int MAX_SPEED = 5;

    public CoquilleBille(double speedX,double speedY, Individual individual, Population pop){

        this.population = pop;
        this.individual = individual;
        this.controller = pop.getController();
        try {
          this.currentPosition = new Position(controller);
        }catch (Exception e) {
          this.currentPosition = new Position(controller,false);
          System.out.println("Position of the point have been set, but it fail to fined a free space.");
        }
        /**increment the number of Shells that exist**/
        id=idCpt++;
        /**update movinSpeedX**/
        this.movingSpeedX=speedX;
        /** update movingSpeedY**/
        this.movingSpeedY=speedY;
    }

    public CoquilleBille(Individual individual, Population pop){
        this(0,0,individual, pop);
        this.addSpeedX(MAX_SPEED);
        this.addSpeedY(MAX_SPEED);
    }

    //=============================================== getters/setters =================================================//

    public int getId(){return id;}

    public Position getCurrentPosition() {return this.currentPosition;}

    public double getMovingSpeedX() { return this.movingSpeedX;}
    public double getMovingSpeedY() { return this.movingSpeedY;}
    public double getMovingSpeed() {
        return Math.sqrt((this.movingSpeedX*this.movingSpeedX)+(this.movingSpeedY*this.movingSpeedY));
    }
    public void setMovingSpeed(double Vx, double Vy) {
        this.movingSpeedX=Vx;
        this.movingSpeedY=Vy;
    }

    public Individual getIndividual() {return individual;}
    public void setIndividual(Individual individual) { this.individual=individual; }
    public Population getPopulation(){ return individual.getPopulation(); }

    //=====================================================================================================//

    public String toString(){
      return id+"pos: "+currentPosition.toString()+" speed: "+movingSpeedX+" "+movingSpeedY+"\t"+individual+"\t"+getPopulation();
    }
    @Override
    public boolean equals(Object o){/**redefinition of equals**/
        if(o!= null && o instanceof CoquilleBille){
            return getId()==((CoquilleBille)(o)).getId();/**Compare Against Shell Id**/
        }
        return false;
    }
    /**
    *{@summary The moving funtion.}<br>
    *Speed will be modify if this hurt a limit of the zone.
    */
    public void move(){
        //bounceIfHitOtherCoquilleBille();
        this.currentPosition.setPos(this.currentPosition.getX()+this.movingSpeedX,this.currentPosition.getY()+this.movingSpeedY);
        bounceIfOutOfZone();
        bounceIfHitWall();
        ricochetAll();
    }
    //==================================================== Bounce off walls =================================================//
    /**
     *{@summary bounce if this will go out of the zone.}<br>
     */
    protected void bounceIfOutOfZone(){
         if (outOfX(currentPosition.getX()+movingSpeedX)) {/**check if the position X of the Shell reaches the boundary X mark of the rebound zone**/
              bounce(true);/**bounce along X**/
         }
         if (outOfY(currentPosition.getY()+movingSpeedY)) {/**check if the position Y of the Shell reaches the boundary Y mark of the rebound zone**/
              bounce(false);/**bounce along Y **/
         }
    }
    /**
     *{@summary bounce if this will hit a wall.}<br>
     */
    protected void bounceIfHitWall(){
        for (Wall wall : getPopulation().getListWall()) {
            if (wall.willCrossWallInX(this) && wall.willCrossWallInY(this)) {
                    bounce(true);
              }
        }
    }
//    /**
//     *{@summary bounce if this will hit an other CoquilleBille.}<br>
//     *All CoquilleBille that this can hit are in population.
//     */
//   public void bounceIfHitOtherCoquilleBille(){
//     for(CoquilleBille coc : population.getAllPoints()){
//       if(!coc.equals(this) && getCurrentPosition().distanceFrom(coc.getCurrentPosition()) <= (2* controller.getRadiusDot())){
//         if(coc.InX(this)){
//         coc.bounce(true);
//         this.bounce(true);
//         }
//         if(coc.InY(this)){
//           coc.bounce(false);
//           this.bounce(false);
//         }
//       }
//     }
//   }

    /**
     *{@summary bounce.}<br>
     *@param inX True if bounce in x coor.
     */
    protected void bounce(boolean inX){// elle sert a gerer les rebondissemnt
         if(inX){ movingSpeedX*=-1;}//inverser le vecteur vitesse Vx en le multipliant par -1
         else{ movingSpeedY*=-1; }//inverser le vecteur vitesse Vy en le multipliant par -1
    }

    /**
     *{@summary bounce in x and y.}<br>
     */
    protected void bounce(){
         bounce(true);
         bounce(false);
    }
    //==================================================== Private =================================================//
    /**
     *{@summary Return true if x coordinate is out the the Zone at next move.}
     *Public only for test.
     */
    public boolean outOfX(double x){
        if(x<= getPopulation().getRadiusDot() || x>= controller.getSpaceSize()[0]- getPopulation().getRadiusDot()){return true;}//verifie si la position de la Coquille dépasse les bornes de la zone par rapport a X
        return false;
    }
    /**
     *{@summary Return true if y coordinate is out the the Zone at next move.}
     */
    private boolean outOfY(double y){//verifie si la position de la Coquille dépasse les bornes de la zone par rapport a Y
        if(y<= getPopulation().getRadiusDot() || y>= controller.getSpaceSize()[1]- getPopulation().getRadiusDot()){return true;}
        return false;
    }

   // private boolean isBetween(double c, double a, double b){
   //     if( c<=b && c>=a ) return true;
   //     return false;
   // }

  //  public boolean InY(CoquilleBille coc){
  //       if((this.getCurrentPosition().getY()-coc.getCurrentPosition().getY()<10)&& (this.getCurrentPosition().getY()-coc.getCurrentPosition().getY()>=0)||(this.getCurrentPosition().getY()-coc.getCurrentPosition().getY()<10)&& (coc.getCurrentPosition().getY()-coc.getCurrentPosition().getY()>=0) ){
  //            return true;
  //       }else{
  //            return false;
  //       }
  //  }
  //  public boolean InX(CoquilleBille coc){
  //        if((this.getCurrentPosition().getX()-coc.getCurrentPosition().getX()<10)&& (this.getCurrentPosition().getX()-coc.getCurrentPosition().getX()>=0)||(this.getCurrentPosition().getX()-coc.getCurrentPosition().getX()<10)&& (coc.getCurrentPosition().getX()-coc.getCurrentPosition().getX()>=0) ){
  //             return true;
  //        }else{
  //             return false;
  //        }
  //  }

    //******************************************************************************************************************************//
    

    /**
    *{@summary Set a random moving speed to SpeedX between -maxSpeed &#38; maxSpeed.}<br>
    *@param maxSpeed the max speed to add.
    */
    public void addSpeedX(int maxSpeed){
        if(maxSpeed<1){maxSpeed=1;}
        if(r.nextBoolean()){maxSpeed=maxSpeed*(-1);}
        this.movingSpeedX = r.nextDouble()*maxSpeed;
    }

    /**
     *{@summary Set a random moving speed to SpeedY between -maxSpeed &#38; maxSpeed.}<br>
     *@param maxSpeed the max speed to add.
     */
    public void addSpeedY(int maxSpeed){
        if(maxSpeed<1){maxSpeed=1;}
        if(r.nextBoolean()){maxSpeed=maxSpeed*(-1);}
        this.movingSpeedY = r.nextDouble()*maxSpeed;
    }

    //============================================= Mutual Bounces =================================================//

    /**
     *{@summary Makes ricochet if necessary with all other points.}
     */
    public void ricochetAll()
    {
        for (CoquilleBille coc : population.getAllPoints())
        {
            if (!this.equals(coc))
            {
                this.ricochet(coc, false);
            }
        }
    }

    /**
     *{@summary Makes ricochet if it does not made yet.}
     *@param coc coquilleBille to make ricochet with.
     *@return true if ricochet was made.
     */
    public boolean ricochet(CoquilleBille coc, boolean isRicochet)
    {
        boolean isDone = false;
        if (!isRicochet && isNear(coc))
        {
            double tmpX = coc.getMovingSpeedX();
            double tmpY = coc.getMovingSpeedY();
            coc.setMovingSpeed(this.movingSpeedX, this.movingSpeedY);
            this.setMovingSpeed(tmpX, tmpY);
            isDone = true;
        }
        return isDone;
    }

    /**
     *{@summary Measure distance between current point and point passing as parameter.}
     *@param coc coquilleBille to measure distance with.
     *@return true if distance less than or equal to two radius of points (so they are contiguous).
     */
    public boolean isNear(CoquilleBille coc)
    {
        double cocX = coc.getCurrentPosition().getX();
        double cocY = coc.getCurrentPosition().getY();
        double currX = this.getCurrentPosition().getX();
        double currY = this.getCurrentPosition().getY();
        if (Math.sqrt(Math.pow(cocX - currX, 2) + Math.pow(cocY - currY, 2)) <= 2*controller.getRadiusDot())
        {
            return true;
        }
        return false;
    }

}
