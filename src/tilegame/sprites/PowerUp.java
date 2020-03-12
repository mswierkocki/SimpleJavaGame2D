package com.brackeen.javagamebook.tilegame.sprites;

import java.lang.reflect.Constructor;
import com.brackeen.javagamebook.graphics.*;


public abstract class PowerUp extends Sprite {

    public PowerUp(Animation anim) {
        super(anim);
    }

    public Object clone() {
        Constructor constructor = getClass().getConstructors()[0];
        try {
            return constructor.newInstance(
                new Object[] {(Animation)anim.clone()});
        }
        catch (Exception ex) {
           ex.printStackTrace();
            return null;
        }
    }


    public static class Star extends PowerUp {
        public Star(Animation anim) {
            super(anim);
        }
    }

  
    public static class Music extends PowerUp {
        public Music(Animation anim) {
            super(anim);
        }
    }

    public static class Goal extends PowerUp {
        public Goal(Animation anim) {
            super(anim);
        }
    }
}
