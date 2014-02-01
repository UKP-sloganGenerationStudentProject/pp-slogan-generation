package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk;

import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

public class NounChunkGeneric
    extends ChunkGeneric
{
    private static final long serialVersionUID = 825846968453941550L;
    boolean _isThe;
    boolean _isProperNoun;
    boolean _isPerson;

    public NounChunkGeneric()
    {
        super();
        this._chunkType = ChunkType.NC;
        this._isThe = false;
        this._isProperNoun = false;
        this._isPerson = false;
    }

    @Override
    public String getId()
    {
        final StringBuilder id = new StringBuilder();
        id.append(super.getId());
        id.append(" [");
        id.append("_isThe:");
        id.append(this._isThe);
        id.append("]");
        id.append(" [");
        id.append("ProperNoun:");
        id.append(this._isProperNoun);
        id.append("]");
        id.append("isPerson:");
        id.append(this._isPerson);
        return id.toString();
    }

    @Override
    public String getSpecificInformation()
    {
        final StringBuilder output = new StringBuilder();
        output.append(" [");
        output.append("_isThe:");
        output.append(this._isThe);
        output.append("]");
        output.append(" [");
        output.append("ProperNoun:");
        output.append(this._isProperNoun);
        output.append("]");
        output.append("isPerson:");
        output.append(this._isPerson);
        return output.toString();
    }

    public void setIsThe()
    {
        this._isThe = true;
    }

    public void setIsProperNoun()
    {
        this._isProperNoun = true;
    }

    public void setIsPerson()
    {
        this._isPerson = true;
    }

    @Override
    public void specializedHeaderGeneration(final Chunk occurrence)
    {
        this._isValueDerivable = true;
        this._semanticValue = occurrence.getAt(occurrence.getPartsNbr() - 1).getSemanticValue();

        // isThe
        for (final ChunkPart occPart : occurrence.getChunkParts()) {
            if (occPart.getTakenValue().toLowerCase().equals("the")) {
                this.setIsThe();
            }
        }


        // isPerson
        final List<String> person = Arrays.asList("i", "me", "myself", "you", "yourself", "she",
                "her", "herself", "he", "him", "himself", "we", "us", "ourself", "ourselves",
                "they", "them", "themself", "themselves");
        if (person.contains(occurrence.getAt(occurrence.getPartsNbr() - 1).getTakenValue())) {
            this.setIsPerson();
        }

    }
}
