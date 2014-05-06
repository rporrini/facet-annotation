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

* ```./run-algorithm.sh ALGORITHM METRIC-NAME CONTEXT MAJORITY-K```

Lo script stampa su standard output i risultati dell'algoritmo a seconda della metrica/export scelti.

Parametri:

* ALGORITHM: majority, ml-frequency, ml-jaccard, ml-tfidf
* METRIC-NAME: qualitative, questionnaire, trec
* CONTEXT: with-context, without-context
* MAJORITY-K: rapporto di accettazione di un predicato per majority vote (0.5 => un predicato è accettato se è associabile ad almeno il 50% dei valori del gruppo)


