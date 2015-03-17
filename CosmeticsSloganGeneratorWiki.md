# Introduction #
This wikipage provides instructions for the use of the Cosmetics Slogan Generator.


# Use Cases #

  * Use the generator via the web demonstrator
  * Run the generator locally or in another project


# Use the generator via demonstrator #

Instructions to run the demonstrator are given on the project home page [pp-slogan-generation](https://code.google.com/p/pp-slogan-generation/).
Please also read next section, it contains the instructions how to give the good parameters to the generator.

# Run the generator locally or in another project #

To use the slogan generator, you need the following :
  * Maven/Eclipse -project from [pp-slogan-generation](https://code.google.com/p/pp-slogan-generation/)
  * A Uby database (Tested for uby\_medium\_0\_3\_0) from http://uby.ukp.informatik.tu-darmstadt.de/uby/
  * A Web1T corpus (for rejecting too rare words during slogan generation).
  * The NRC Emotion Lexicon, Saif M. Mohammad and Peter D. Turney, NRC Technical Report,December 2013, Ottawa, Canada  (for rejecting words with negative connotation)

The only class you have to consider is de.tudarmstadt.ukp.experiments.mft.uimapp\_cosmetics.sloganGeneration.SloganGenerator.
This class contains a method main() that shows how to use this class. It is important to know that the generator uses a slogan corpus (already included in the project) to generate slogan patterns and then generate slogans. The generation of the slogan patterns takes place in the method init() of this class. This step can be skipped by using the serialization of an already initialized SloganGenerator instance. Before the initialization, the following information must be precised to the SloganGenerator : NRC Emotion Lexicon path, Slogan Corpus Path, Web1T corpus path, and uby database access information.

Once the SloganGenerator has been initialized, the method main provides a interface in the console so that the user can give his preferences for the slogan generation.