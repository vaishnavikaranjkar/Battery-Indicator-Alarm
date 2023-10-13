import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.System.exit;

public class Main{
    static JFrame frame=new JFrame("Battery Alarm");
    //public void f()
    public static void PlayMusic(String location)
    {
        try
        {
            File alarmPath=new File(location);
            if(alarmPath.exists())
            {
                AudioInputStream audioInput= AudioSystem.getAudioInputStream(alarmPath);
                Clip clip=AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            }
            else
            {
                System.out.println("Cant find file");
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }
    public static void guiDisplay()
    {
        JLabel l1=new JLabel("The battery alarm is active",SwingConstants.CENTER);
        JLabel l2 =new JLabel("Battery percentage is:",SwingConstants.CENTER);
        JLabel l3=new JLabel("The alarm will ring, when battery percentage hits 100%", SwingConstants.CENTER);
        JButton b1 = new JButton("Quit");

        l1.setForeground(Color.green);
        l1.setPreferredSize(new Dimension(150,100));
        l1.setBounds(110,60,250,40);

        l2.setPreferredSize(new Dimension(150,100));
        l2.setForeground(Color.blue);
        l2.setBounds(110,140,250,40);

        l3.setBounds(60,100,400,40);

        b1.setBounds(190,230,80,30);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        frame.add(l1);
        frame.add(l3);
        frame.add(l2);
        frame.add(b1);
        frame.setSize(500,350);
        frame.setLayout(null);
        frame.setVisible(true);

    }
    public static void main(String[] args) {
        String alarmAudioFile="alarm.wav";
        Kernel32.SYSTEM_POWER_STATUS batteryStatus = new Kernel32.SYSTEM_POWER_STATUS();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        guiDisplay();
        boolean flag;
        JLabel l4=new JLabel();
        l4.setPreferredSize(new Dimension(150,100));
        l4.setBounds(220,180,150,40);
        frame.add(l4);
        Runnable checkBattery = new Runnable(){
            public void run() {
                Kernel32.INSTANCE.GetSystemPowerStatus(batteryStatus);
                System.out.println(batteryStatus); // Shows result of toString() method

                if(batteryStatus.BatteryLifePercent==100)
                {
                        PlayMusic(alarmAudioFile); 
                        JOptionPane.showMessageDialog(null,"Press OK when you unplug the charger");
                        System.exit(0);
                }
                l4.setText(batteryStatus.getBatteryLifePercent());
            }
        };
        executor.scheduleAtFixedRate(checkBattery, 0, 3, TimeUnit.SECONDS);
    }
}

