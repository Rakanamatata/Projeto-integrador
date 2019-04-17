/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dolphin;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.*;
import javax.imageio.ImageIO;

public class Teacher extends JFrame {

    public static final int PORT = 5555;
    private static final long SCREEN_SHOT_PERIOD = 200;
    private static final int WINDOW_HEIGHT = 400;
    private static final int WINDOW_WIDTH = 500;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String studentName;
    private JLabel iconLabel = new JLabel();
    private RobotActionQueue jobs = new RobotActionQueue();
    private Thread writer;
    private Timer timer;
    private volatile boolean running = true;

    public Teacher(Socket socket) throws IOException, ClassNotFoundException {
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        studentName = (String) in.readObject();
        setupUI();

        createReaderThread();
        timer = createScreenShotThread();
        writer = createWriterThread();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                timer.cancel();
            }
        });
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    out.close();
                } catch (IOException ex) {
                }
                try {
                    in.close();
                } catch (IOException ex) {
                }
            }
        });
        System.out.println("finished connecting to " + socket);
    }

    private Timer createScreenShotThread() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                jobs.add(new Screenshot());
            }
        }, 1, SCREEN_SHOT_PERIOD);
        return t;
    }

    private void setupUI() {
        setTitle("Screen from " + studentName);
        getContentPane().add(new JScrollPane(iconLabel));
        iconLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (running) {
                    jobs.add(new MoveMouse(e));
                    jobs.add(new ClickMouse(e));
                    jobs.add(new Screenshot());
                    System.out.println("Mouse clicked");
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
            @Override
            public void mouseMoved(MouseEvent e) {
                if (running) {
                    jobs.add(new MoveMouse(e));
                    System.out.println("Mouse moved");
                    // jobs.add(new ClickMouse(e));
                    // jobs.add(new Screenshot());
                } else {
                    // Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private Thread createWriterThread() {
        Thread writer = new Thread("Writer") {
            @Override
            public void run() {
                try {
                    while (true) {
                        RobotAction action = jobs.next();
                        out.writeObject(action);
                        out.flush();
                    }
                } catch (Exception e) {
                    System.out.println("Connection to " + studentName + " closed (" + e + ')');
                    setTitle(getTitle() + " - disconnected");
                }
            }
        };
        writer.start();
        return writer;
    }

    private void showIcon(byte[] byteImage) throws IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(byteImage);
        BufferedImage img = ImageIO.read(bin);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                iconLabel.setIcon(new ImageIcon(img));
            }
        });
    }

    private void createReaderThread() {
        Thread readThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        byte[] img = (byte[]) in.readObject();
                    //    System.out.println("Received screenshot of "
                    //            + img.length + " bytes from " + studentName);
                        showIcon(img);
                    } catch (Exception ex) {
                        System.out.println("Exception occurred: " + ex);
                        writer.interrupt();
                        timer.cancel();
                        running = false;
                        return;
                    }
                }
            }
        };
        readThread.start();
    }

    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(PORT);
        ArrayList<Teacher> tArr = new ArrayList<>();
        System.out.println(InetAddress.getLocalHost().toString());
        while (true) {
            Socket socket = ss.accept();
            System.out.println("Connection From " + socket);
            tArr.add(new Teacher(socket));
            System.out.println("hey");
            System.out.println(new Screenshot());
        }
    }
}
