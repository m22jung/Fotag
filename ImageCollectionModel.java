import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Observable;

/**
 * Created by Max on 2016-03-10.
 */
public class ImageCollectionModel extends Observable implements Serializable {
    transient JFrame frame;
    ArrayList<ImageModel> imageModelArrayList;
    int imageNum;
    int rateSort;
    boolean gridLayout;
    boolean changed;

    // constructor
    ImageCollectionModel(JFrame frame_) {
        frame = frame_;
        imageModelArrayList = new ArrayList<ImageModel>();
        imageNum = 0;
        rateSort = 0;
        gridLayout = true;
        changed = false;
        setChanged();
    }

    public void addImage(ImageModel im) {
        imageModelArrayList.add(im);
        imageNum++;
    }

    public void layoutChange(boolean changeTo) {
        if (gridLayout != changeTo) {
            gridLayout = changeTo;
            for (ImageModel im : imageModelArrayList) {
                im.gridLayout = changeTo;
                im.update();
            }
            changed = true;
            update();
        }
    }

    public void update() {
        setChanged();
        notifyObservers();
    }
}
