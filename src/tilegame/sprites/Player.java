
public class Player extends Creature {

    private static final float JUMP_SPEED = -.95f;

    private boolean onGround;

    public Player(Animation left, Animation right,
        Animation deadLeft, Animation deadRight)
    {
        super(left, right, deadLeft, deadRight);
    }

    public void collideHorizontal() {
        setVelocityX(0);
    }

    public void collideVertical() {
        if (getVelocityY() > 0) {
            onGround = true;
        }
        setVelocityY(0);
    }

    public void setY(float y) {
       if (Math.round(y) > Math.round(getY())) {
            onGround = false;
        }
        super.setY(y);
    }

    public void wakeUp() {
     
    }


    public void jump(boolean forceJump) {
        if (onGround || forceJump) {
            onGround = false;
            setVelocityY(JUMP_SPEED);
        }
    }

    public float getMaxSpeed() {
        return 0.5f;
    }
}
