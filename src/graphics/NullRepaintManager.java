/**
    Klasa NullRepaintManager dziedziczy po RepaintManager 
*/
public class NullRepaintManager extends RepaintManager {

    /**
         NullRepaintManager.
    */
    public static void install() {
        RepaintManager repaintManager = new NullRepaintManager();
        repaintManager.setDoubleBufferingEnabled(false);
        RepaintManager.setCurrentManager(repaintManager);
    }

}
