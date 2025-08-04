package de.hc.geldautomaten;

import java.math.BigDecimal;

public interface OnlineBankingService {
    static OnlineBankingService create(Repository repository) {
        return null;
    }

    OnlineBankingSession login(long kontonummer, long kundennummer, String pin);

    BigDecimal ermittleKontostand(OnlineBankingSession session);

    void ueberweisen(OnlineBankingSession session, long kontonummerEmpfaenger, BigDecimal betrag);
}
