### Product Slogan Generation based on Lexical Resources ###

This project provides a web interface to the different product slogan generators that have been developed during the [Unstructured Information Management software project](http://www.ukp.tu-darmstadt.de/teaching/courses/ws-1314/unstructured-information-management/) in winter term 2013/14 at the [UKP Lab](http://www.ukp.tu-darmstadt.de/ukp-home/), [TU Darmstadt](http://www.ukp.tu-darmstadt.de/tu/).

The slogan generation project employs
  * the [DKPro Core component collection for linguistic preprocessing of text](http://code.google.com/p/dkpro-core-asl/) - in order to linguistically analyse existing product slogans in the domains of computer games, automobiles, beauty products and soft drinks
  * lexical resources in order to generate creative slogans for selected product domains:
    * the  [large-scale linked lexical resource UBY](http://code.google.com/p/uby)
    * the [NRC Emotion Lexicon version 0.92](http://www.purl.org/net/NRCemotionlexicon) created by Saif Mohammad and Peter Turney at the National Research Council Canada in Ottawa, see also: _Saif Mohammad and Peter Turney. Crowdsourcing a Word-Emotion Association Lexicon. Computational Intelligence, 29 (3), 436-465, 2013_



### Setup ###

The project is a Wicket web application that is managed by Maven. The application was tested with Tomcat 7 and Eclipse Kepler. In order to run the demonstrator, you should use the Eclipse Java EE version (already contains m2e) and, additionally, you have to install
  * Eclipse Web Developer Tools
  * m2e-wtp - Maven Integration for WTP
  * WST Server Adapters

Set up a Tomcat 7 server in you Eclipse workspace and add the demonstrator application to it. The application expects two environment variables to be set (both as absolute paths):
  * `DKPRO_HOME`: should contain subfolder web1t/ENGLISH containing the Google n-grams corpus for n={1,2,3}
  * `DEMO_RES`: points to the _src/main/resources_ folder

Lastly, you need to create a configuration file _src/main/resources/config.properties_. You can use the existing _config.properties.sample_ as a template.