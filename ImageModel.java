
import javax.swing.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;

/**
 * Created by Max on 2016-03-10.
 */
public class ImageModel extends Observable implements Serializable {
    ImageCollectionModel icm;
    transient ImageView imageView;
    String imagePath;
    String modifiedDate;
    String imageName;
    int userRate;
    long size;
    boolean gridLayout;

    // constructor
    ImageModel(ImageCollectionModel icm_, String path, long date, String name, boolean layout, long size_) {
        icm = icm_;
        gridLayout = layout;
        imagePath = path;
        imageName = name;
        userRate = 0;
        size = size_/1000;

        Date modDate = new Date(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(modDate);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        modifiedDate = month + "/" + day + "/" + year;

        imageView = new ImageView(this);
        this.addObserver(imageView);
        setChanged();
    }

    public void update() {
        setChanged();
        notifyObservers();
    }
}
