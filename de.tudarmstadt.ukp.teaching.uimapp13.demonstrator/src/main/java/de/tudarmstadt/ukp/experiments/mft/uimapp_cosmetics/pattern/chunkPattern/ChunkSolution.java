package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPartSolution;

public class ChunkSolution
{

    private final Chunk _model;
    private final List<Integer> _constraintIds;
    private final List<ChunkPartSolution> _chunkPartSolutions;

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
    }

    public ChunkSolution generateNewsChunkSolution(ChunkPartSolution part)
    {
        ChunkSolution output = null;

        ChunkSolution sol = new ChunkSolution(this);
        //if the chunkpartsolution and the current chunksolution are compatible a new ChunkSolution is generated
        if(sol.addChunkPartSolution(part))
        {
            output = sol;
        }
        return output;
    }

    public boolean addChunkPartSolution(ChunkPartSolution part)
    {
        boolean output = false;

        //look if the _constraints ids are compatible, ie if the intersection of the constraints is empty
        List<Integer> copyOfNewConstraintIds = new ArrayList<>(part.getConstraintIds());
        copyOfNewConstraintIds.retainAll(this._constraintIds);

        if(copyOfNewConstraintIds.size()==part.getConstraintIds().size())
        {
            this._constraintIds.addAll(part.getConstraintIds());
            this._chunkPartSolutions.add(part);

            output = true;
        }

        return output;
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



}
