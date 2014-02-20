package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk;

import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

/**
*
* NounChunkGeneric inherits from {@link ChunkGeneric}
*
* It can contain exclusively Chunks of type noun.
* It specifies two new properties : if the chunk contains the word "the" and if it is about a person
* (we check if the chunk contains : "i", "me", "myself", "you", "yourself", "she",
                "her", "herself", "he", "him", "himself", "we", "us", "ourself", "ourselves",
                "they", "them", "themself", "themselves").
*
* @author Matthieu Fraissinet-Tachet
*
*/

public class NounChunkGeneric
    extends ChunkGeneric
{
    private static final long serialVersionUID = 825846968453941550L;
    boolean _isThe;
    boolean _isPerson;

    public NounChunkGeneric()
    {
        super();
        this._chunkType = ChunkType.NC;
        this._isThe = false;
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
        output.append("isPerson:");
        output.append(this._isPerson);
        return output.toString();
    }

    public void setIsThe()
    {
        this._isThe = true;
    }

    public void setIsPerson()
    {
        this._isPerson = true;
    }

    @Override
    public void specializedGenericGeneration(final Chunk occurrence)
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
