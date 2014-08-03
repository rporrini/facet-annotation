## Requisiti di sistema

* linux
* git
* java
* eclipse

## Per iniziare a sviluppare

1. ```git clone https://{USERNAME}:{PASSWORD}@bitbucket.org/rporrini/cluster-labelling.git```
2. ```cd cluster-labelling```
3. ```./build-and-test.sh```

La procedura di build installa l'insieme dei tool necessari e prevede anche lo scaricamento dei dataset per la sperimentazione. Per questo motivo la prima volta che viene eseguita dura un pò (nell'ordine dei 60 minuti)

## Creazione degli indici
Dalla root del repository:

* ```./build-index-for-dbpedia.sh```. Per creare gli indici di DBPedia. Tempo stimato per l'indicizzazione: circa __20__ ore
* ```./build-index-for-yago.sh```. Per creare gli indici di Yago. Tempo stimato per l'indicizzazione: circa __9__ ore

## Creazione del Gold Standard di Limaye (Yago)
Dalla root del repository:

* ```./generate-sarawagi-gold-standard.sh```
* passo di pulizia a mano :(
* ```./generate-sarawagi-qrels.sh```

## Run degli algoritmi
Dalla root del repository:

* ```./run-algorithm.sh algorithm=ALGORITHM occurrences=OCCURRENCES context=CONTEXT kb=KNOWLEDGE-BASE summary=METRIC-NAME```

Lo script stampa su standard output i risultati dell'algoritmo a seconda della metrica/export scelti.

Parametri:

* ALGORITHM: mh, mhw, mhsw, mhwv, mhwcv, ml
* OCCURRENCES: simple, contextualized 
* CONTEXT: no, partial, complete
* KNOWLEDGE-BASE: dbpedia, yago1, dbpedia-with-labels
* METRIC-NAME: qualitative, questionnaire, trec

## Run di tutti gli algoritmi
Dalla root del repository:

* ```./run-all-algorithms.sh KNOWLEDGE-BASE```

Lo script salva risultati di tutti gli algoritmi ognuno in un file apposito (nella cartella ```evaluation/results/KNOWLEDGE-BASE-results```) a seconda della base di conoscenza scelta.

Parametri:

* KNOWLEDGE-BASE: dbpedia, yago1, dbpedia-with-labels

## Calcolo delle metriche via trec_eval
Dalla root del repository:

* ```./evaluate-results.sh KNOWLEDGE-BASE GOLD-STANDARD RESULTS-FILE```

Parametri:

* KNOWLEDGE-BASE: dbpedia, yago1, dbpedia-with-labels
* GOLD-STANDARD: file formato trec del gold standard (si trovano nella cartella ```evaluation/results/```)
* RESULTS-FILE: file formato trec dei risultati degli algoritmi (si trovano nelle cartelle ```evaluation/results/KNOWLEDGE-BASE-results```)

## Calcolo delle metriche di tutti gli algoritmi via trec_eval
Dalla root del repository:

* ```./evaluate-all-results.sh KNOWLEDGE-BASE```

Lo script prende tutti i risultati prodotti da ```./run-algorithm.sh KNOWLEDGE-BASE METRIC-NAME``` e li confronta con il gold-standard generando un file ```all-results.csv``` contenente il confronto
Parametri:

* KNOWLEDGE-BASE: dbpedia, yago1, dbpedia-with-labels

# PATTERN EMERGENTI DALLA SPERIMENTAZIONE

## Per quanto riguarda MAP e MRR
* __contextualized__ è meglio di __simple__ su TUTTI gli algoritmi su TUTTE le kb

* se siamo nel caso __simple__, __partial__ è sempre meglio di __no__ e __complete__ su TUTTI gli algoritmi e TUTTE le kb

* se siamo nel caso __contextualized__ con algoritmo __ml__:
	+ __partial__ è meglio di __no__ e __complete__ su TUTTE le kb (su yago si ottengono gli stessi risultati)

* se siamo nel caso __contextualized__ con algoritmo __mh__ o __mhw__:
	+ su dbpedia __no__ è leggermente meglio di __partial__ 
	+ su dbpedia-with-labels __partial__ è meglio di __no__
	+ __complete__ è sempre il caso peggiore su dbpedia e dbpedia-with-labels
	+ su yago non c'è differenza tra __no__ __partial__ e __complete__

* __complete__ è sempre peggio di __no__ e __partial__ su __dbpedia__ e __dbpedia-with-labels__ mentre su __yago__ hanno gli stessi risulati

## Per quanto riguarda NDCG
* su dbpedia e dbpedia-with-labels:
	+ __simple__ è peggio di __contextualized__
	+ __complete__ è peggio di __no__ e __partial__
	+ __ml__ ha un andamento peggiore di __mh__ e __mhw__ a parità di come considero il contesto
