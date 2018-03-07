import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Max on 2016-03-10.
 */
public class ImageView extends JPanel implements Observer {
    private ImageModel im;
    private BufferedImage image;
    private Image scaledInst;
    private int scaledWidth = 0;
    private int scaledHeight = 0;
    private int imageWidth;
    private int imageHeight;
    private ImageIcon starGray = new ImageIcon(getClass().getResource("icon/4.png"));
    private ImageIcon starYellow = new ImageIcon(getClass().getResource("icon/5.png"));
    private ImageIcon reseticon = new ImageIcon(getClass().getResource("icon/6.png"));
    private ImageIcon deleteicon = new ImageIcon(getClass().getResource("icon/7.png"));
    private JButton reset = new JButton(reseticon);
    private JButton delete = new JButton(deleteicon);
    private JButton zoom;
    private StarOnImage star1;
    private StarOnImage star2;
    private StarOnImage star3;
    private StarOnImage star4;
    private StarOnImage star5;
    private ArrayList<StarOnImage> stars;

    // constructor
    ImageView(ImageModel im_) {
        im = im_;
        stars = new ArrayList<StarOnImage>();
        try {
            image = ImageIO.read(new File(im.imagePath));
        } catch (IOException e) {
            Image non = deleteicon.getImage();
            image = new BufferedImage(non.getWidth(null), non.getHeight(null), BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr = image.createGraphics();
            bGr.drawImage(non, 0, 0, null);
            bGr.dispose();
        }

        imageWidth = image.getWidth();
        imageHeight = image.getHeight();

        if (imageHeight > 200) {
            scaledHeight = 200;
            scaledWidth = (int) (((double) 200 / (double) imageHeight) * (double) imageWidth);
            if (scaledWidth > 300) {
                int modWidth = scaledWidth;
                scaledWidth = 300;
                scaledHeight = (int) (((double) 300 / (double) modWidth) * (double) scaledHeight);
            }
        } else if (imageHeight <= 200 && imageWidth > 300) {
            scaledWidth = 300;
            scaledHeight = (int) (((double) 300 / (double) imageWidth) * (double) imageHeight);
        } else {
            scaledWidth = imageWidth;
            scaledHeight = imageHeight;
        }
        scaledInst = image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

        reset.setBackground(Color.DARK_GRAY);
        reset.setSize(20, 20);
        reset.setBorderPainted(false);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (im.userRate != 0) {
                    im.userRate = 0;
                    for (StarOnImage star : stars) {
                        star.repaint();
                    }
                }
            }
        });

        delete.setSize(20, 20);
        delete.setVisible(false);
        delete.setOpaque(false);
        delete.setContentAreaFilled(false);
        delete.setBorderPainted(false);
        delete.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                delete.setVisible(true);
                ArrayList<ImageModel> ima = im.icm.imageModelArrayList;
                for (int i=0; i<ima.size(); ++i) {
                    if (ima.get(i) == im) {
                        ima.remove(i);
                        im.icm.imageNum--;
                        im.icm.changed = true;
                        im.icm.update();
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                delete.setVisible(true);
            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                delete.setVisible(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                delete.setVisible(false);
            }
        });

        zoom = new JButton();
        zoom.setOpaque(false);
        zoom.setContentAreaFilled(false);
        zoom.setBorderPainted(false);
        zoom.addMouseListener(new MouseAdapter() {
            boolean entered;
            @Override
            public void mouseReleased(MouseEvent e) {
                delete.setVisible(true);
                if (entered) {
                    JFrame frame = new JFrame(im.imagePath);
                    frame.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                                frame.dispose();
                            }
                        }
                    });
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    frame.setBackground(Color.LIGHT_GRAY);
                    frame.setSize(new Dimension(720, 540));

                    JPanel p = new JPanel() {
                        int zoomWidth, zoomHeight;

                        @Override
                        public void paintComponent(Graphics g) {
                            int contentWidth = frame.getContentPane().getWidth();
                            int contentHeight = frame.getContentPane().getHeight();
                            if (imageHeight > contentHeight) {
                                zoomHeight = contentHeight;
                                zoomWidth = (int) (((double) contentHeight / (double) imageHeight) * (double) imageWidth);
                                if (zoomWidth > contentWidth) {
                                    int modWidth = zoomWidth;
                                    zoomWidth = contentWidth;
                                    zoomHeight = (int) (((double) contentWidth / (double) modWidth) * (double) zoomHeight);
                                }
                            } else if (imageHeight <= contentHeight && imageWidth > contentWidth) {
                                zoomWidth = contentWidth;
                                zoomHeight = (int) (((double) contentWidth / (double) imageWidth) * (double) imageHeight);
                            } else {
                                zoomWidth = imageWidth;
                                zoomHeight = imageHeight;
                            }
                            Image zoomInst = image.getScaledInstance(zoomWidth, zoomHeight, Image.SCALE_SMOOTH);
                            g.drawImage(zoomInst, (getWidth() - zoomWidth) / 2, (getHeight() - zoomHeight) / 2, null);
                        }
                    };
                    frame.add(p);
                    frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                delete.setVisible(true);
                entered = true;
            }
            @Override
            public void mouseExited(MouseEvent e) {
                delete.setVisible(false);
                entered = false;
            }
        });

        star1 = new StarOnImage(1);
        star2 = new StarOnImage(2);
        star3 = new StarOnImage(3);
        star4 = new StarOnImage(4);
        star5 = new StarOnImage(5);
        stars.add(star1);
        stars.add(star2);
        stars.add(star3);
        stars.add(star4);
        stars.add(star5);

        this.add(star1);
        this.add(star2);
        this.add(star3);
        this.add(star4);
        this.add(star5);
        this.add(reset);
        this.add(delete);
        this.add(zoom);
    }

    private class StarOnImage extends JComponent {
        int num;
        Image image;

        StarOnImage(int num_) {
            num = num_;
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (im.userRate != num) {
                        im.userRate = num;
                        for (StarOnImage star : stars) {
                            star.repaint();
                        }
                    }
                }
            });
        }

        public void paintComponent(Graphics g) {
            if (im.userRate < num) {
                image = starGray.getImage();
            } else {
                image = starYellow.getImage();
            }
            g.drawImage(image, 0, 0, null);
        }
    }

    protected void paintComponent(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        Font font = new Font("Comic Sans MS", Font.BOLD, 14);

        if (im.gridLayout) {
            g.fillRect(0, 0, 400, 300);
            g.drawImage(scaledInst, (300 - scaledWidth) / 2, (200 - scaledHeight) / 2, this);
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString(im.imageName, 0, 215);
            g.drawString(im.modifiedDate, 0, 238);
            star1.setBounds(190, 221, 22, 22);
            star2.setBounds(212, 221, 22, 22);
            star3.setBounds(234, 221, 22, 22);
            star4.setBounds(256, 221, 22, 22);
            star5.setBounds(278, 221, 22, 22);
            reset.setBounds(164, 223, 20, 20);
            delete.setBounds(280, 0, 20, 20);
            zoom.setBounds(0, 0, 300, 200);

        } else {
            g.fillRect(0, 0, getWidth(), 240);
            g.drawImage(scaledInst, 40+((300 - scaledWidth)/2), 20+((200 - scaledHeight)/2), this);
            g.setFont(font);
            g.setColor(Color.WHITE);
            g.drawString("File name:", 360, 64);
            g.drawString("Modified date:", 360, 88);
            g.drawString("Image size:", 360, 112);
            g.drawString("File size:", 360, 136);
            g.drawString(im.imageName, 480, 64);
            g.drawString(im.modifiedDate, 480, 88);
            g.drawString(imageWidth + " x " + imageHeight, 480, 112);
            g.drawString(im.size + " KB", 480, 136);
            star1.setBounds(360, 22, 22, 22);
            star2.setBounds(382, 22, 22, 22);
            star3.setBounds(404, 22, 22, 22);
            star4.setBounds(426, 22, 22, 22);
            star5.setBounds(448, 22, 22, 22);
            reset.setBounds(472, 24, 20, 20);
            delete.setBounds(getWidth()-60, 20, 20, 20);
            zoom.setBounds(40, 20, 300, 200);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
    }
}
