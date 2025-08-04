package de.hc.geldautomaten;

public interface Geldkarte {

    long getKartennummer();

    long getKontonummer();

    boolean checkPin(String pin);

}
