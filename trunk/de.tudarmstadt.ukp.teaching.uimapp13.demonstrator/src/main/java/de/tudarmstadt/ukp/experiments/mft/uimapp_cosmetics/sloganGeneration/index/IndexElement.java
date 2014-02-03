package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.index;

public abstract class IndexElement
{

    /*
     *  ChunkGeneric and ChunkPartGeneric are deriving from IndexElement
     */

    abstract protected String getId();
    abstract protected String getTypeAsString();
}
