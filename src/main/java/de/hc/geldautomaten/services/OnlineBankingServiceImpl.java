package de.hc.geldautomaten.services;

import de.hc.geldautomaten.entities.Bankkonto;
import de.hc.geldautomaten.records.OnlineBankingSession;
import de.hc.geldautomaten.repositories.Repository;

import java.math.BigDecimal;
import java.util.Optional;

public class OnlineBankingServiceImpl implements OnlineBankingService {
    private final Repository repository;

    public OnlineBankingServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public OnlineBankingSession login(long kontonummer, long kundennummer, String pin) {
        Optional<Bankkonto> bankkontoOptional = repository.findBankkontoByKontonummer(kontonummer);
        Bankkonto bankkonto = bankkontoOptional.orElseThrow();
        if (bankkonto.getKunde().getKundennummer() != kundennummer)
            throw new IllegalArgumentException("Kundennummer passt nicht zum Konto");
        if (!bankkonto.checkOnlineBankingPin(pin))
            throw new IllegalArgumentException("Pin Falsch");
        return new OnlineBankingSession(bankkonto);
    }

    @Override
    public BigDecimal ermittleKontostand(OnlineBankingSession session) {
        return null;
    }

    @Override
    public void ueberweisen(OnlineBankingSession session, long kontonummerEmpfaenger, BigDecimal betrag) {

    }
}
