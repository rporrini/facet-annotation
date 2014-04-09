#!/usr/bin/python

import rdflib

fo = open("../geonames/geonames.nt", "wb")
totalStmt = 0

with open("../geonames/all-geonames-rdf.txt") as fileobject:
	count = 0
	for line in fileobject:
		if count%2 != 0:
			g = rdflib.Graph()
			result = g.parse(data=line,format='xml')
			totalStmt += len(g)
			s = g.serialize(format='nt')
			fo.write(s)

		count = count + 1

print "Total statements: ", totalStmt
fo.close()

