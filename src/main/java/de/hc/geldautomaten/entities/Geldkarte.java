package de.hc.geldautomaten.entities;

public interface Geldkarte {

    long getKartennummer();

    long getKontonummer();

    boolean checkPin(String pin);

}
