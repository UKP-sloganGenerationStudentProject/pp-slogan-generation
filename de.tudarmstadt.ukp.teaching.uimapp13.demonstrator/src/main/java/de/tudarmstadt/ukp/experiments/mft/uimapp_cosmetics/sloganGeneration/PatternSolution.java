package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPattern.ChunkSolution;

public class PatternSolution
{
    private final Pattern _model;
    private final List<Integer> _constraintIds;
    private final List<ChunkSolution> _chunkSolutions;

    public PatternSolution(Pattern model)
    {
        _model = model;
        _constraintIds = new ArrayList<>();
        _chunkSolutions = new ArrayList<>();
    }

    public PatternSolution (PatternSolution solution)
    {
        _model = solution.getModel();
        _constraintIds = new ArrayList<>(solution.getConstraintIds());
        _chunkSolutions = new ArrayList<>(solution.getChunkPartSolutions());
    }

    public PatternSolution generateNewPatternSolution(ChunkSolution part)
    {
        PatternSolution output = null;

        PatternSolution sol = new PatternSolution(this);
        //if the chunkpartsolution and the current chunksolution are compatible a new ChunkSolution is generated
        if(sol.addChunkSolution(part))
        {
            output = sol;
        }
        return output;
    }

    public boolean addChunkSolution(ChunkSolution part)
    {
        boolean output = false;

        //look if the _constraints ids are compatible, ie if the intersection of the constraints is empty
        List<Integer> copyOfNewConstraintIds = new ArrayList<>(part.getConstraintIds());
        copyOfNewConstraintIds.retainAll(this._constraintIds);

        if(copyOfNewConstraintIds.size()==0)
        {
            this._constraintIds.addAll(part.getConstraintIds());
            this._chunkSolutions.add(part);

            output = true;
        }

        return output;
    }

    public Pattern getModel()
    {
        return _model;
    }

    public List<Integer> getConstraintIds()
    {
        return _constraintIds;
    }


    public List<ChunkSolution> getChunkPartSolutions()
    {
        return _chunkSolutions;
    }

    public static List<PatternSolution> concatenate(List<PatternSolution> patternSolutions, List<ChunkSolution> chunkSolutions)
    {


        List<PatternSolution> newPatternSolutions= new ArrayList<PatternSolution>();
        for(PatternSolution ps : patternSolutions)
        {
            for(ChunkSolution cs: chunkSolutions)
            {
                //try to create a new chunk solution that is the current chunk solution plus the current chunk part solution
                PatternSolution nps = ps.generateNewPatternSolution(cs);
                if(nps!=null)
                {
                    //if it works (ie constraints compatible add it to the generated chunksolutions
                    newPatternSolutions.add(nps);
                }
            }
        }
        return newPatternSolutions;
    }

    public String generateText()
    {

        StringBuilder output = new StringBuilder();
        for(ChunkSolution cs : _chunkSolutions)
        {
            output.append(cs.generateText());
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
