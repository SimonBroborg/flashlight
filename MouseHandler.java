import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseHandler extends MouseAdapter {

    private flashlight2 fl;
    public MouseHandler(flashlight2 fl){
        this.fl = fl;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        fl.setTargetX(e.getX());
        fl.setTargetY(e.getY());
    }
}
