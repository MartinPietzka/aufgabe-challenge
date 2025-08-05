package de.hc.geldautomaten.entities;

public interface Kunde {

    String getVorname();

    String getNachname();

    long getKundennummer();

    Geldkarte getGeldkarte();
}
