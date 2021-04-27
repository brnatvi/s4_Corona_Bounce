package org.coronabounce.models;

import org.coronabounce.mvcconnectors.Controllable;

import java.util.TimerTask;


public class Sick extends Individual {

  public Sick(CoquilleBille coc, Population p){
      super(coc,p);
      p.nbSick++;/**increase  the number of sick individuals**/
      p.getTimer().schedule(new TimerTask()/**plan an action that will take place after the healing period**/
      {
          @Override
          public void run()
          {
              if (p.getController().getState() == Controllable.eState.Working)
              {
                  coc.setIndividual(new Recovered(coc, p));/**overwrite the Sick   individual that exists in the shell and replace it with Recovered individual**/
                  p.nbSick--;/**decrease the number of Sick individuals**/
              }
          }
      },p.getDurationHealing());
  }
  

    public void agitSur() {
        contaminate();
    }
    /**
    *A function that transform to Incubating an Individual if :
    *<ul>
    *<li> It is a  different Individual.
    *<li> It is close to this.
    *<li> It is a Healthy Individual.
    *</ul>
    */
    private void contaminate(){
        for(CoquilleBille c : population.getAllPoints()){/**browse the list of Shells**/
            if(!coc.equals(c) && population.distance(coc,c)<= population.getContaminationRadius() && c.getIndividual() instanceof Healthy){
                c.setIndividual(new Incubating(c, population));
            }
        }
    }
    @Override
    public boolean isSick(){return true;}

}
