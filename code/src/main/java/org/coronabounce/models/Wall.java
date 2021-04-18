package org.coronabounce.models;

import org.coronabounce.controllers.Controller;
import org.coronabounce.mvcconnectors.Controllable;

import java.util.*;
public class Wall  {
    private final int id;
    private static int cpt=0;
    private double thikness;
    private double positionX;
    private double positionY;
    private Controllable controller;

    public Wall(Controllable iController, double posX){//ce mur va separer la population en deux populations
       this.thikness=Controller.getThickness();
       this.positionX=posX;
       this.positionY=0;
       this.controller = iController;
       id=cpt++;
       System.out.println("new wall "+id+" from "+(positionX-thikness)+" to "+(positionX+thikness));//@a
    }
    public String toString(){return id+" x="+positionX+" y="+positionY+" th="+thikness;}

    public void setPositionY(double positionY) { this.positionY = positionY; }

    public double getPositionX() { return this.positionX; }
    public double getPositionY() { return positionY; }
    public double getThickness() { return this.thikness; }

    //On aurai pu mettre les murs dans la Zone et utiliser le timer de Zone dans population.
    public void makeWallGoDown(Population pop){
        TimerTask tt = null;
        pop.getT().schedule(tt = new TimerTaskWall(this.controller,this), 0, 100);
    }
    /**
    *{@summary Make CoquilleBille bounce if it will hit a wall.}
    *@param coc The CoquilleBille that we may make bounce.
    */
    //
    // public void separatePop1(CoquilleBille coc,int nbzones) {
    //
    //     //this.HitWallInX(coc);
    //     double posX = coc.getPosition().getX();
    //
    //
    //     int zone = InwhichZoneItis(posX, nbzones, this.thikness);
    //     double limitInf = repartInZones(nbzones,thikness)[zone - 1];
    //     double limitSup = repartInZones(nbzones,thikness)[zone]-thikness;
    //
    //     if (coc.getPosition().getX() < limitSup && Math.abs(posX - limitSup) <= 1)
    //     {
    //         coc.bounce(true);}
    //
    //     else if (coc.getPosition().getX() > limitInf && Math.abs(posX - limitInf) <= 1) {
    //
    //         coc.bounce(true);
    //     }
    // }


    /**
    *{@summary Return true if it will cross the wall in x.}<br>
    *@param coc The CoquilleBille that we may make bounce.
    */
    public boolean willCrossWallInX(CoquilleBille coc){

      double curentX = coc.getPosition().getX();
      double futurX = curentX+coc.getMovingSpeedX();
      double radius = coc.getPop().getRadiusDot();
      radius/=2;
      if(curentX < positionX-thikness/2 && futurX > positionX-thikness/2){ return true;}
      if(curentX > positionX+thikness/2 && futurX < positionX+thikness/2){ return true;}
      // if(curentX+radius/2 < positionX-thikness/2 && futurX-radius/2 > positionX-thikness/2){ return true;}
      // if(curentX-radius/2 > positionX+thikness/2 && futurX+radius/2 < positionX+thikness/2){ return true;}
      if(curentX-radius < positionX-thikness/2 && futurX+radius > positionX+thikness/2){ return true;}
      if(curentX+radius > positionX+thikness/2 && futurX-radius < positionX+thikness/2){ return true;}
      // if(curentX+radius < positionX-thikness/2 && futurX+radius > positionX+thikness/2){ return true;}
      // if(curentX-radius > positionX+thikness/2 && futurX-radius < positionX+thikness/2){ return true;}
      // if(curentX-radius < positionX-thikness/2 && futurX-radius > positionX+thikness/2){ return true;}
      // if(curentX+radius > positionX+thikness/2 && futurX+radius < positionX+thikness/2){ return true;}
      return false;
    }

    // public void HitWallInX(CoquilleBille coc,int nbzones) {
    //
    //
    //
    //     int number=nbzones;
    //     double curentX = coc.getPosition().getX();
    //     double futurX = curentX + coc.getMovingSpeedX();
    //     double curentY = coc.getPosition().getY();
    //     double futurY = curentY + coc.getMovingSpeedY();
    //
    //     int currentZone = InwhichZoneItis(curentX, number, thikness);
    //     int futurZone = InwhichZoneItis(futurX, number, thikness);
    //     double limitSup = repartInZones(number, thikness)[currentZone];
    //     double limitInf = repartInZones(number, thikness)[currentZone - 1];
    //     if (futurZone != currentZone) {
    //
    //         if (futurZone > currentZone) coc.getPosition().setPos(limitSup - thikness - 1, futurY);
    //         if (futurZone < currentZone) coc.getPosition().setPos(limitInf + 1, futurY);
    //     }
    //     else coc.getPosition().setPos(coc.getPosition().getX()+coc.getMovingSpeedX(),coc.getPosition().getY()+coc.getMovingSpeedY());
    //
    // }





    /**
    *{@summary Return true if it will cross the wall in y.}<br>
    *It will hit in Y only if wall is enoth low.<br>
    *@param coc The CoquilleBille that we may make bounce.
    */
    public boolean willCrossWallInY(CoquilleBille coc){
      if(positionY > coc.getPosition().getY()){
        return true;
      }
      return false;
    }

    // public double[] repartInZones(int nombre,double th){
    //     //<>
    //
    //
    //     /* A table with delimiters */
    //     double [] limits = new double[nombre+1];
    //     limits[0]=0;
    //
    //     for(int i=1;i<nombre;i++){
    //         limits[i]=  (i*(Controller.getWidth()/nombre)+th);
    //     }
    //     limits[nombre]=Controller.getWidth();
    //     return limits;
    // }
    //
    // public int InwhichZoneItis(double posX,int nombre,double th){
    //     double [] tab=repartInZones(nombre,th);
    //     for(int i=0;i<nombre;i++){
    //         if(posX>=tab[i] && posX<=tab[i+1]) return i+1 ;
    //     }
    //     return -1;/* Not in any Zone ! */
    // }













}
class TimerTaskWall extends TimerTask{
    private Wall w;
    private Controllable controller;

    public TimerTaskWall(Controllable iController, Wall w){
        this.w = w;
        this.controller = iController;
    }
    @Override
    public synchronized void run(){
        if (this.controller.getState() == Controllable.eState.Working){
            w.setPositionY(w.getPositionY()+1);//le mur avance petit a petit pour aller de la postio y=0 et attendre y=zone.height
            // System.out.println(w.getPositionY());
            // System.out.println(w.getPositionX());
            if(w.getPositionY()>Controller.getHeight()){
                cancel();
            }
        }
    }
}
