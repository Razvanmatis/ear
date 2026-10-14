# Entscheidungsvorlage: Einsatz von Grafana/Prometheus zur Überwachung des Systems

## 1. Ausgangssituation

Das System besteht aus einem Web-Frontend und einem Java-Backend. Das Frontend ermöglicht die Erfassung und Anzeige von Mengenmeldungen. Das Backend stellt REST-Schnittstellen bereit, persistiert die Meldungen und leitet eingereichte Daten über einen bestehenden SOAP-Service weiter. Außerdem ermöglicht es den Abruf bereits eingereichter Mengenmeldungen.

Mengenmeldungen können jederzeit eingereicht werden; zeitweise ist mit erhöhtem Anfrageaufkommen zu rechnen. Eine Fehlerbehandlung und automatisierte Tests sind vorhanden. Der Quellcode und die zugehörige Dokumentation werden in einem Git-Repository verwaltet.

Derzeit stehen jedoch nur begrenzt aussagekräftige Tracing- und Betriebsdaten zur Verfügung. Ein konkreter Bedarf für ein kontinuierliches, zentrales Monitoring ist bislang nicht ausreichend belegt.

## 2. Zielsetzung

Ziel ist es, den Nutzen einer Einführung von Grafana und Prometheus für die Überwachung des Systems zu bewerten.

Dabei soll insbesondere geprüft werden, ob der erwartete Mehrwert den zusätzlichen Implementierungs-, Konfigurations- und Betriebsaufwand zum aktuellen Entwicklungsstand rechtfertigt.

## 3. Handlungsoptionen

**Option A: Verzicht auf Grafana/Prometheus**

- Kein zusätzlicher Betriebs- und Wartungsaufwand für einen separaten Monitoring-Stack
- Nutzung der bestehenden Fehlerbehandlung und Protokollierung
- Gezielte Fehleranalysen sowie Last- und Performance-Tests bei konkretem Bedarf
- Erneute Bewertung, sobald aussagekräftige Betriebsdaten oder konkrete Monitoring-Anforderungen vorliegen

**Option B: Einführung von Grafana/Prometheus**

- Zentrale Erfassung und Visualisierung geeigneter Betriebsmetriken
- Erkennung von Auffälligkeiten bei Antwortzeiten, Fehlerraten und Ressourcenauslastung
- Möglichkeit zur Einrichtung von Dashboards und Alarmierungen
- Zusätzlicher Aufwand für Instrumentierung, Konfiguration, Betrieb und Wartung
- Der tatsächliche Nutzen hängt von der Auswahl aussagekräftiger Metriken und deren zuverlässiger Erfassung ab

## 4. Bewertung

Grafana und Prometheus können insbesondere bei erhöhtem Anfrageaufkommen helfen, Engpässe und Betriebsprobleme frühzeitig zu erkennen. Für das vorliegende System wären beispielsweise Antwortzeiten der REST-Schnittstellen, Fehlerquoten bei der Verarbeitung von Mengenmeldungen sowie die Dauer und Fehlerrate der SOAP-Aufrufe relevante Kennzahlen.

Zum aktuellen Zeitpunkt liegen jedoch nicht ausreichend aussagekräftige Tracing- und Betriebsdaten vor, um den konkreten Monitoring-Bedarf zuverlässig zu bestimmen. Die Einführung würde zunächst zusätzliche Instrumentierung und Konfiguration erfordern, ohne dass der unmittelbare Nutzen bereits ausreichend belegt ist.

Die bestehende Fehlerbehandlung und die automatisierten Tests unterstützen bereits die Absicherung der Funktionalität. Sie ersetzen zwar kein Laufzeitmonitoring, bieten aber eine Grundlage, um die Systemfunktionalität zunächst ohne zusätzlichen Monitoring-Stack abzusichern.

Dabei ist zu berücksichtigen, dass Prometheus auch unabhängig von verteiltem Tracing Metriken erfassen kann. Fehlende Tracing-Daten allein schließen seinen Einsatz daher nicht aus. Ausschlaggebend ist vielmehr, dass der zusätzliche Aufwand derzeit noch nicht durch einen ausreichend konkretisierten Betriebsbedarf gerechtfertigt wird.

## 5. Empfehlung

Es wird empfohlen, zunächst auf die Einführung von Grafana und Prometheus zu verzichten.

Die vorhandenen Möglichkeiten zur Fehleranalyse sollen weiter genutzt und bei konkretem Bedarf durch gezielte Last- und Performance-Tests ergänzt werden. Gleichzeitig sollte die Architektur einen späteren Einstieg in das Monitoring ermöglichen.

Sobald konkrete Performance-Probleme, erhöhte Anforderungen an die Betriebsüberwachung oder eine aussagekräftigere Datenbasis vorliegen, soll die Entscheidung erneut überprüft werden.

## 6. Zu treffende Managemententscheidung

Das Management wird gebeten, den vorläufigen Verzicht auf Grafana und Prometheus zu bestätigen.

Dadurch werden zum aktuellen Zeitpunkt keine zusätzlichen Ressourcen für die Einführung und den Betrieb eines separaten Monitoring-Stacks eingeplant. Eine spätere Einführung bleibt bei entsprechendem Bedarf ausdrücklich möglich.

## 7. Erfolgskriterien

- Die Systemfunktionalität wird weiterhin durch automatisierte Tests abgesichert.
- Fehler können mithilfe der vorhandenen Fehlerbehandlung und Protokollierung untersucht werden.
- Konkrete Performance-Fragestellungen können bei Bedarf durch gezielte Last- und Performance-Tests beantwortet werden.
- Der Bedarf an kontinuierlichem Monitoring wird bei veränderten Betriebsanforderungen erneut bewertet.