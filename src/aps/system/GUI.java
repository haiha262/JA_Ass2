package aps.system;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.BorderLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.Timer;

/**
 *
 * @author M
 */
public class GUI extends JFrame implements ActionListener {

    public static final int DEFAULT_WIDTH = SystemSupport.DEFAULT_WIDTH;
    public static final int DEFAULT_HEIGHT = SystemSupport.DEFAULT_HEIGHT;

    private JPanel buttonPanel;

    private JButton btnPlay;
    private JButton btnPause;
    private JButton btnFoward2X;

    private JButton btnPark;
    private JButton btnCollect;

    static final int SPEED_DEFAULT = 100;
    private int speed = SPEED_DEFAULT;
    private Timer timer;
    private PaintSystem canvas;
    private paintFloor floor;

    private Thread aThread;
    private boolean useThread = true;

    public GUI() {
        setTitle("Assignment 1");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // add the sample text label
//        label = new JLabel("The quick brown fox jumps over the lazy dog.");
//        label.setFont(new Font("Serif", Font.PLAIN, DEFAULT_SIZE));
//        add(label, BorderLayout.CENTER);
        // add the radio buttons
        canvas = new PaintSystem(true);

        buttonPanel = new JPanel();

        btnPlay = new JButton("Play");
        btnPause = new JButton("Pause");
        btnFoward2X = new JButton("Foward 2X");
        btnPark = new JButton("Park Car");
        btnCollect = new JButton("Collect Car");
        btnPlay.addActionListener(this);
        btnPause.addActionListener(this);
        btnFoward2X.addActionListener(this);
        btnPark.addActionListener(this);
        btnCollect.addActionListener(this);

        buttonPanel.add(btnPlay);
        buttonPanel.add(btnPause);
        buttonPanel.add(btnFoward2X);
        buttonPanel.add(btnPark);
        buttonPanel.add(btnCollect);

        add(buttonPanel, BorderLayout.SOUTH);

        //add panes
        JTabbedPane tabbedPane = new JTabbedPane();

//        panel= new JPanel();
        //panel.add(new Label("Ground"));
        // panel.add(canvas, BorderLayout.CENTER);
        tabbedPane.addTab("Ground", canvas);

        for (int i = 0; i < SystemSupport.NF; i++) {
            floor = new paintFloor(i);
            tabbedPane.addTab("Floor " + (i + 1), floor);
        }
        add(tabbedPane, BorderLayout.CENTER);

        if (useThread) {
            aThread = new Thread(canvas);
            aThread.start();
        } else {
            timer = new Timer(speed, new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    repaint();
                    //canvas.repaint();
                    //floor1.repaint();
                }
            });
            timer.start();
        }

    }

    public void actionPerformed(ActionEvent e) {
        if (!useThread) {
            repaint();
        }
        if (e.getSource() == btnPlay) {
            speed = SPEED_DEFAULT;

            if (!useThread) {
                timer.start();
            } else {
                aThread.resume();
            }
        } else {
            if (e.getSource() == btnPause) {

                if (!useThread) {
                    timer.stop();
                } else {
                    aThread.suspend();
                }
            } else {
                if (e.getSource() == btnFoward2X) {
                    speed = SPEED_DEFAULT / 6;
                    if (!useThread) {
                        timer.start();
                    }
                } else {
                    if (e.getSource() == btnPark) {
                        if (canvas.isProgess()) {
                            JOptionPane.showMessageDialog(this, "Wait unitl the progess finihes ");
                        } else if (canvas.canParking()) {
                            canvas.changeState(Statement.STATE_ENTER_INIT);

                        } else {
                            JOptionPane.showMessageDialog(this, "Not enough space to park");
                        }
                    } else if (e.getSource() == btnCollect) {
                        if (canvas.isProgess()) {
                            JOptionPane.showMessageDialog(this, "Wait unitl the progess finihes ");
                        } else {
                            String id = JOptionPane.showInputDialog(this, "Enter car id");
                            boolean canRelease = canvas.setFindID(Integer.parseInt(id));
                            if (canRelease) {
                                canvas.changeState(Statement.STATE_RELEASE_CAR_INIT);

                            } else {
                                JOptionPane.showMessageDialog(this, "Can't found that car id ");
                            }
                        }
                    }
                }
            }
        }
        if (!useThread) {
            timer.setDelay(speed);
        } else {
            SystemSupport.DelayTime(500);
        }
    }
}
