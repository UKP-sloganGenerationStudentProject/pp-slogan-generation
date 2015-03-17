# Introduction #

The following informations describe how to use the Car Slogan Generator.

# Use the generator via demonstrator #

Instructions to run the demonstrator are given on the project home page [pp-slogan-generation](https://code.google.com/p/pp-slogan-generation/)

# Integrate the generator in some other project #

To use the generator in another project you need:
  * Maven/Eclipse -project from [pp-slogan-generation](https://code.google.com/p/pp-slogan-generation/)
  * An Uby database from [Uby](https://code.google.com/p/uby/)/http://uby.ukp.informatik.tu-darmstadt.de/uby/
  * (optionally) A Web1T corpus. _Needed for Slogan Pattern 8 and 9 (NC PC NC)_
  * (optionally) The NRC Emotion Lexicon. _Needed for emotion support._


## Classes of interest ##

### de.tobiasloeser.slogangenerator.Main ###
Use the function **main** to get a list of objects of the class **Slogan**.
The class Slogan has a function **toString** to get the slogan as a string.
The function **main** needs a object of the class **SGConfig**.
### de.tobiasloeser.slogangenerator.SGConfig ###
This class offers different options.
  * SloganCount: Count of slogans to generate
  * ProductName: Name of product
  * UseProductNameCreative: Possibility to use product name in different ways
  * GoodLuck: Generator will try to get more verbs with semantic label
  * Emotion: Get slogans with emotions like
    * positive
    * negative
    * anger
    * anticipation
    * disgust
    * fear
    * joy
    * sadness
    * surprise
    * trust
  * TemplateId:
    1. NC with Alliteration
    1. NC without Alliteration
    1. VC NC without Alliteration
    1. VC NC with Alliteration
    1. NC VC NC without Alliteration
    1. NC VC NC with Alliteration
    1. NC with Oxymoron
    1. NC PC NC without Alliteration (Web1T is needed)
    1. NC PC NC with Alliteration (Web1T is needed)

Settings:
  * DBUser: User for database
  * DBPassword: Password for database
  * DBUrl: URL of database
  * Web1TPath: Path to Web1T
  * EmotionPath: Path to Emotionlist.sg (generation possible with NRC Emotion Lexicon)

### de.tobiasloeser.slogangenerator.SloganGenerator ###

Use the constructor with an object of SGConfig and then use the generateSlogans method (also with SGConfig) to generate slogans the simplest way.

## Creation of Emotionlist.sg ##

To create the Emotionlist.sg file use the class EmotionListGenerator. You need the path to the NRC Emotion Lexicon and the path where you want to save the Emotionlist.sg file.