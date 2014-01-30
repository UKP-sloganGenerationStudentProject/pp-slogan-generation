package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

public class AdvChunkHeader
    extends ChunkHeader
{

    private static final long serialVersionUID = 2167117216771551930L;

    public AdvChunkHeader()
    {
        super();
        this._chunkType = ChunkType.ADVC;
    }

    @Override
    public void specializedHeaderGeneration(final Chunk occurrence)
    {
        this._isValueDerivable = false;
    }

}
