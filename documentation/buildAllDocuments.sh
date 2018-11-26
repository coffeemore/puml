#!/bin/sh

if [ ! -f projektdokumentation.aux ]
then
	echo "Bitte zuerst die projektdokumentation erzeugen"
	exit 1
fi

pdflatex -jobname entwicklerdokumentation -synctex=1 -interaction=nonstopmode "\def\CurrentAudience{developer}\input{projektdokumentation}"
pdflatex -jobname benutzerhandbuch -synctex=1 -interaction=nonstopmode "\def\CurrentAudience{manualDE}\input{projektdokumentation}"
#Zweiter Build wegen Inhaltsverzeichniss
pdflatex -jobname entwicklerdokumentation -synctex=1 -interaction=nonstopmode "\def\CurrentAudience{developer}\input{projektdokumentation}"
pdflatex -jobname benutzerhandbuch -synctex=1 -interaction=nonstopmode "\def\CurrentAudience{manualDE}\input{projektdokumentation}"

