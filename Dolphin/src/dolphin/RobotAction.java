/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dolphin;

import java.awt.*;
import java.io.*;

public interface RobotAction extends Serializable {

    Object execute(Robot robot) throws IOException;
}
