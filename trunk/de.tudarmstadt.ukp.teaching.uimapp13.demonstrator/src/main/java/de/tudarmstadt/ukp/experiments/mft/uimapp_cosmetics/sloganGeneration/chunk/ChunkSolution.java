package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPartSolution;

public class ChunkSolution
implements Serializable
{

    /**
     *
     */
    private static final long serialVersionUID = -4071788481380397984L;
    private final Chunk _model;
    private final List<Integer> _constraintIds;
    private final List<ChunkPartSolution> _chunkPartSolutions;
    private boolean _hasBodyPart;

    public ChunkSolution(Chunk model)
    {
        _model = model;
        _constraintIds = new ArrayList<>();
        _chunkPartSolutions = new ArrayList<>();
    }

    public ChunkSolution (ChunkSolution solution)
    {
        _model = solution.getModel();
        _constraintIds = new ArrayList<>(solution.getConstraintIds());
        _chunkPartSolutions = new ArrayList<>(solution.getChunkPartSolutions());
        _hasBodyPart = solution.hasBodyPart();
    }

    public boolean hasBodyPart()
    {
        return _hasBodyPart;
    }


    public ChunkSolution generateNewChunkSolution(Resources resources,ChunkPartSolution part)
    {
        ChunkSolution output = null;

        ChunkSolution sol = new ChunkSolution(this);
        //if the chunkpartsolution and the current chunksolution are compatible a new ChunkSolution is generated
        if(sol.tryAddChunkPartSolution(resources,part))
        {
            output = sol;
        }
        return output;
    }

    public boolean tryAddChunkPartSolution(Resources resources, ChunkPartSolution part)
    {
        boolean output = false;

        if(_hasBodyPart && part.hasBodyPart() && resources.hasBodyPartConstraint())
        {
            return false;
        }

        //look if the _constraints ids are compatible, ie if the intersection of the constraints is empty
        List<Integer> copyOfNewConstraintIds = new ArrayList<>(part.getConstraintIds());
        copyOfNewConstraintIds.retainAll(this._constraintIds);

        if(copyOfNewConstraintIds.size()==0)
        {
            this._constraintIds.addAll(part.getConstraintIds());
            this._chunkPartSolutions.add(part);
            this._hasBodyPart = part.hasBodyPart();

            return true;
        }

        return false;
    }

    public Chunk getModel()
    {
        return _model;
    }

    public List<Integer> getConstraintIds()
    {
        return _constraintIds;
    }


    public List<ChunkPartSolution> getChunkPartSolutions()
    {
        return _chunkPartSolutions;
    }

    public static List<ChunkSolution> concatenate(Resources resources,List<ChunkSolution> chunkSolutions, List<ChunkPartSolution> chunkPartSolutions)
    {
        List<ChunkSolution> newChunkSolutions= new ArrayList<ChunkSolution>();
        for(ChunkSolution cs : chunkSolutions)
        {
            for(ChunkPartSolution cps: chunkPartSolutions)
            {
                //try to create a new chunk solution that is the current chunk solution plus the current chunk part solution
                ChunkSolution ncs = cs.generateNewChunkSolution(resources,cps);
                if(ncs!=null)
                {
                    //if it works (ie constraints compatible add it to the generated chunksolutions
                    newChunkSolutions.add(ncs);
                }
            }
        }
        return newChunkSolutions;
    }

    public String generateText()
    {

        StringBuilder output = new StringBuilder();
        for(ChunkPartSolution cps : _chunkPartSolutions)
        {
            output.append(cps.generateText());
            output.append(" ");
        }
        return output.toString();
    }

    @Override
    public String toString()
    {
        return generateText();
    }




}
