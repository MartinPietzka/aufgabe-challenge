package de.hc.geldautomaten.services;

import de.hc.geldautomaten.entities.Geldautomat;
import de.hc.geldautomaten.entities.Geldkarte;
import de.hc.geldautomaten.records.GeldautomatSession;
import de.hc.geldautomaten.records.Location;
import de.hc.geldautomaten.repositories.Repository;

import java.math.BigDecimal;
import java.util.Optional;

public class GeldautomatenServiceImpl implements GeldautomatenService {
    private final Repository repository;

    public GeldautomatenServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public long create(Location location, BigDecimal verfuegbaresBargeld) {
        repository.beginTransaction();
        try {
            Geldautomat erstellterAutomat = repository.createGeldautomat(location, verfuegbaresBargeld);
            repository.commitTransaction();
            return erstellterAutomat.getNummer();
        } catch (Exception e) {
            repository.rollbackTransaction();
            throw new RuntimeException("Geldautomat konnte nicht erstellt werden.",e);
        }
    }

    @Override
    public Optional<Geldautomat> findByLocation(Location location) {
        return Optional.empty();
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
