package de.hc.geldautomaten;

import de.hc.geldautomaten.Geldkarte;

public interface Kunde {

    String getVorname();

    String getNachname();

    long getKundennummer();

    Geldkarte getGeldkarte();
}
