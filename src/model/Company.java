package model;

import app.observer.Observable;
import app.observer.Observer;

public class Company implements Observer {

    private String name;

    public Company(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable observable) {

        Cars car = (Cars) observable;

        System.out.println(name + " kilometre " + car.getKilometre() + " 'de !!!");
    }
}
