package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.pattern;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.ChunkSolution;

public class PatternSolution
{
    private final Pattern _model;
    private final List<Integer> _constraintIds;
    private final List<ChunkSolution> _chunkSolutions;
    private boolean _hasBodyPart;

    public PatternSolution(Pattern model)
    {
        _model = model;
        _constraintIds = new ArrayList<>();
        _chunkSolutions = new ArrayList<>();
        _hasBodyPart = false;
     }

    public PatternSolution (PatternSolution solution)
    {
        _model = solution.getModel();
        _constraintIds = new ArrayList<>(solution.getConstraintIds());
        _chunkSolutions = new ArrayList<>(solution.getChunkPartSolutions());
        _hasBodyPart = solution.hasBodyPart();
    }

    public PatternSolution generateNewPatternSolution(Resources resources,ChunkSolution part)
    {
        PatternSolution output = null;

        PatternSolution sol = new PatternSolution(this);
        //if the chunkpartsolution and the current chunksolution are compatible a new ChunkSolution is generated
        if(sol.tryAddChunkSolution(resources,part))
        {
            output = sol;
        }
        return output;
    }

    public boolean tryAddChunkSolution(Resources resources, ChunkSolution part)
    {

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
            this._chunkSolutions.add(part);
            if(part.hasBodyPart())
            {
                this._hasBodyPart = true;
            }

            return true;
        }

        return false;
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

    public boolean hasBodyPart()
    {
        return _hasBodyPart;
    }

    public static List<PatternSolution> concatenate(Resources resources,List<PatternSolution> patternSolutions, List<ChunkSolution> chunkSolutions)
    {
        List<PatternSolution> newPatternSolutions= new ArrayList<PatternSolution>();
        for(PatternSolution ps : patternSolutions)
        {
            for(ChunkSolution cs: chunkSolutions)
            {
                //try to create a new chunk solution that is the current chunk solution plus the current chunk part solution
                PatternSolution nps = ps.generateNewPatternSolution(resources,cs);
                if(nps!=null)
                {
                    //if it works (ie constraints compatible add it to the generated chunksolutions
                    newPatternSolutions.add(nps);
                }
            }
        }
        return newPatternSolutions;
    }

    public boolean hasConstraint()
    {
        return this._constraintIds.size()>0;
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
