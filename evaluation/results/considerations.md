# PATTERN EMERGENTI DALLA SPERIMENTAZIONE

## Per quanto riguarda MAP e MRR
* __contextualized__ è meglio di __simple__ su TUTTI gli algoritmi su TUTTE le kb

* se siamo nel caso __simple__, __partial__ è sempre meglio di __no__ e __complete__ su TUTTI gli algoritmi e TUTTE le kb

* se siamo nel caso __contextualized__ con algoritmo __ml__:
	+ __partial__ è meglio di __no__ e __complete__ su __mh__ e __mhw__ su TUTTE le kb (su yago si ottengono gli stessi risultati)

* se siamo nel caso __contextualized__ con algoritmo __mh__ o __mhw__:
	+ __complete__ è sempre il caso peggiore su tutte le su dbpedia e dbpedia-with-labels
	+ su dbpedia __no__ è leggermente meglio di __partial__ 
	+ su dbpedia-with-labels __partial__ è meglio di __no__
	+ su yago non c'è differenza tra __no__ __partial__ e __complete__

* __complete__ è peggio di __no__ e __partial__ con TUTTI gli algoritmi su __dbpedia__ e __dbpedia-with-labels__ mentre su __yago__ hanno gli stessi risulati

## Per quanto riguarda NDCG
* su dbpedia e dbpedia-with-labels:
	+ __simple__ è peggio di __contextualized__
	+ __complete__ è peggio di __no__ e __partial__
	+ __ml__ ha un andamento peggiore di __mh__ e __mhw__ a parità di come sto considerando il contesto

