import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Max on 2016-03-10.
 */
public class ImageCollectionView extends JPanel implements Observer{
    private ImageCollectionModel icm;
    private ArrayList<JPanel> jPanelArrayList;
    private int imagePerPanel;
    private int imageNum;

    // constructor
    ImageCollectionView(ImageCollectionModel icm_) {
        icm = icm_;
        imagePerPanel = 2;
        imageNum = 0;
        icm.frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                addImages();
            }
        });
    }

    protected void addImages() {
        int temp_imagePerPanel = icm.frame.getWidth()/400;
        int temp_imageNum = icm.imageNum;

        if (imagePerPanel != temp_imagePerPanel || imageNum != temp_imageNum || icm.changed) {
            imagePerPanel = temp_imagePerPanel;
            imageNum = temp_imageNum;
            icm.changed = false;

            this.removeAll();
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            if (icm.gridLayout) {
                jPanelArrayList = new ArrayList<JPanel>();

                int curPanelNum = 0;
                int curImageNum = 0;

                for (int i=0; i<imageNum; ++i) {
                    if (i == 0 || curImageNum == imagePerPanel) {
                        curImageNum = 0;
                        curPanelNum++;
                        if (i == 0) curPanelNum--;
                        JPanel jp = new JPanel();
                        jp.setLayout(null);
                        jp.setBackground(Color.DARK_GRAY);
                        jp.setPreferredSize(new Dimension(imagePerPanel*400, 300));
                        jPanelArrayList.add(jp);
                        this.add(jp);
                    }

                    if (icm.imageModelArrayList.get(i).userRate >= icm.rateSort) {
                        icm.imageModelArrayList.get(i).imageView.setBounds((50 * (curImageNum + 1)) + (curImageNum * 300), 30, 300, 300);
                        jPanelArrayList.get(curPanelNum).add(icm.imageModelArrayList.get(i).imageView);
                        curImageNum++;
                    }
                }
                this.revalidate();

            } else {
                for (int i=0; i<imageNum; ++i) {
                    if (icm.imageModelArrayList.get(i).userRate >= icm.rateSort) {
                        icm.imageModelArrayList.get(i).imageView.setPreferredSize(new Dimension(640, 240));
                        this.add(icm.imageModelArrayList.get(i).imageView);
                    }
                }
                icm.frame.repaint();
                this.revalidate();
            }
        }
    }

    protected void paintComponent(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void update(Observable o, Object arg) {
        repaint();
        addImages();
    }
}
