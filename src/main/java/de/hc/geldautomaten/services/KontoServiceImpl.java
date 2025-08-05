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
        return repository.findBankkontoByKontonummer(kontonummer);
    }

    @Override
    public long eroeffneKonto(String vorname, String nachname, String pin) {
        repository.beginTransaction();
        try {
            Bankkonto konto = repository.createBankkonto(vorname, nachname, pin);
            repository.commitTransaction();
            return konto.getKontonummer();
        } catch (Exception e) {
            repository.rollbackTransaction();
            throw new RuntimeException("Kontoeröffnung fehlgeschlagen für " + vorname + " " + nachname, e);
        }
    }


    @Override
    public Kontoauszug erstelleKontoauszug(Session session, LocalDate start, LocalDate inklusivesEnde) {
        return null;
    }
}
