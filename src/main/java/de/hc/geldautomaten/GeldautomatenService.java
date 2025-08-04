package de.hc.geldautomaten;

import java.math.BigDecimal;
import java.util.Optional;

public interface GeldautomatenService {

    static GeldautomatenService create(Repository repository) {
        return null;
    }

    long create(Location location, BigDecimal verfuegbaresBargeld);

    Optional<Geldautomat> findByLocation(Location location);

    void aufladen(GeldautomatSession session, BigDecimal betrag);

    BigDecimal ermittleKontostand(GeldautomatSession session);

    BigDecimal abheben(GeldautomatSession session, BigDecimal betrag);

    GeldautomatSession login(Geldautomat geldautomat, Geldkarte geldkarte, String pin);

}
