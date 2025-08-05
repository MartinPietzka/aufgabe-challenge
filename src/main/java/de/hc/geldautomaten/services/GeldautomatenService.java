package de.hc.geldautomaten.services;

import de.hc.geldautomaten.entities.Geldautomat;
import de.hc.geldautomaten.entities.Geldkarte;
import de.hc.geldautomaten.records.GeldautomatSession;
import de.hc.geldautomaten.records.Location;
import de.hc.geldautomaten.repositories.Repository;

import java.math.BigDecimal;
import java.util.Optional;

public interface GeldautomatenService {

    static GeldautomatenService create(Repository repository) {
        return new GeldautomatenServiceImpl(repository);
    }

    long create(Location location, BigDecimal verfuegbaresBargeld);

    Optional<Geldautomat> findByLocation(Location location);

    void aufladen(GeldautomatSession session, BigDecimal betrag);

    BigDecimal ermittleKontostand(GeldautomatSession session);

    BigDecimal abheben(GeldautomatSession session, BigDecimal betrag);

    GeldautomatSession login(Geldautomat geldautomat, Geldkarte geldkarte, String pin);

}
