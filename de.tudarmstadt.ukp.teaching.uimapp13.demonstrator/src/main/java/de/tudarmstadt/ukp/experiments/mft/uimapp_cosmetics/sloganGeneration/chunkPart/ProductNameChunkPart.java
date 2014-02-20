package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;



/**
 *
 * Inherits from {@link ChunkPart}.
 * For product name. Will be replacsed by the product name during the slogan generation
 *
 * @author Matthieu Fraissinet-Tachet
 *
 */

public class ProductNameChunkPart
extends ChunkPart
{
    /**
     *
     */
    private static final long serialVersionUID = 4553356923866259712L;

    public ProductNameChunkPart()
    {
        super();
    }

    @Override
    public List<ChunkPartSolution> generateChunkPartSolution(Resources resources,ChunkPart orginialPart)
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
