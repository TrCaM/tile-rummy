package project.rummy.Observer;

import project.rummy.control.ActionHandler;
import project.rummy.control.Player;
import project.rummy.entities.Table;

import java.util.Observable;
import java.util.Scanner;

public class TableSource extends Observable implements  Runnable {
    public void run(){
        while (true) {
            String respose = new Scanner(System.in).next();
            setChanged();
            notifyObservers(respose);
        }
    }
}
