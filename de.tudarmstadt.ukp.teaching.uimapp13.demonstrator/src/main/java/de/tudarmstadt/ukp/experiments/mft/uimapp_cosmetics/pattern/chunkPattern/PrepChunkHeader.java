package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;


public class PrepChunkHeader
    extends ChunkHeader
{
    private static final long serialVersionUID = -4041465958122119822L;

    public PrepChunkHeader()
    {
        super();
        this._chunkType = ChunkType.PC;
    }

    @Override
    public void specializedHeaderGeneration(final Chunk occurrence)
    {
        this._isValueDerivable = false;
    }

}
