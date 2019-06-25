package yc.com.base;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by wanglin  on 2018/12/11 19:08.
 */
public class ObservableManager extends Observable {

    private static ObservableManager instance;

    public static ObservableManager get() {
        synchronized (ObservableManager.class) {
            if (instance == null) {
                synchronized (ObservableManager.class) {
                    instance = new ObservableManager();
                }
            }
        }
        return instance;
    }


    public void addMyObserver(Observer observer) {
        addObserver(observer);
    }

    public void notifyMyObserver(Object object) {
        setChanged();
        notifyObservers(object);

    }

    public void deleteMyObserver(Observer observer) {
        deleteObserver(observer);
    }

    public void deleteMyObservers() {
        deleteObservers();
    }

}
