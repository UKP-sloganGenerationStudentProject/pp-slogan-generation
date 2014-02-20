package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations;

import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;

/**
*
* @author Matthieu Fraissinet-Tachet
*
*/

public enum ChunkPartType
{
    N("N"),
    PUNC("PUNC"),
    ART("ART"),
    V("V"),
    ADJ("ADJ"),
    PP("PP"),
    PR("PR"),
    //CARD("CARD"),
    //CONJ("CONJ"),
    //PRT("PRT"),
    ADV("ADV"),
    PRODUCT_NAME("productname"),
    UNDEFINED("UNDEFINED");

    private String name ="";
    ChunkPartType(String name)
    {
        this.name = name;
    }

    public static ChunkPartType getTypeOf(EPartOfSpeech val)
    {
        ChunkPartType type = UNDEFINED;
        if(val.equals(EPartOfSpeech.noun)
                ||val.equals(EPartOfSpeech.nounCommon)
                ||val.equals(EPartOfSpeech.nounProper)
                ||val.equals(EPartOfSpeech.nounProperFirstName)
                ||val.equals(EPartOfSpeech.nounProperLastName))
        {
            type = N;
        }

        if(val.equals(EPartOfSpeech.verb)
                ||val.equals(EPartOfSpeech.verbAuxiliary)
                ||val.equals(EPartOfSpeech.verbMain)
                ||val.equals(EPartOfSpeech.verbModal))
        {
            type = V;
        }

        if(val.equals(EPartOfSpeech.adjective))
        {
            type = ADJ;
        }

        if(val.equals(EPartOfSpeech.adverb)
                ||val.equals(EPartOfSpeech.adverbPronominal))
        {
            type = ADV;
        }

        if(val.equals(EPartOfSpeech.determiner)
                ||val.equals(EPartOfSpeech.determinerDefinite)
                ||val.equals(EPartOfSpeech.determinerDemonstrative)
                ||val.equals(EPartOfSpeech.determinerIndefinite)
                ||val.equals(EPartOfSpeech.determinerInterrogative)
                ||val.equals(EPartOfSpeech.determinerPossessive))
        {
            type = ART;
        }

        if(val.equals(EPartOfSpeech.pronounPossessive))
        {
            type = PP;
        }

        if(val.equals(EPartOfSpeech.pronounPersonal)
                ||val.equals(EPartOfSpeech.pronounPersonalIrreflexive)
                ||val.equals(EPartOfSpeech.pronounDemonstrative)
                ||val.equals(EPartOfSpeech.pronounRelative)
                ||val.equals(EPartOfSpeech.pronounIndefinite)
                ||val.equals(EPartOfSpeech.pronounPersonalReflexive)
                ||val.equals(EPartOfSpeech.pronounPersonalIrreflexive)
                ||val.equals(EPartOfSpeech.pronounInterrogative))
        {
            type = PR;
        }

        return type;
    }

    public static ChunkPartType getTypeOf(String val)
    {
        ChunkPartType type = null;
        if(val.equals("NP")||val.equals("NN"))
        {
            type = N;
        }
        else
        {
            try
            {
                type = valueOf(val);
            }
            catch(IllegalArgumentException e)
            {
                type = UNDEFINED;
            }
        }
        return type;
    }



    @Override
    public String toString()
    {
        return name;
    }
}