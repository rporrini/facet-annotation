#!/usr/bin/python
import rdflib
import sys

ontology = sys.argv[1]
output = sys.argv[2]
graph = rdflib.Graph()
graph.load(ontology)
f = open(output, 'wb')
f.write(graph.serialize(format='nt'))

