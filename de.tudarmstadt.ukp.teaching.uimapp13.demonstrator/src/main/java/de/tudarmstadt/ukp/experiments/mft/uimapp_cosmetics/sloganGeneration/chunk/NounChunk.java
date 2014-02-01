package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.PatternGenerator;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;

public class NounChunk
    extends Chunk
{
    private static final long serialVersionUID = 14702405733617685L;
    boolean _isBodyPart;
    String _bodyPartName;

    public NounChunk()
    {
        super();
        this._isBodyPart = false;
        this._bodyPartName = "UNKNOWN";
    }

    @Override
    public void generateInformation(final Resources resources)
    {

        super.generateInformation(resources);

        for (final ChunkPart occPart : this._chunkParts) {
            if (occPart.getChunkPartType().equals(ChunkPartType.N)
                    && occPart.getSemanticValue().equals("body")
                    && PatternGenerator.getSelectablePartsOfBody().contains(occPart.getLemma())
                    && PatternGenerator.getSelectablePartsOfBody().contains(occPart.getLemma())) {
                this._isBodyPart = true;
                this._bodyPartName = occPart.getLemma();
            }
        }
    }

    public void setIsBodyPart()
    {
        this._isBodyPart = true;
    }

    public boolean isBodyPart()
    {
        return this._isBodyPart;
    }

    public String getBodyPartName()
    {
        return this._bodyPartName;
    }

    @Override
    public String toString()
    {
        return super.toString() + " [ isBodyPart : " + this._isBodyPart + "; bodyPartName : "
                + this._bodyPartName + "]";
    }

}
