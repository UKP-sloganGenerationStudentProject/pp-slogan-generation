package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkpart;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPartGeneric;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.index.Index;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;

public class TestChunkPartEquivalence
{

    @Test
    public void testGenericChunkEquivalence()
    {
        //we test here the two main equivalence mechanisms : chunktype and semantic value

        Index<ChunkPartGeneric> chunkPartGenericIndex = new Index<ChunkPartGeneric>();

        for(ChunkPartType type : ChunkPartType.values())
        {
            chunkPartGenericIndex.addElement(ChunkPartGeneric.createChunkPartGeneric(type, "sem1"));
            chunkPartGenericIndex.addElement(ChunkPartGeneric.createChunkPartGeneric(type, "sem2"));
        }

            assertTrue("ChunkParts of differents type and with different semantic value can't be equivalent",chunkPartGenericIndex.getElements().size()==2*ChunkPartType.values().length);

    }

}
