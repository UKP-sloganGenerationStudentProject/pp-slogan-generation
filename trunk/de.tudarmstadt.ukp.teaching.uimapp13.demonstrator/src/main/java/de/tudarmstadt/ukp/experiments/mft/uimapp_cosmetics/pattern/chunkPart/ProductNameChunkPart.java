package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.Resources;




public class ProductNameChunkPart
extends ChunkPart
{
    public ProductNameChunkPart()
    {
        super();
    }

    @Override
    public List<ChunkPartSolution> generate(Resources resources,ChunkPart orginialPart)
    {
        List<ChunkPartSolution> output = new ArrayList<ChunkPartSolution>();
        ChunkPartSolution solution = new ChunkPartSolution(orginialPart,null,resources.getProductName());
        if(this.hasConstraint())
        {
            solution.addConstraintId(this.getConstraintId());
        }
        output.add(solution);
        return output;
    }


}
