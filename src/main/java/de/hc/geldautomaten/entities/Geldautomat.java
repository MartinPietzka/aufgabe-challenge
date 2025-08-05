package de.hc.geldautomaten.entities;

import de.hc.geldautomaten.records.Location;

import java.math.BigDecimal;

public interface Geldautomat {

    long getNummer();

    Location getLocation();

    BigDecimal ermittleVerfuegbaresBargeld();

    void aufladen(BigDecimal betrag);

    void abheben(BigDecimal betrag);
}
