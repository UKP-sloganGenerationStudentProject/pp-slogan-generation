package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart;

import java.util.ArrayList;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator.Resources;




public class ProductNameChunkPart
extends ChunkPart
{
    public ProductNameChunkPart()
    {
        super();
    }

    @Override
    public ArrayList<String> generate(Resources resources)
    {
        ArrayList<String> output = new ArrayList<String>();
        output.add(resources.getProductName());
        return output;
    }


}
