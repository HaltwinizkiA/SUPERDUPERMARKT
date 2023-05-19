# SUPERDUPERMARKT


Punkt 

* Begründe deine Wahl verwendeter Design Patterns

-Singlton für LocaleProductsBase.Dies ist eine Datenbanksimulation. Sie muss einzigartige und Multi Thread sein, deshalb habe ich 
eine threadsafe singltone benuzt. Für thread Sichercheit habe ich auch CopyOnWriteArrayList benutzt.
 - Fabric für Product erstellung.

Variablen die mehr Thread benutzt könnnen, habe ich als
Atomic erstellt.

Factory zur Produkterstellung in Console Render.

LocaleProductsBase.class - eine DB Simulation

Als Locale Datenspeicher habe ich CopyOnWriteArrayList für Threadsicherheit benutzt.

DiscaredProducts.csv- Geschichte der ausrangierten Produkte.

logQualityChange.csv- Geschichte des Qualitätswandels.

Reflection für Daten aus csv load, damit save and load mehr mastabierte sein.
(Verwaltung einfach durch Annotation)



__________________________________________________________________
* Erstelle ein Modul für einen neuen Produkttypen (gestalte das Produkt und die
Verarbeitungsregeln selbst)

Ich habe selbe Whiskey hinzufügt  
-Sie hat Tagespreis.
-Preiserhöhungen alle 25 Tage  
 Grundpreis+ 0,10€ * Qualität/25
-verfällt nicht

 __________________________________________________________________

* Erstelle ein Modul für eine weitere Datenquelle (SQL).
  HibernateProductRepository.class
-------------------------------------------------------------------
* Die Funktion für verworfene Produkte, habe ich über einen Thread realisiert habe.
Sie prüft einmal am Tag day counter, ändert die Qualität und entfernt gegebenenfalls das Produkt aus product.csv
und legt es in discardedProducts.csv ab.Das Protokoll wird auch in der Datei logQualityChange.csv gespeichert,
  damit man die Qualitätsänderungen überprüfen kann.
