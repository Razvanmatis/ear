# ADR-001: Auswahl der Persistenztechnologie

**Status:** Angenommen

## Kontext

Das Backend muss die Daten der `MengenabgabeEntity` dauerhaft speichern. Die Entität enthält Mengen-, Einheiten-, Zeitraums- und Registrierungsdaten. Für die Speicherung wird eine relationale Datenbank benötigt, die sich in Spring Boot integrieren lässt.

## Betrachtete Optionen

**1. H2:** Eingebettete relationale Datenbank mit geringem Konfigurations- und Betriebsaufwand. Unterstützt SQL und die Integration über JPA.

**2. PostgreSQL:** Leistungsfähiges relationales Datenbanksystem mit umfangreichen Funktionen und guter Eignung für produktive Anwendungen. Erfordert zusätzlichen Infrastruktur- und Betriebsaufwand.

**3. MySQL:** Etabliertes relationales Datenbanksystem mit guter Unterstützung für produktive Anwendungen. Benötigt ebenfalls eine separate Datenbankinfrastruktur.

## Entscheidung

H2 wird als Persistenztechnologie ausgewählt. Für den vorgesehenen Anwendungsfall bietet sie die erforderlichen relationalen Funktionen bei geringem Konfigurations- und Betriebsaufwand. PostgreSQL und MySQL werden aufgrund des höheren Infrastrukturaufwands nicht bevorzugt.

Der Datenbankzugriff erfolgt über Spring Data JPA.

## Konsequenzen

- Einfache Integration in Spring Boot.
- Geringer Betriebsaufwand.
- Dauerhafte Speicherung erfordert den dateibasierten Betrieb von H2; im In-Memory-Modus gehen die Daten beim Beenden der Datenbank verloren.
- Bei steigenden Anforderungen an Verfügbarkeit oder Skalierung ist die Entscheidung erneut zu prüfen.