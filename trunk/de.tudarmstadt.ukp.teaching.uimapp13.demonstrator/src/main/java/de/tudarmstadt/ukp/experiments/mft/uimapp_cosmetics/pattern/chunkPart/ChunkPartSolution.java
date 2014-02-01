package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart;

import java.util.ArrayList;
import java.util.List;

public class ChunkPartSolution
{
    private final ChunkPart _model;
    private List<Integer> _constraintIds;
    private final String _lemmaSolution;

    public ChunkPartSolution(ChunkPart model,List<Integer> constraintIds, String solutionLemma)
    {
        _model = model;
        _lemmaSolution = solutionLemma;
        _constraintIds = constraintIds;
        if(_constraintIds == null)
        {
            _constraintIds = new ArrayList<>();
        }
    }

    public List<Integer> getConstraintIds()
    {
        return _constraintIds;
    }
}
