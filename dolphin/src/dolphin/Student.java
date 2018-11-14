/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dolphin;

import java.awt.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Student {

    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private final Robot robot;

    public Student(String serverMachine, String studentName) throws IOException, AWTException {
        Socket socket = new Socket(InetAddress.getLocalHost(), 5555);
        robot = new Robot();
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        out.writeObject(studentName);
        out.flush();
    }

    public void run() throws ClassNotFoundException {
        try {
            while (true) {
                RobotAction action = (RobotAction) in.readObject();
                Object result = action.execute(robot);
                if (result != null) {
                    out.writeObject(result);
                    out.flush();
                    out.reset();
                }
            }
        } catch (IOException ex) {
            System.out.println("Connection closed");
        }
    }

    public static void main(String[] args) throws Exception {
        Student student = new Student("aaaa", "aaa");
        student.run();

    }
}
