# Star Crosshair (Fabric, Minecraft 1.21.11)

Voraussetzung: JDK 21.

Bauen:
    gradle wrapper --gradle-version 9.2.0   (einmalig, erzeugt gradlew)
    ./gradlew build                          (Windows: gradlew.bat build)
Die Mod liegt danach in build/libs/starcrosshair-1.0.0.jar -> in den mods-Ordner legen (mit Fabric API).

Testen ohne Installation:
    ./gradlew runClient

Eigene Textur: src/main/resources/assets/starcrosshair/textures/gui/star.png ersetzen (64x64, transparent).

Menü: Rechts-Shift (änderbar unter Optionen > Steuerung > Star Crosshair).
Einstellungen werden in config/starcrosshair.json gespeichert.
Optionale Hotkeys (standardmäßig unbelegt): Stern-Crosshair umschalten, Himmels-Bild umschalten.

Ohne Installation bauen (GitHub):
1. Auf github.com ein neues Repository anlegen und den Inhalt dieses Ordners hochladen (inkl. Ordner .github).
2. Reiter "Actions" > "Build mod" > fertigen Lauf öffnen > unten bei "Artifacts" starcrosshair-jar herunterladen.
3. Die .jar (nicht die -sources.jar) plus Fabric API in den mods-Ordner legen.
