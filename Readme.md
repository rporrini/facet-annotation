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
Lo script stampa su standard output i risultati dell'algoritmo a seconda della metrica/export scelti. Dalla root del repository:

* ```./run-algorithm.sh algorithm=ALGORITHM occurrences=OCCURRENCES context=CONTEXT kb=KNOWLEDGE-BASE summary=METRIC-NAME```

```
summary	=	the format of the results, namely questionnaire, trec
occurrences	=	the function applied to count each occurrence, namely simple, contextualized
kb	=	the knowledge base to use, namely dbpedia, dbpedia-with-labels, yago1, yago1-simple
algorithm	=	the algorithm to use, namely mh, mhw, mhsw, mhwv, mhwcv, ml
```

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

* KNOWLEDGE-BASE: dbpedia, dbpedia-with-labels, yago1, yago1-simple

