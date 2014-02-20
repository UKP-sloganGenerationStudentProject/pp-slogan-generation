package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;


/**
*
* AdvChunkGeneric inherits from {@link ChunkGeneric}
*
* It can contain exclusively Chunks of type adverb. It specifies also that it can contain at most
* one adverb, because we don't want Chunks of type adverb to have equivalents.
*
* @author Matthieu Fraissinet-Tachet
*
*/


public class AdvChunkGeneric
    extends ChunkGeneric
{

    private static final long serialVersionUID = 2167117216771551930L;

    public AdvChunkGeneric()
    {
        super();
        this._chunkType = ChunkType.ADVC;
    }

    @Override
    public void specializedGenericGeneration(final Chunk occurrence)
    {
        this._isValueDerivable = false;
    }

}
