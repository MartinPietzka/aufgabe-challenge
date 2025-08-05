package de.hc.geldautomaten.services;

import de.hc.geldautomaten.entities.Geldautomat;
import de.hc.geldautomaten.entities.Geldkarte;
import de.hc.geldautomaten.records.GeldautomatSession;
import de.hc.geldautomaten.records.Location;
import de.hc.geldautomaten.repositories.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;

public class GeldautomatenServiceImpl implements GeldautomatenService {
    private static final Logger logger = LoggerFactory.getLogger(GeldautomatenServiceImpl.class);
    private final Repository repository;

    public GeldautomatenServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public long create(Location location, BigDecimal verfuegbaresBargeld) {
        logger.debug("Erstelle Geldautomaten am Ort {}.", location);
        repository.beginTransaction();
        try {
            Geldautomat erstellterAutomat = repository.createGeldautomat(location, verfuegbaresBargeld);
            repository.commitTransaction();
            logger.info("Geldautomat mit Nummer {} erfolgreich erstellt.", erstellterAutomat.getNummer());
            return erstellterAutomat.getNummer();
        } catch (Exception e) {
            repository.rollbackTransaction();
            logger.error("Fehler bei der Erstellung des Geldautomaten am Ort {}.", location, e);
            throw new RuntimeException("Geldautomat konnte nicht erstellt werden.", e);
        }
    }

    @Override
    public Optional<Geldautomat> findByLocation(Location location) {
        return repository.findGeldautomatByLocation(location);
    }

    @Override
    public void aufladen(GeldautomatSession session, BigDecimal betrag) {

    }

    @Override
    public BigDecimal ermittleKontostand(GeldautomatSession session) {
        return null;
    }

    @Override
    public BigDecimal abheben(GeldautomatSession session, BigDecimal betrag) {
        return null;
    }

    @Override
    public GeldautomatSession login(Geldautomat geldautomat, Geldkarte geldkarte, String pin) {
        return null;
    }
}
