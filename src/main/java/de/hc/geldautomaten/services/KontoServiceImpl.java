package de.hc.geldautomaten.services;

import de.hc.geldautomaten.Session;
import de.hc.geldautomaten.entities.Bankkonto;
import de.hc.geldautomaten.records.Kontoauszug;
import de.hc.geldautomaten.repositories.Repository;

import java.time.LocalDate;
import java.util.Optional;

public class KontoServiceImpl implements KontoService {
    private final Repository repository;

    public KontoServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Bankkonto> findByKontonummer(long kontonummer) {
        return Optional.empty();
    }

    @Override
    public long eroeffneKonto(String vorname, String nachname, String pin) {
        Bankkonto erstelltesKonto = repository.createBankkonto(vorname, nachname, pin);
        return erstelltesKonto.getKontonummer();
    }

    @Override
    public Kontoauszug erstelleKontoauszug(Session session, LocalDate start, LocalDate inklusivesEnde) {
        return null;
    }
}
