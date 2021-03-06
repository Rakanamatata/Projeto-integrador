/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dolphin;

import java.awt.*;
import java.awt.event.MouseEvent;

public class MoveMouse implements RobotAction {

    private final int x;
    private final int y;

    public MoveMouse(Point to) {
        x = (int) to.getX();
        y = (int) to.getY();
    }

    public MoveMouse(MouseEvent event) {
        this(event.getPoint());
    }

    public Object execute(Robot robot) {
        robot.mouseMove(x, y);
        return null;
    }

    public String toString() {
        return "MoveMouse: x=" + x + ", y=" + y;
    }
}
