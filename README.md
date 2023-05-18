# SUPERDUPERMARKT


Punkt 

* Begründe deine Wahl verwendeter Design Patterns

Singlton für LocaleProductsBase. 
Dies ist eine Datenbanksimulation. Sie muss einzigartige und Multi Thread sein, deshalb habe ich 
eine threadsafe singltone benuzt. Für thread Sichercheit habe ich auch CopyOnWriteArrayList benutzt.

Variablen die mehr Thread benutzt könnnen, habe ich als
Atomic erstellt.
Factory zur Produkterstellung in Console Render.

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
