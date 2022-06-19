package Logic.Entities.Senses;

import Logic.Entities.Entity;

import java.util.ArrayList;

public class SoundSense {
    private final int hearRadius;
    private boolean walkSound;
    protected boolean shoutSound;
    private ArrayList<Sound> sounds;

    Entity entity;


    public SoundSense(Entity entity) {
        this.hearRadius = 15;
        this.walkSound = false;
        this.shoutSound = false;
        this.sounds = new ArrayList<>();
        this.entity = entity;
    }

    public void update(){
        canHear(entity.gamePanel.entityM.guards, entity.gamePanel.entityM.intruders);
    }


    private void canHear(Entity[] guards, Entity[] intruders){
        sounds.clear();

        for(int i = 0; i<guards.length; i++){
            if(entity.getId() != guards[i].getId()){
                if(guards[i].soundSense.isWalkSound() == true && entity.movement.distanceBetween(guards[i].movement.getX(), guards[i].movement.getY()) <= hearRadius){
                    double[] dirVector = entity.movement.directionToCoords(guards[i].movement.getX(), guards[i].movement.getY());
                    sounds.add(new Sound(dirVector[0], dirVector[1], false));
                }
                if( guards[i].soundSense.isShoutSound() == true && entity.movement.distanceBetween(guards[i].movement.getX(), guards[i].movement.getY()) <= hearRadius){
                    double[] dirVector = entity.movement.directionToCoords(guards[i].movement.getX(), guards[i].movement.getY());
                    sounds.add(new Sound(dirVector[0], dirVector[1], true));
                }
            }
        }


        for(int i = 0; i<intruders.length; i++){
            if(intruders[i] != null){
                if(entity.getId() != intruders[i].getId()){
                    if(intruders[i].soundSense.isWalkSound() == true && entity.movement.distanceBetween(intruders[i].movement.getX(), intruders[i].movement.getY()) <= hearRadius){
                        double[] dirVector = entity.movement.directionToCoords(intruders[i].movement.getX(), intruders[i].movement.getY());
                        sounds.add(new Sound(dirVector[0], dirVector[1], false));
                    }
                }
            }
        }
    }

    public void setWalkSound(boolean walkSound) {
        this.walkSound = walkSound;
    }

    public void setShoutSound(boolean shoutSound) {
        this.shoutSound = shoutSound;
    }

    public int getSoundsSize() {
        return sounds.size();
    }

    public boolean isWalkSound() {
        return walkSound;
    }

    public boolean isShoutSound() {
        return shoutSound;
    }
}
