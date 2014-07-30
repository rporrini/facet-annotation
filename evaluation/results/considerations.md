# PATTERN EMERGENTI DALLA SPERIMENTAZIONE

## Per quanto riguarda MAP e MRR
* "contextualized" è meglio di "simple" su TUTTI gli algoritmi su TUTTE le kb

* se siamo nel caso "simple", "partial" è sempre meglio di "no" e "complete" su TUTTI gli algoritmi e TUTTE le kb

* se siamo nel caso "contextualized" con algoritmo "ml":
+ "partial" è meglio di "no" e "complete" su "mh" e "mhw" su TUTTE le kb (su yago si ottengono gli stessi risultati)

* se siamo nel caso "contextualized" con algoritmo "mh" o "mhw":
+ "complete" è sempre il caso peggiore su tutte le su dbpedia e dbpedia-with-labels
+ su dbpedia "no" è leggermente meglio di "partial" 
+ su dbpedia-with-labels "partial" è meglio di "no"
+ su yago non c'è differenza tra "no" "partial" e "complete"

* "complete" è peggio di "no" e "partial" con TUTTI gli algoritmi su "dbpedia" e "dbpedia-with-labels" mentre su "yago" hanno gli stessi risulati

## Per quanto riguarda NDCG
* su dbpedia e dbpedia-with-labels:
+ "simple" è peggio di "contextualized"
+ "complete" è peggio di "no" e "partial"
+ "ml" ha un andamento peggiore di "mh" e "mhw" a parità di come sto considerando il contesto

