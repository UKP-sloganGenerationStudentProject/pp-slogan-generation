package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.index.Index;

public class TestChunkEquivalence
{

    @Test
    public void testGenericChunkEquivalence()
    {
        //we test here the two main equivalence mechanisms : chunktype and semantic value

        Index<ChunkGeneric> chunkGenericIndex = new Index<ChunkGeneric>();

        NounChunkGeneric ncg1 = new NounChunkGeneric();
        ncg1.setSemanticValue("sem1");
        chunkGenericIndex.addElement(ncg1);
        NounChunkGeneric ncg2 = new NounChunkGeneric();
        ncg2.setSemanticValue("sem2");
        chunkGenericIndex.addElement(ncg2);

        AdjChunkGeneric acj1 = new AdjChunkGeneric();
        acj1.setSemanticValue("sem1");
        chunkGenericIndex.addElement(acj1);
        AdjChunkGeneric acj2 = new AdjChunkGeneric();
        acj2.setSemanticValue("sem2");
        chunkGenericIndex.addElement(acj2);

        VerbChunkGeneric vcg1 = new VerbChunkGeneric();
        vcg1.setSemanticValue("sem1");
        chunkGenericIndex.addElement(vcg1);
        VerbChunkGeneric vcg2 = new VerbChunkGeneric();
        vcg2.setSemanticValue("sem2");
        chunkGenericIndex.addElement(vcg2);

        PrepChunkGeneric pcg1 = new PrepChunkGeneric();
        pcg1.setSemanticValue("sem1");
        chunkGenericIndex.addElement(pcg1);
        PrepChunkGeneric pcg2 = new PrepChunkGeneric();
        pcg2.setSemanticValue("sem2");
        chunkGenericIndex.addElement(pcg2);

        AdvChunkGeneric adcg1 = new AdvChunkGeneric();
        adcg1.setSemanticValue("sem1");
        chunkGenericIndex.addElement(adcg1);
        AdvChunkGeneric adcg2 = new AdvChunkGeneric();
        adcg2.setSemanticValue("sem2");
        chunkGenericIndex.addElement(adcg2);

        assertTrue("Chunks of differents type and with different semantic value can't be equivalent",chunkGenericIndex.getElements().size()==10);
    }

}
