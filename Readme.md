# Facet Annotation Using Knowledge Bases

[![Build Status](https://travis-ci.org/rporrini/facet-annotation.svg?branch=master)](https://travis-ci.org/rporrini/facet-annotation)

Source code and experiments described in the paper [__Facet Annotation Using Knowledge Bases__](https://par.nsf.gov/servlets/purl/10059515) accepted for publication in the proceedings of the [WWW2018 Conference](https://www2018.thewebconf.org/).

## System Requirements

* linux (tested on Linux Mint 17 Qiana)
* git
* java
* 50GB available disk space
* 4GB memory

## Configuring your local machine

```
#!bash
$ ./build-and-test.sh -skip-regression-tests
```

The ```./build-and-test.sh``` script downloads and installs various tools that are needed to run the experiments, as well as all the needed datasets, namely DBPedia and YAGO1. For this reason, the first time that you will execute the script on your local machine it will take about 1 hour to complete. After the first time only compilation and testing steps will be executed

## Running the experimental campaign

To run the whole experimental campaign you can just launch the script

```
$ ./experimental-campaign.sh 
```

However, this will take you a long time to complete since it includes also the creation of the indices, besides the run of all the algorithms. Below there is the list of all the steps that are tacken by the script.

There are few steps that have to be taken in order to run the experiments and reproduce all the experimental results. First you need to process all the dataset and create the Lucene Indexes that are needed by all the implemented facet linking approaches. This step has to be performed only once:

```
$ scripts/indexes/build-index-for-dbpedia.sh
$ scripts/indexes/scaled-depths-for-dbpedia.sh
$ scripts/indexes/build-index-for-yago1.sh
$ scripts/indexes/scaled-depths-for-yago1.sh
```
Estimated execution time: about __1__ hours for YAGO and about __6__ hours for DBPEDIA

Once the indexes are created you can run all the experiments:

```
$ ./run-all-algorithms.sh dbpedia
$ ./run-all-algorithms.sh dbpedia-ontology
$ ./run-all-algorithms.sh dbpedia-with-labels
$ ./run-all-algorithms.sh yago1
$ ./run-all-algorithms.sh yago1-simple
```

Then you can evaluate the results:

```
$ ./evaluate-all-results.sh
```

The results are saved in the directory ```evaluation/results```. Notice that the repository already comes with those results, but you can re-run the experimental campaign to replicate them or to test/evaluate improvements to the algorithm. Results are also printed on screen.

## Gold Standards

We provide two gold standards to evaluate the algorithm against the DBPedia and YAGO1 knowledge bases. Checkout for the folder ```evaluation/gold-standards```.

## Convenience scripts

Reset the repository, compile the code, test, download the knowledge bases, build the indices and run the experimental campaign (8 hours for dbpedia, 4 hours for yago1):
```
$ ./reset-and-bootstrap.sh (yago1 | dbpedia)
``` 

Run the algorithms excluding the baselines:
```
$ scripts/experiments/run-algorithm.sh $DATASET -skip-baselines
``` 

Run a single algorithm. The following script will print the raw results of an algorithm on standard output:
```
$ scripts/experiments/run-algorithm.sh
``` 
Check for the required command line parameters.


Performing a two-tailed t-test on the experimental results of all the algorithms
```
$ scripts/experiments/ttest.sh
```


