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

## Backend mit Docker

Das Image muss mit dem Repository als Build-Kontext gebaut werden, da der
Maven-Build neben dem Backend auch `openapi/openapi.yaml` verwendet. Die
Befehle werden im Stammverzeichnis des Repositorys ausgeführt:

```powershell
docker build -f backend/Dockerfile -t ear-backend .
```
oder
```powershell
podman build -f backend/Dockerfile -t ear-backend .
```

Vor dem Start die Zugangsdaten setzen und den Container ausführen:

```powershell
$env:EAR_USERNAME = "mein-benutzer"
$env:EAR_PASSWORD = "mein-passwort"

docker run --rm --cgroups=disabled -p 8080:8080 --env EAR_USERNAME --env EAR_PASSWORD -v ear-backend-data:/app/data ear-backend
```
oder
```powershell
$env:EAR_USERNAME = "mein-benutzer"
$env:EAR_PASSWORD = "mein-passwort"

podman run --rm --cgroups=disabled -p 8080:8080 --env EAR_USERNAME --env EAR_PASSWORD -v ear-backend-data:/app/data ear-backend
```

Das Backend ist anschließend unter `http://localhost:8080` erreichbar. Das
Docker-Volume `ear-backend-data` speichert die H2-Datenbank unabhängig vom
Container. Für ein getrennt bereitgestelltes Frontend kann zusätzlich
`EAR_FRONTEND_ORIGIN` als Umgebungsvariable an den Container übergeben werden;
standardmäßig ist `http://localhost:4200` erlaubt.

## Ermitteln der Podman-IP für direkten Zugriff

```powershell
podman machine ssh
ip -4 -o addr show
```
Dann die IP Adresse der `eth0`-Schnittstelle notieren und in POSTMAN / Browser verwenden, z.B. http://172.28.85.244

## Frontend mit Docker

Das Frontend-Image wird im Stammverzeichnis des Repositorys gebaut:

```powershell
docker build -f frontend/Dockerfile -t ear-frontend .
```
oder
```powershell
podman build -f frontend/Dockerfile -t ear-frontend .
```

Das Frontend benötigt das Backend unter `http://localhost:8080`. Starte daher
zuerst das Backend wie oben beschrieben und anschließend das Frontend:

```powershell
docker run --cgroups=disabled --rm -p 4200:80 ear-frontend
```
oder
```powershell
podman run --cgroups=disabled --rm -p 4200:80 ear-frontend
```

Die Anwendung ist unter `http://localhost:4200` erreichbar.

