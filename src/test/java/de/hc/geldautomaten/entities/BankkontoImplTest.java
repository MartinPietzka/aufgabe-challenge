package de.hc.geldautomaten.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

class BankkontoImplTest {

    private Kunde dummyKunde;

    @BeforeEach
    void setUp() {
        Geldkarte dummyGeldkarte = new GeldkarteImpl(1L, 1L, "1234");
        dummyKunde = new KundeImpl(1L, "Hans", "Peter", dummyGeldkarte);
    }

    @Test
    void checkOnlineBankingPin_WennKorrektePinEingegebenWird_SollteTrueZurueckgeben() {
        Bankkonto bankkonto = new BankkontoImpl(1L, dummyKunde, "passwort321", BigDecimal.ZERO);

        boolean isPinCorrect = bankkonto.checkOnlineBankingPin("passwort321");

        assertThat(isPinCorrect).isTrue();
    }

    @Test
    void checkOnlineBankingPin_WennFalschePinEingegebenWird_SollteFalseZurueckgeben() {
        Bankkonto bankkonto = new BankkontoImpl(1L, dummyKunde, "passwort321", BigDecimal.ZERO);

        boolean isPinCorrect = bankkonto.checkOnlineBankingPin("xyz");

        assertThat(isPinCorrect).isFalse();
    }

    @Test
    void abheben_WennBetragAbgebuchtWird_SollteKontostandVerringern() {
        Bankkonto bankkonto = new BankkontoImpl(1L, dummyKunde, "pw123", new BigDecimal("200.00"));

        bankkonto.abheben(new BigDecimal("70.00"));

        assertThat(
                bankkonto.ermittleKontostand())
                .isEqualByComparingTo("130.00");
    }

    @Test
    void abheben_WennKontoNichtGedecktIst_SollteExceptionWerfen() {
        Bankkonto bankkonto = new BankkontoImpl(1L, dummyKunde, "pw123", new BigDecimal("100.00"));

        assertThatThrownBy(() -> {
            bankkonto.abheben(new BigDecimal("120.00"));
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Kontostand nicht ausreichend")
                .hasMessageContaining("100.00")
                .hasMessageContaining("120.00");
    }

    @Test
    void abheben_WennBetragNegativIst_SollteExceptionWerfen() {
        Bankkonto bankkonto = new BankkontoImpl(1L, dummyKunde, "pw123", new BigDecimal("200.00"));

        assertThatThrownBy(() -> {
            bankkonto.abheben(new BigDecimal("-50.00"));
        })
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void aufladen_WennBetragPositivIst_SollteKontostandErhoehen() {
        Bankkonto bankkonto = new BankkontoImpl(1L, dummyKunde, "pw123", new BigDecimal("500.00"));

        bankkonto.aufladen(new BigDecimal("150.00"));

        assertThat(bankkonto.ermittleKontostand())
                .isEqualByComparingTo("650.00");
    }

    @Test
    void aufladen_WennBetragNegativIst_SollteExceptionWerfen() {
        Bankkonto bankkonto = new BankkontoImpl(1L, dummyKunde, "pw123", new BigDecimal("500.00"));

        assertThatThrownBy(() -> {
            bankkonto.aufladen(new BigDecimal("-100.00"));
        })
                .isInstanceOf(IllegalArgumentException.class);
    }
}