package de.hc.geldautomaten;

public record GeldautomatSession(Geldautomat geldautomat, Bankkonto bankkonto) implements Session {
    @Override
    public Bankkonto getBankkonto() {
        return bankkonto();
    }
}
