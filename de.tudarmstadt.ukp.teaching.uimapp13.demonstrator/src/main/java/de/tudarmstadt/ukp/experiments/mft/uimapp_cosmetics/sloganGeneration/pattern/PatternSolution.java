package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.pattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.Chunk;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.ChunkSolution;

/**
 *
 * A PatternSolution object represents a slogan generated by a {@link Pattern}.
 * It contains the list of {@link ChunkSolution} object generated by the {@link Chunk}s of this pattern.
 * It can be directly converted into a slogan.
 *
 * @author Matthieu Fraissinet-Tachet
 *
 */
public class PatternSolution
implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1103197724078927303L;
    /*
     * A PatternSolution object represents a slogan generated by a pattern.
     * It contains the list of ChunkSolution object generated by the chunks of this pattern.
     * It has a reference to _model, the pattern it was created by.
     * It contains also a list of the ids of the constraints that it respects.
     * _hasBodyPart indicates if this slogan includes a body part. We don't need to know if
     * the body part corresponds to the constraint because this selection is done at a lower level
     * (when there is a body part constraint, then just body parts that correspond to this constraint
     * are generated).
     */

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

    /**
     * Generate, if possible (otherwise returns null) a new {@link PatternSolution} concatenating
     *  the {@link ChunkSolution} given as input to a copy of the current instance.
     *
     * @param resources
     * @param chunkSolution
     * @return
     */
    public PatternSolution generateNewPatternSolution(Resources resources,ChunkSolution chunkSolution)
    {
        PatternSolution output = null;

        PatternSolution sol = new PatternSolution(this);
        //if the chunkpartsolution and the current chunksolution are compatible a new ChunkSolution is generated
        //that concatenate the two elements
        if(sol.tryAddChunkSolution(resources,chunkSolution))
        {
            output = sol;
        }
        return output;
    }

    /**
     * Try to concatenate the {@link ChunkSolution} item given as input to the current instance.
     * The output is a boolean saying if the operation works.
     * @param resources
     * @param chunkSolution
     * @return
     */
    public boolean tryAddChunkSolution(Resources resources, ChunkSolution chunkSolution)
    {
        //returns true if it is possible to add this chunksolution to the current PatternSolution.
        //In this case, the chunkSolution instance is concatenated to the current PatternSolution
        //instance.

        if(_hasBodyPart && chunkSolution.hasBodyPart() && resources.hasBodyPartConstraint())
        {
            return false;
        }

        //look if the _constraints ids are compatible, ie if the intersection of the constraints is empty
        List<Integer> copyOfNewConstraintIds = new ArrayList<>(chunkSolution.getConstraintIds());
        copyOfNewConstraintIds.retainAll(this._constraintIds);

        if(copyOfNewConstraintIds.size()==0)
        {
            this._constraintIds.addAll(chunkSolution.getConstraintIds());
            this._chunkSolutions.add(chunkSolution);
            if(chunkSolution.hasBodyPart())
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

    /**
     * concatenates a list of chunkSolutions to a list of pattern solutions, which is to say tries
     * to add each chunkSolutions at the end of the chunkSolution list of each patternSolutions.
     * @param resources
     * @param patternSolutions
     * @param chunkSolutions
     * @return
     */
    public static List<PatternSolution> concatenate(Resources resources,List<PatternSolution> patternSolutions, List<ChunkSolution> chunkSolutions)
    {
        //concatenates a list of chunkSolutions to a list of pattern solutions, which is to say tries
        //to add each chunkSolutions at the end of the chunkSolution list of each patternSolutions.
        //If it functions the new generated patternSolution is added to the output list.
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

    public boolean hasSuggestedWordConstraint()
    {
        //say if a constraint over the suggested words is respected in this patternSolution
        return this._constraintIds.size()>0;
    }

    /**
     * Extract the text value corresponding to this {@link PatternSolution} instance. Returns a
     * generated slogan !
     * @return
     */
    public String generateText()
    {
        //generate the text of the slogan
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
