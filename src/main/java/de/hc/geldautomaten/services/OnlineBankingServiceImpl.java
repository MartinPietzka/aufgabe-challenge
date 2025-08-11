package de.hc.geldautomaten.services;

import de.hc.geldautomaten.records.OnlineBankingSession;
import de.hc.geldautomaten.repositories.Repository;

import java.math.BigDecimal;

public class OnlineBankingServiceImpl implements OnlineBankingService {
    private final Repository repository;

    public OnlineBankingServiceImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public OnlineBankingSession login(long kontonummer, long kundennummer, String pin) {
        return null;
    }

    @Override
    public BigDecimal ermittleKontostand(OnlineBankingSession session) {
        return null;
    }

    @Override
    public void ueberweisen(OnlineBankingSession session, long kontonummerEmpfaenger, BigDecimal betrag) {

    }
}
