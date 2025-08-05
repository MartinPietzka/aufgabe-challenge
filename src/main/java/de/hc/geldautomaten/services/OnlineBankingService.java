package de.hc.geldautomaten.services;

import de.hc.geldautomaten.records.OnlineBankingSession;
import de.hc.geldautomaten.repositories.Repository;

import java.math.BigDecimal;

public interface OnlineBankingService {
    static OnlineBankingService create(Repository repository) {
        return null;
    }

    OnlineBankingSession login(long kontonummer, long kundennummer, String pin);

    BigDecimal ermittleKontostand(OnlineBankingSession session);

    void ueberweisen(OnlineBankingSession session, long kontonummerEmpfaenger, BigDecimal betrag);
}
