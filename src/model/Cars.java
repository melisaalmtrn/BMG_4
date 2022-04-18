package model;

import app.observer.Observable;

import java.math.BigDecimal;

public class Cars extends Observable {

    private BigDecimal kilometre;
    private BigDecimal maxKilometre;


    public Cars(BigDecimal maxKilometre) {
        this.maxKilometre = maxKilometre;
        this.kilometre = BigDecimal.valueOf(70);
    }

    public BigDecimal getKilometre() {
        return kilometre;
    }

    public void setKilometre(BigDecimal kilometre) {
        this.kilometre = kilometre;

        System.out.println(kilometre);

        kilometreControl();
    }

    private void kilometreControl() {

        boolean manyKilometre = kilometre.compareTo(maxKilometre) >= 0;

        if (manyKilometre) {
            inform();
        }
    }
}
