### Einleitung

Wir stellen uns an dieser Stelle vor, dass ein Kunde einen Auftrag für eine individuelle Entwicklung dieses Projektes an
dich übergeben hat.

[Unvollständige Skizze des Datenmodells](challenges_geldautomaten_skizze.png)

### Aufgabenstellung

Eine Bank möchte eine Software zur Nutzung eines Bankkontos durch ihre Kunden bereitstellen. Hierbei fallen folgende
Anwendungsfälle an:

- Der Kunde kann einen Geldbetrag auf ein Bankkonto einzahlen

- Der Kunde kann einen Geldbetrag von einem Bankkonto abheben

- Der Kunde kann sich den aktuellen Stand seines Kontos zurückliefern lassen

- Der Kunde kann sich alle Transaktionen, die auf seinem Konto angefallen sind, zurückliefern lassen

- Der Kunde kann sich Kontoauszüge erzeugen lassen, welche alle Transaktionen enthält, die zwischen einem Start- und
  inklusivem Enddatum erfolgt sind

### Hinweise/Vorgaben

- Diese Challenge beinhaltet (bis auf Gradle) nur Technologien, die du aus der Journey 2 Junior kennst

- Es wird ein Github-Repository für dich bereitgestellt, welches bereits die grobe Struktur der Anwendung vorgibt.

- Es soll keine UI erstellt werden. Nutze die Main-Methode aus der Klasse <i>de.hc.geldautomaten.Bank</i>

- Die Daten sollen in einer h2-Datenbank persistiert werden

- Implementiere gegen die bereitgestellten Interfaces, ergänze sie, wenn notwendig, führe aber keine strukturellen
  Änderungen durch. Solltest
  du dennoch strukturelle Änderungen durchführen, so musst du sie im Review erläutern.

- Strukturiere den Code und unterteile ihn entsprechend einer Schichtenarchitektur.

- Setze Logging ein, wo du es für sinnvoll erachtest

- 100% Testabdeckung wird vorausgesetzt

- Dokumentiere deinen Code, wo du es für sinnvoll erachtest 

- Verwende JDBC; bedenke aber, dass diese Art der Persistenz vielleicht nicht für immer bleibt

- Die Anwendung soll als executable Jar bereitgestellt werden können.

### Erwartete Ergebnisse im bereitgestellten Github-Repository

- Entity-Relationship Diagramm

- Use-Case Diagramm

- Class-Diagram

- Fertige Implementierung

### Ablauf

Timebox: ca. 2 PT

- Für den Zeitraum der Challenge bist du von jedem sonstigen Termin freigestellt

- Nach Ablauf der Zeit wird das Github-Repository geschlossen und es werden keine weiteren Pushes gestattet, daher
  aktualisiere regelmäßig dein Repository

- Nach erfolgtem Review durch deine Trainer erfolgt eine gemeinsame Nachbesprechung.
