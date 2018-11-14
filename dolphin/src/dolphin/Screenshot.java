package dolphin;


import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;

public class Screenshot implements RobotAction {

    public Object execute(Robot robot) throws IOException {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Rectangle shotArea = new Rectangle(defaultToolkit.getScreenSize());
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ImageIO.write(robot.createScreenCapture(shotArea), "jpg", bout);
        
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bout);;
//        encoder.encode(robot.createScreenCapture(shotArea));
//        
        return bout.toByteArray();  
    }

    public String toString() {
        return "Screenshot";
    }
}
