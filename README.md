# ics-split
Google Agenda .ics file splitter

Strangely, Google Agenda allows us to export our data in a single export file, but this file can't later be imported if it is too big. So we need a tool to split the exported file into smaller files, suitable for import.

This project creates an executable jar program that will create smaller files from a bigger file.

The program is called with two arguments: the name of your big file and the maximum size you want in each output file. If yous supply a name such as "google.ics" for your big file, the
program will generate 'google-1.ics', 'google-2.ics', etc.

A good way to test the program is to request a split with a very big maximum size. In this case, only one output file will be generated, and its contents is supposed to be exactly the same as the contents of the input file, byte for byte. 

Steps to split a file in smaller 500000 maximum byte files:
* Install sbt if not already done.
* sbt clean compile assembly
* java -jar ./target/scala-2.13/ics_split.jar \<your big file\> 500000

The project includes a pre-built jar file as of 2023-07-25
