package utils;

import org.lwjgl.glfw.GLFW;
 
public class SyncTimer {
 
    final static double GLFW_RESOLUTION = 1.0D;
     
    public final static int LWJGL_GLFW = 2;
     
    private double timeThen;
    private boolean enabled = true;
     
    public SyncTimer() {

    }
         
    private double getResolution() {
        return GLFW_RESOLUTION;
    }
     
    private double getTime() {
        return GLFW.glfwGetTime();
    }
     
     
    public void setEnabled(boolean enable) { enabled = enable;}
     
    public boolean isEnabled() { return enabled; }
     
    public int sync(double fps) throws Exception {
        double resolution = getResolution();
        double timeNow =  getTime();
        int updates = 0;
         
        if (enabled) {
            double gapTo = resolution / fps + timeThen;
             
            while (gapTo < timeNow) {
                gapTo = resolution / fps + gapTo;
                updates++;
            }
            while (gapTo > timeNow) {
                Thread.sleep(1);
                timeNow = getTime();
            }
            updates++;
 
            timeThen = gapTo;
        } else {
            while (timeThen < timeNow) {
                timeThen = resolution / fps + timeThen;
                updates++;
            }
        }
         
        return updates;
    }
}
