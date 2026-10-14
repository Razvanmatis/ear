# Entscheidungsvorlage: Einsatz von SonarQube zur Sicherstellung der Codequalität

**Adressat:** Management  
**Entscheidungsgegenstand:** Einführung von SonarQube zur automatisierten Analyse der Codequalität

## 1. Ausgangssituation

Das System besteht aus einem webbasierten Frontend und einem Java-Backend. Das Backend stellt REST-Schnittstellen bereit, persistiert Mengenmeldungen und übermittelt diese über einen bestehenden SOAP-Service. Zusätzlich ermöglicht es den Abruf bereits abgegebener Mengenmeldungen.

Da Mengenmeldungen jederzeit abgegeben werden können und Lastspitzen auftreten können, sind Wartbarkeit, Zuverlässigkeit und die frühzeitige Erkennung von Qualitätsproblemen besonders wichtig. Automatisierte Tests und ein Git-Repository sind bereits vorgesehen.

## 2. Zielsetzung

SonarQube soll den Quellcode automatisiert auf Code-Smells, potenzielle Fehler und – abhängig von der eingesetzten Edition und Konfiguration – Sicherheitsprobleme untersuchen.

Ziel ist es, Qualitätsmängel frühzeitig zu erkennen, die Wartbarkeit des Systems zu verbessern und die Einhaltung einheitlicher Qualitätsstandards sicherzustellen.

## 3. Handlungsoptionen

| Option | Vorteile | Nachteile |
|---|---|---|
| Kein SonarQube | Kein zusätzlicher Einführungs- und Betriebsaufwand | Qualitätsprobleme werden überwiegend durch Reviews und Tests erkannt |
| SonarQube Community Build | Automatisierte Codeanalyse, Qualitätskennzahlen und Integration in den Entwicklungsprozess | Konfiguration, Betrieb und Pflege verursachen Aufwand; Funktionsumfang ist begrenzt |
| Kommerzielle SonarQube-Edition | Erweiterte Funktionen, abhängig von Edition und Lizenz | Zusätzliche Lizenzkosten und gegebenenfalls höherer Einführungsaufwand |

## 4. Bewertung

SonarQube ergänzt die vorhandenen automatisierten Tests und Code Reviews. Insbesondere bei der REST- und SOAP-Kommunikation, der Verarbeitung von Mengenmeldungen und der Fehlerbehandlung kann eine automatisierte Analyse potenzielle Qualitätsprobleme frühzeitig sichtbar machen.

Die Lösung kann außerdem in die CI/CD-Pipeline integriert werden, sodass Qualitätsprüfungen bei Änderungen am Git-Repository automatisiert ausgeführt werden. Dadurch können definierte Qualitätskriterien bereits vor der Integration neuer Änderungen überprüft werden.

SonarQube ersetzt jedoch weder automatisierte Funktionstests noch Lasttests. Insbesondere die Verarbeitung von Lastspitzen muss weiterhin durch geeignete Performance- und Lasttests überprüft werden.

## 5. Empfehlung

Es wird empfohlen, zunächst den SonarQube Community Build einzusetzen und in den bestehenden Entwicklungsprozess zu integrieren. Die Analyse soll mindestens das Java-Backend umfassen; das Frontend kann entsprechend der verwendeten Programmiersprachen und des verfügbaren Funktionsumfangs ebenfalls einbezogen werden.

Im Rahmen eines Pilotbetriebs sollen die Aussagekraft der Analyseergebnisse, der zusätzliche Entwicklungsaufwand und der Nutzen für die Codequalität bewertet werden.

## 6. Zu treffende Managemententscheidung

Das Management wird gebeten, den Pilotbetrieb freizugeben und die erforderlichen personellen Ressourcen für Einrichtung, Integration und Pflege bereitzustellen.

## 7. Erfolgskriterien
- Automatisierte Codequalitätsprüfungen bei Änderungen am Repository.
- Frühzeitige Erkennung relevanter Code-Smells und potenzieller Fehler.
- Nachvollziehbare Entwicklung der Qualitätskennzahlen.
- Vertretbarer Einführungs- und Betriebsaufwand.