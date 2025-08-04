package de.hc.geldautomaten;

import java.math.BigDecimal;

public interface Geldautomat {

    long getNummer();

    Location getLocation();

    BigDecimal ermittleVerfuegbaresBargeld();

    void aufladen(BigDecimal betrag);

    void abheben(BigDecimal betrag);
}
