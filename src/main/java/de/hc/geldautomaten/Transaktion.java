package de.hc.geldautomaten;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Transaktion {

    LocalDateTime getZeitpunkt();

    BigDecimal getBetrag();


}
