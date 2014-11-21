## System Requirements

* linux (tested on Linux Mint 17 Qiana)
* git
* java

## Checking out the repository and configuring your local machine

1. ```git clone https://bitbucket.org/rporrini/cluster-labelling.git```
2. ```cd cluster-labelling```
3. ```./build-and-test.sh```

The ```./build-and-test.sh``` script downloads and installs various tools that are needed to run the experiments, as well as all the needed datasets, namely DBPedia and YAGO1. For this reason, the first time that you will execute the script on your local machine it will take about 1 hour to complete. After the first time only compilation and testing steps will be executed

## Running the experimental campaign

To run the whole experimental campaign you can just launch the script

* ```./experimental-campaign.sh```

However, this will take you a long time to complete since it includes also the creation of the indices, besides the run of all the algorithms. Below there is the list of all the steps that are tacken by the script.

There are few steps that have to be taken in order to run the experiments and reproduce all the experimental results. First you need to process all the dataset and create the Lucene Indexes that are needed by all the implemented facet linking approaches. This step has to be performed only once:

* ```./scripts/indexes/build-index-for-dbpedia.sh```. Estimated execution time: about __20__ hours
* ```./scripts/indexes/build-index-for-yago.sh```. Estimated execution time: about __9__ hours

Once the indexes are created you can run all the experiments:

* ```./run-all-algorithms.sh dbpedia```
* ```./run-all-algorithms.sh dbpedia-ontology```
* ```./run-all-algorithms.sh dbpedia-with-labels```
* ```./run-all-algorithms.sh yago1```
* ```./run-all-algorithms.sh yago1-simple```

Then you can evaluate the results:

* ```./evaluate-all-results.sh```

The results are saved in the directory ```evaluation/results```. Notice that the repository already comes with those results, but you can re-run the experimental campaign to replicate them or to test/evaluate improvements to the algorithm.

## Gold Standards

We provide two gold standards to evaluate the algorithm against the DBPedia and YAGO1 knowledge bases. Checkout for the folder ```evaluation/gold-standards```.

## Convenience scripts

Running a single algorithm. The following script will print the raw results of an algorithm on standard output:

* ```./scripts/experiments/run-algorithm.sh```. Check for the required command line parameters.

Performing a two-tailed t-test on the experimental results of two algorithms

* ```./ttest.sh```. Check for the required command line parameters.

## Contacts

Riccardo Porrini, [riccardo.porrini@disco.unimib.it](mailto:riccardo.porrini@disco.unimib.it)

