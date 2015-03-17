# Introduction #

On this wiki page short instructions are given how to use the results of this project regarding game slogan generation.

# Use Cases #

  * Use the generator via demonstrator
  * Run the generator locally
  * Integrate the generator in some other project
  * Generate the analysis files used to develop the generator

# Use the generator via demonstrator #

Instructions to run the demonstrator are given on the project home page [pp-slogan-generation](https://code.google.com/p/pp-slogan-generation/)

# Run the generator locally #

To run the generator locally you need the following:
  * Maven/Eclipse -project from [pp-slogan-generation](https://code.google.com/p/pp-slogan-generation/)
  * An Uby database (Tested for uby\_open\_0\_3\_0) from [Uby](https://code.google.com/p/uby/)/http://uby.ukp.informatik.tu-darmstadt.de/uby/
  * A custom database. An empty schema is located in the project under: "/src/main/resources/koch/sql\_dumb\_2014\_01\_29\_1700\_no\_data"
  * (optionally) A Web1T corpus.
  * Correctly set constants in "de.koch.uim project.util.Constants"

While the generator only needs its parameters to run, the local GUI depends on additional constants. Please see class “de.koch.uim project.util.Constants”.
Edit the constants accordingly. The Javadoc gives examples.
To run the applicaton run the main method of “de.koch.uim\_project.main.Main”.

## Note ##
  * While the application will work with an empty custom database the absence of emotion words has a heavy impact on the quality of the generated slogans. In this case it is advisable to turn the emotion option to “None” to save performance.
  * The application will work without a web1t corpus but the quality of the generated slogans is lesser.

# Integrate the generator in some other project #

To use the generator in another project you need:
  * Maven/Eclipse -project from [pp-slogan-generation](https://code.google.com/p/pp-slogan-generation/)
  * An Uby database (Tested for uby\_open\_0\_3\_0) from [Uby](https://code.google.com/p/uby/)/http://uby.ukp.informatik.tu-darmstadt.de/uby/
  * A custom database. An empty schema is located in the project under: "/src/main/resources/koch/sql\_dumb\_2014\_01\_29\_1700\_no\_data"
  * (optionally) A Web1T corpus.

Classes of interest are:
  * “de.koch.uim\_project.generation.Generator” This class is used to generate slogans.
  * “de/koch/uim project/util/Config”. This class serves as configuration interface for “de.koch.uim\_project.generation.Generator”.

While the GUI and the analysis depend on constants the generator itself
does not. Everything it needs has to be specified in the conifguration class.

# Generate analysis files #

To generate the analysis files used to develop this generator you need:
  * Maven/Eclipse -project from [pp-slogan-generation](https://code.google.com/p/pp-slogan-generation/)
  * An Uby database (Tested for uby\_open\_3\_0\_0) from [Uby](https://code.google.com/p/uby/)
  * A custom database. An empty schema is located under “/src/main/resources/koch/sql\_dumb\_2014\_01\_29\_1700\_no\_data” Attention: The analysis will not produce results with an empty custom database! Tables “stopword”, “functionword” and “slogans” are essential.
  * A stop word list as text file
  * Correctly set constants in “de.koch.uim project.util.Constants” Please see class “de.koch.uim project.util.Constants”. Edit the constants accordingly. The Javadoc gives examples.

To generate the analysis files run the main method of: “de.koch.uim project.analyse.Analysemain”

# Fill the Database #

In the package "de.koch.uim\_project.database.importdb" you find classes to help you with importing data to the database. Please take a look at Dbwriter and its main method. The Javadoc gives further information.