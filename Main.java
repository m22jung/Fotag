import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Created by Max on 2016-03-10.
 */
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Fotag!");
        ImageCollectionModel icm = null;

        try {
            FileInputStream loadFile = new FileInputStream("fotag.sav");
            ObjectInputStream load = new ObjectInputStream(loadFile);
            icm = (ImageCollectionModel) load.readObject();
            load.close();
            loadFile.close();
            icm.frame = frame;
            for (ImageModel im : icm.imageModelArrayList) {
                ImageView iv = new ImageView(im);
                im.imageView = iv;
                im.addObserver(iv);
            }
        } catch (Exception e) {
            if (e instanceof FileNotFoundException) {
                icm = new ImageCollectionModel(frame);
            } else {
                e.printStackTrace();
            }
        }

        if (icm != null) {
            frame.setMinimumSize(new Dimension(640, 480));
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            Toolbar tb = new Toolbar(icm, frame);
            icm.addObserver(tb);
            ImageCollectionView icv = new ImageCollectionView(icm);
            icm.addObserver(icv);

            // let all the views know that they're connected to the model
            icm.notifyObservers();

            // create the window
            JPanel p = new JPanel();
            p.setLayout(new BorderLayout());

            JScrollPane scroller = new JScrollPane(icv,
                    JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                    JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroller.getVerticalScrollBar().setUnitIncrement(14);
            scroller.setBackground(Color.DARK_GRAY);

            tb.setPreferredSize(new Dimension(frame.getWidth(), 60));
            p.add(tb, BorderLayout.NORTH);
            p.add(scroller, BorderLayout.CENTER);

            frame.getContentPane().add(p);
            frame.setVisible(true);

            final ImageCollectionModel finalIcm = icm;
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                public void run() {
                    try {
                        FileOutputStream saveFile = new FileOutputStream("fotag.sav");
                        ObjectOutputStream save = new ObjectOutputStream(saveFile);
                        save.writeObject(finalIcm);
                        save.close();
                        saveFile.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }));
        } else {
            JFrame f = new JFrame();
            JOptionPane.showMessageDialog(f, "\"Fotag.sav\" is corrupted. Delete this file and retry");
            System.exit(0);
        }
    }
}
