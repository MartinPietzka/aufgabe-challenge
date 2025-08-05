package de.hc.geldautomaten.records;

import de.hc.geldautomaten.entities.Transaktion;

import java.math.BigDecimal;
import java.util.List;

public record Kontoauszug(
        BigDecimal startkontostand,
        BigDecimal endkontostand,
        List<Transaktion> transaktionen) {
}
