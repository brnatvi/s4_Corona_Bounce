package org.coronabounce.models;

import org.coronabounce.data.Data;
import org.coronabounce.mvcconnectors.Controllable;
import org.coronabounce.mvcconnectors.Displayable;

import java.util.*;

public class Population implements Displayable {

    /** Current controller contains all parameters **/
    private Controllable controller;
    /** Contains the population "CoquilleBille"  list **/
    private List<CoquilleBille> listCoquille = new ArrayList<CoquilleBille>();
    /** Collect the counts of healthy, sick, incubating, recovered in form of FIFO **/
    private Data data;
    /** Sick people count **/
    public int nbSick;
    /** Healthy people count **/
    public int nbHealthy;
    /** Recovered people count **/
    public int nbRecovered;
    /** Incubating people count ( got the virus but not sick ) **/
    public int nbIncubating;
    /** Boundaries (walls) list **/
    private List<Wall> listWall = new ArrayList<Wall>();
    private Timer timer;
    private TimerTask timerTask = null;
    private static Random random = new Random();

    //============================= Constructors ======================================================================/

    public Population(Controllable controller, int nbH, int nbS, int nbR,boolean isLockDown, boolean isWall, boolean isRestrictionMovement) {
        this.controller = controller;
        data = new Data();
        timer = new Timer();
        Position.cleanListTakenPositions();

        if(isLockDown){  /** In case we apply "LockDown" scenario, we need to create "ConfinedBille(S)" **/

            for (int i = 0; i < nbH; i++) {
                CoquilleBille coc = new ConfinedBille(null, this);
                Individual in = new Healthy(coc, this);
                coc.setIndividual(in);
                listCoquille.add(coc);
            }
            for (int i = 0; i < nbS; i++) {
                CoquilleBille coc = new ConfinedBille(null, this);

                Individual in = new Sick(coc, this);
                coc.setIndividual(in);
                listCoquille.add(coc);
            }
            for (int i = 0; i < nbR; i++) {
                CoquilleBille coc = new ConfinedBille(null, this);
                Individual in = new Recovered(coc, this);
                coc.setIndividual(in);
                listCoquille.add(coc);
            }
        }
        else {
            for (int i = 0; i < nbH; i++) {
                CoquilleBille coc = new CoquilleBille(null, this);/**First, create an empty "CoquilleBille" **/
                Individual in = new Healthy(coc, this);/** create a Healthy individual **/
                coc.setIndividual(in);/** Put a Healthy individual in the "CoquilleBille" **/
                listCoquille.add(coc);/** Add this "Coquille" to the list **/
            }
            for (int i = 0; i < nbS; i++) {
                CoquilleBille coc = new CoquilleBille(null, this);/**First, create an empty "CoquilleBille" **/
                Individual in = new Sick(coc, this);/** create a Sick individual **/
                coc.setIndividual(in);/** Put a Sick individual in the "CoquilleBille" **/
                listCoquille.add(coc);/** Add this "Coquille" to the list **/
            }
            for (int i = 0; i < nbR; i++) {
                CoquilleBille coc = new CoquilleBille(null, this);/**First, create an empty "CoquilleBille" **/
                Individual in = new Recovered(coc, this);/** create a Recovered individual **/
                coc.setIndividual(in); /** Put a Recovered individual in the "CoquilleBille" **/
                listCoquille.add(coc); /** Add this "Coquille" to the list **/
            }

        }
        if(isRestrictionMovement) {this.RestrictMovement();}/**In case , "RectrictedMouvement" scenario is applied, method RestrictMouvement() is called **/
        if(isWall){ /** In "Boundaries" scanario , we create Walls **/
          createWalls(controller.getWallsCount());/** Get the walls' number via the Controller, and create that many (add them the the walls' list ) **/
          for (Wall wall : listWall ) {
            wall.makeWallGoDown(this);/** Make the wall go down little by little **/
          }
        }

    }

    public Population(Controllable controller, int nbIndividus,boolean isLockDown, boolean isWall, boolean isRestrictionMovement) {
        this(controller, nbIndividus - 1, 1, 0,isLockDown,isWall,isRestrictionMovement);
    }

    //===========================  Getters/Setters ====================================================================/

    /**
     * {@summary Share controller.}
     */
    public Controllable getController() { return controller; }

    /**
     * {@summary Get list of points/individuals.}
     */
    public List<CoquilleBille> getAllPoints() { return listCoquille; }

    /**
     * {@summary Get radius of dot/individual.}
     * @return radius dot.
     */
    public double getRadiusDot() { return controller.getRadiusDot(); }

    /**
     * {@summary Get timer created in Population.}
     */
    public Timer getTimer() { return timer; }

    /**
     * {@summary Get list of walls.}
     */
    public List<Wall> getListWall() { return listWall; }
    //Use only by test.
    public void setListWall(List<Wall> l){listWall=l;}

    //========================= Virus Getters/Setters =================================================================/

    /**
     * {@summary Get duration of incubation.}
     */
    public long getDurationIncubation() { return controller.getDurationIncubation(); }

    /**
     * {@summary Get duration of non-contamination after recovery.}
     */
    public long getDurationImmunity() { return controller.getDurationImmunity(); }

    /**
     * {@summary Get duration of sickness.}
     */
    public long getDurationHealing() { return controller.getDurationHealing(); }

    /**
     * {@summary Get radius of contamination around point.}
     * @return contamination radius.
     */
    public double getContaminationRadius() { return controller.getContaminationRadius(); }

    //========================= Points Interactions ===================================================================/

    public void addIndividual(Individual i) { /** Create a "CoquilleBille" , put in the Individual "i" and add it the the CoquilleBille ' list **/
        CoquilleBille coc = new CoquilleBille(i, this);
        listCoquille.add(coc);
    }

    public void Contacts(){
        for(CoquilleBille coc:listCoquille){
            coc.getIndividual().agitSur();
        }

    }

    private double dist(Wall w,CoquilleBille coc){/** Calculate distance between a "CoquilleBille" and a "Wall" **/
        double x1 = coc.getCurrentPosition().getX();/** The X  position of the CoquilleBille **/
        double x2 = w.getPositionX();/** The X position of the Wall **/
        double y1 = coc.getCurrentPosition().getY();/** The Y position of the CoquilleBille **/
        double y2 = w.getPositionY();/** The Y position of the Wall **/
        return  Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }


    public double distance(CoquilleBille i1, CoquilleBille i2) { /** Calculate distance between two  "CoquilleBille" (s)  **/
        return i1.getCurrentPosition().distanceFrom(i2.getCurrentPosition());
    }

    //*****************************************************Strict lockdown*************************************************/
    /**
     *
     * RestrictMovement() prevent a big part of the population from moving
     olny(doctors, cops ..) go to work and people go out rarely and just out of necessity **/

    public void  RestrictMovement()
    {
        int prctg=random.nextInt(10)+70;
        int cpt = (prctg * this.getNbIndividus()) / 100;

        while (cpt > 0) {
            int index = random.nextInt(this.getNbIndividus());
            /** make sure to get a new coquille(check if the coquille has already been chosen or not) **/
            while (this.listCoquille.get(index).getMovingSpeed() == 0) index = random.nextInt(this.getNbIndividus());
            this.listCoquille.get(index).setMovingSpeed(0, 0);
            cpt--;
        }
    }


    //========================= Prints ================================================================================/

    /**
     * {@summary Print for debug, internal using function}
     */
    private void printPop() {

        int i = 0;
        for (CoquilleBille coc : listCoquille) {
            System.out.printf("Individu num : %d de position suivante  %.3f et  %.3f et de etat de sante  %s  Vitesse : %.3f \n", i, coc.getCurrentPosition().getX(), coc.getCurrentPosition().getY(), coc.getIndividual().healthState(), coc.getMovingSpeed());
            i++;
        }
    }

    /**********************************************Moving of Billes*************************************************/

    public void makeBilleMove() {
        for (CoquilleBille coc : listCoquille) {
          coc.move();
        }
    }

    //================================ Functions for Walls ============================================================/

    /**
     *{@summary Create the walls.}<br>
     *All the wall will be create at equals distance from eatch other.<br>
     *@param numberOfWall the number of wall that will be add.
     */
    private boolean createWalls(int numberOfWall){
        double maxX = controller.getSpaceSize()[0];
        for (int i = 1; i <= numberOfWall; i++) {
            double posX = (maxX * i)/(numberOfWall + 1);
            listWall.add(new Wall(this.controller, posX));
        }
        if(numberOfWall>0){
            return true;
        }
        else{
            return false;
        }
    }

    /**
    *{@summary Return how much zone there is.}
    */
    public int getNbZones(){ return this.getNbWall()+1; }

    /**
    *{@summary Return how much wall there is.}
    */
    public int getNbWall(){ return listWall.size(); }

    /**
     * {@summary Return list of positions on axis of abscissas of all walls.}
     */
    public ArrayList<Double> getPositionsOfWalls()
    {
        ArrayList<Double> rez = new ArrayList();
        for (int i = 0; i < listWall.size(); i++)
        {
            rez.add(listWall.get(i).getPositionX());
        }
        return rez;
    }

    /**
     * {@summary Return list of heights of all walls.}
     */
    public ArrayList<Double> getHeigthsOfWalls()
    {
        ArrayList<Double> rez = new ArrayList();
        for (int i = 0; i < listWall.size(); i++)
        {
            rez.add(listWall.get(i).getPositionY());
        }
        return rez;
    }

    /**
     * {@summary Return list of thicknesses of all walls. }
     */
    public ArrayList<Double> getThicknessesOfWalls()
    {
        ArrayList<Double> rez = new ArrayList();
        for (int i = 0; i < listWall.size(); i++)
        {
            rez.add(listWall.get(i).getThickness());
        }
        return rez;
    }

    //========================= Population Statistics =================================================================/

    /**
     * {@summary Get total number of points / number of Individuals. }
     */
    public int getNbIndividus() { return getAllPoints().size(); }

    /**
     * {@summary Get number of healthy}
     */
    public int getNbHealthy() { return nbHealthy; }

    /**
     * {@summary Get number of infected people (sick + incubating).}
     */
    public int getNbInfected() { return (nbSick + nbIncubating); }

    /**
     * {@summary Get number of recovered. }
     */
    public int getNbRecovered() { return nbRecovered; }

    /**
     * {@summary Transfers NbSick and NbRecovered to Data to save them to draw AreaChart.}
     * To show correctly superposed layers in AreaChart we take:
     * <ul>
     * <li> NbHealthy taken as 100% (bottom layer)
     * <li> nbSick = nbSick + NbIncubating + nbRecovered (middle layer)
     * <li> NbRecovered = NbRecovered (top layer)
     * </ul>
     * Superposed they present ratio of these tree values (nbHealthy, nbSick/Incubating and nbRecovered) in 100%.
     */
    @Override
    public void saveStatToData()
    {
        getTimer().schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                if (controller.getState() == Controllable.eState.Working){
                    data.setData(100 * (nbSick + nbIncubating + nbRecovered) / controller.getPersonsCount(), 100 * nbRecovered / controller.getPersonsCount());
                    //System.out.println("Statistic Thread run " + Thread.currentThread().getId());
                }
            }
        }, 0, 100);
    }

    /**
     * {@summary Get saved statistics (history) }
     * @return Data - history
     */
    @Override
    public Data getData() { return this.data; }

    //============================= Timer Management ===================================================================/

    /**
     * {@summary Close timer to stop using this population.}
     */
    public void stopTimer(boolean b_StopTimer)
    {
        if (null != this.timerTask)
        {
            if (!this.timerTask.cancel())
            {
                System.out.println("Can't cancel task!\n");
            }
            this.timerTask = null;
            this.timer.purge();
        }
        if (b_StopTimer)
        {
            this.timer.cancel();
            this.timer = null;
        }
    }
}
