package de.hc.geldautomaten;

import java.math.BigDecimal;
import java.util.List;

public record Kontoauszug(BigDecimal startkontostand, BigDecimal endkontostand, List<Transaktion> transaktionen) {
}
