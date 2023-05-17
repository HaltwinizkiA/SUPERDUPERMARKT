# SUPERDUPERMARKT


Punkt 

* Begründe deine Wahl verwendeter Design Patterns

Singlton für LocaleProductsBase.
Dies ist eine Datenbanksimulation. Sie muss einzigartige sein und Multi Thread, deshalb habe ich 
eine threadsafe singltone benuzt.Variablen die mehr Thread benutzt könnnen habe ich als
Atomic erstellt.
Zuerst hate ich Fabric method für die Product Erstellung in Console, 
der habe ich auch für Date load benutzt. Weiter habe ich mich für Reflection entschieden.
damit save and load mehr mastabierte sein.

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
