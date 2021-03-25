package org.coronabounce.models;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Recovered extends Individual{

    public Recovered(CoquilleBille coc, Population p){
      super(coc,p);
       p.nbRecovered++;
      p.getT().schedule(new TimerTask() {
          @Override
          public void run() {
              coc.setIndividual(new Healthy(coc,p));
              p.nbRecovered --;

          }
      },p.getDurationNonContamination());


    }

}
