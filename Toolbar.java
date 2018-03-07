
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Max on 2016-03-10.
 */
public class Toolbar extends JPanel implements Observer {
    private ImageCollectionModel icm;
    private JFrame frame;
    private ImageIcon gridicon = new ImageIcon(getClass().getResource("icon/1.png"));
    private ImageIcon listicon = new ImageIcon(getClass().getResource("icon/2.png"));
    private ImageIcon loadicon = new ImageIcon(getClass().getResource("icon/3.png"));
    private ImageIcon starGray = new ImageIcon(getClass().getResource("icon/4.png"));
    private ImageIcon starYellow = new ImageIcon(getClass().getResource("icon/5.png"));
    private JButton grid = new JButton(gridicon);
    private JButton list = new JButton(listicon);
    private JButton load = new JButton(loadicon);
    private JButton showall = new JButton(new String("Show All"));
    private Star star1;
    private Star star2;
    private Star star3;
    private Star star4;
    private Star star5;
    private ArrayList<Star> stars;
    private JFileChooser fc;

    // constructor
    Toolbar(ImageCollectionModel icm_, JFrame frame_) {
        icm = icm_;
        frame = frame_;
        stars = new ArrayList<Star>();

        this.setLayout(null);

        grid.setBackground(Color.GRAY);
        grid.setBounds(10, 7, 45, 45);
        list.setBackground(Color.GRAY);
        list.setBounds(60, 7, 45, 45);
        load.setBackground(Color.GRAY);
        load.setBorderPainted(false);
        showall.setBackground(Color.GRAY);
        showall.setFont(new Font("Comic Sans MS", Font.PLAIN, 8));
        showall.setForeground(Color.WHITE);
        star1 = new Star(1);
        star2 = new Star(2);
        star3 = new Star(3);
        star4 = new Star(4);
        star5 = new Star(5);
        stars.add(star1);
        stars.add(star2);
        stars.add(star3);
        stars.add(star4);
        stars.add(star5);

        fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                String extension = getExtension(f);
                if (extension != null) {
                    if (extension.equals("gif") ||
                            extension.equals("jpeg") ||
                            extension.equals("jpg") ||
                            extension.equals("png")) {
                        return true;
                    } else {
                        return false;
                    }
                }
                return false;
            }
            @Override
            public String getDescription() {
                return "images";
            }
        });

        load.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == load) {
                    int returnVal = fc.showOpenDialog(Toolbar.this);

                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File[] files = fc.getSelectedFiles();

                        // make imageModel then add to icm
                        for (File file : files) {
                            String path = file.getPath();
                            boolean exist = false;

                            // check for existing image
                            for (int i=0; i<icm.imageModelArrayList.size(); ++i) {
                                if (Objects.equals(icm.imageModelArrayList.get(i).imagePath, path)) {
                                    exist = true;
                                    break;
                                }
                            }

                            if (!exist) {
                                icm.addImage(new ImageModel(icm, file.getPath(), file.lastModified(), file.getName(), icm.gridLayout, file.length()));
                            } else {
                                JOptionPane.showMessageDialog(frame, "\"" + path + "\"" + " exists already.");
                            }
                        }
                        icm.update();
                    }
                    // reset file chooser
                    fc.setSelectedFile(null);
                }
            }
        });

        showall.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (icm.rateSort != 0) {
                    icm.rateSort = 0;
                    icm.changed = true;
                    icm.update();
                }
            }
        });

        grid.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!icm.gridLayout) {
                    icm.layoutChange(true);
                }
            }
        });

        list.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (icm.gridLayout) {
                    icm.layoutChange(false);
                }
            }
        });

        this.add(grid);
        this.add(list);
        this.add(load);
        this.add(showall);
        this.add(star1);
        this.add(star2);
        this.add(star3);
        this.add(star4);
        this.add(star5);
    }

    private class Star extends JComponent {
        int num;
        Image image;

        Star(int num_) {
            num = num_;
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    icm.rateSort = num;
                    icm.changed = true;
                    for (Star star : stars) {
                        star.repaint();
                    }
                    icm.update();
                }
            });
        }

        public void paintComponent(Graphics g) {
            if (icm.rateSort < num) {
                image = starGray.getImage();
            } else {
                image = starYellow.getImage();
            }
            g.drawImage(image, 0, 0, null);
        }
    }

    public String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.GRAY);
        g2.fillRect(0, 0, frame.getWidth(), frame.getHeight());

        Font font = new Font("Comic Sans MS", Font.BOLD, 35);
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        g2.drawString("Fotag!", 150, 40);

        font = new Font("Comic Sans MS", Font.PLAIN, 15);
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        g2.drawString("Filter by:", frame.getWidth()-220, 35);

        showall.setBounds(frame.getWidth()-120, 44, 68, 15);
        load.setBounds(frame.getWidth()-280, 5, 50, 50);
        star1.setBounds(frame.getWidth()-140, 17, 22, 22);
        star2.setBounds(frame.getWidth()-118, 17, 22, 22);
        star3.setBounds(frame.getWidth()-96, 17, 22, 22);
        star4.setBounds(frame.getWidth()-74, 17, 22, 22);
        star5.setBounds(frame.getWidth()-52, 17, 22, 22);
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}
