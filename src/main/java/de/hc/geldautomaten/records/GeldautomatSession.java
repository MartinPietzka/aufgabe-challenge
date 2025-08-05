package de.hc.geldautomaten.records;

import de.hc.geldautomaten.entities.Bankkonto;
import de.hc.geldautomaten.entities.Geldautomat;
import de.hc.geldautomaten.Session;

public record GeldautomatSession(
        Geldautomat geldautomat,
        Bankkonto bankkonto) implements Session {

    @Override
    public Bankkonto getBankkonto() {
        return bankkonto();
    }
}
