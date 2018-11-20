package dolphin;


import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import DirectRobot;

public class Screenshot implements RobotAction {


    public Object execute(Robot robot) throws IOException {
        ColorModel model = new DirectColorModel(32, 0xff0000, 0xff00, 0xff, 0xff000000);
        BufferedImage image = new BufferedImage(model, Raster.createWritableRaster(model.createCompatibleSampleModel(width, height), new DataBufferInt(pixels, width * height), null), false, new Hashtable<Object, Object>());
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", bout);

        // @deprecated
//        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(bout);
//        encoder.encode(robot.createScreenCapture(shotArea));
//        
        return bout.toByteArray();  
    }

    public String toString() {
        return "Screenshot";
    }
}
