# ear

## Backend-Anmeldung

Die Backend-Anmeldung verwendet HTTP Basic. Vor dem Start des Backends müssen
`EAR_USERNAME` und `EAR_PASSWORD` als Umgebungsvariablen gesetzt werden. Es gibt
keine Zugangsdaten im Quellcode oder Standardzugänge. Für ein getrennt
bereitgestelltes Frontend kann die erlaubte Origin über
`EAR_FRONTEND_ORIGIN` konfiguriert werden; lokal ist der Standard
`http://localhost:4200`.

In PowerShell können die Werte für die aktuelle Sitzung beispielsweise so
gesetzt werden:

```powershell
$env:EAR_USERNAME = "mein-benutzer"
$env:EAR_PASSWORD = "mein-passwort"
$env:EAR_FRONTEND_ORIGIN = "http://localhost:4200"
```

HTTP Basic überträgt die Zugangsdaten bei jeder Anfrage im Authorization-Header.
Außerhalb lokaler Entwicklung muss die Verbindung deshalb durch HTTPS geschützt
sein.