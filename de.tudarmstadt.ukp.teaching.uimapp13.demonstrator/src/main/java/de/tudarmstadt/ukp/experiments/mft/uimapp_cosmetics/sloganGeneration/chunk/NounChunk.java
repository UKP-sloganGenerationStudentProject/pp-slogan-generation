package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Parameters;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;

/**
 *
 * NounChunk is a child class from {@link Chunk}.
 *
 * It defines two more properties : if the chunk is about a body part and what this body part is.
 *
 * @author Matthieu Fraissinet-Tachet
 *
 */

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

        for (final ChunkPart occPart : _chunkParts) {
            if (occPart.getChunkPartType().equals(ChunkPartType.N)
           		&& occPart.getSemanticValue().equals("body")
           		&& Parameters.getSelectablePartsOfBody().contains(occPart.getLemma())
           		&& Parameters.getSelectablePartsOfBody().contains(occPart.getLemma())){
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
