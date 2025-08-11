package de.hc.geldautomaten.services;

import de.hc.geldautomaten.Bank;
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
        Bankkonto bankkonto = session.getBankkonto();
        return bankkonto.ermittleKontostand();
    }

    @Override
    public void ueberweisen(OnlineBankingSession session, long kontonummerEmpfaenger, BigDecimal betrag) {
        Bankkonto bankkonto = session.getBankkonto();
//        System.out.println(bankkonto);
        Optional<Bankkonto> bankkontoEmpfaengerOptional = repository.findBankkontoByKontonummer(kontonummerEmpfaenger);
        Bankkonto bankkontoEmpfaenger = bankkontoEmpfaengerOptional.orElseThrow();

        repository.beginTransaction();
        try {
            bankkontoEmpfaenger.aufladen(betrag);
//            repository.save(bankkontoEmpfaenger);
            bankkonto.abheben(betrag);
//            repository.save(bankkonto);

            repository.commitTransaction();
        } catch (Exception e) {
            repository.rollbackTransaction();
            throw new RuntimeException("Überweisung fehlgeschlagen", e);
        }
    }
}
