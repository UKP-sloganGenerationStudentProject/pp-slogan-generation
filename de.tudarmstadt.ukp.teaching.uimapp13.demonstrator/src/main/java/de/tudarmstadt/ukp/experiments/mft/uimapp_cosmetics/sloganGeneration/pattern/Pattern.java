package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.pattern;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Utils;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.Chunk;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.ChunkGeneric;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.ChunkSolution;

/**
 *
 * this class represents a pattern. It is identified by its id that depend on the {@link ChunkGeneric}
 * instances associated to the {@link Chunk} instances it is made of.
 * A pattern is made of chunks, each {@link Chunk} is like a generator of a part of the slogan (from
 * the left to the right).
 *
 * @author Matthieu Fraissinet-Tachet
 *
 */
public class Pattern
implements Serializable
{
    private static final long serialVersionUID = -6075369273605316813L;
    //elements this pattern are made of
    private final ArrayList<Chunk> _elementList;
    //all the slogan from the slogan source file that correspond to this pattern
    private final ArrayList<String> _sloganOccurrences;

    private String _patternType;

    //indicates if the pattern is able to generate a slogan that includes one of the suggested words
    private boolean _hasConstraint;
    private boolean _haveConstraintsBeenChecked;


    public Pattern()
    {
        _elementList = new ArrayList<Chunk>();
        _sloganOccurrences = new ArrayList<String>();
        _patternType = "UNDEFINED";
        _hasConstraint = false;
        _haveConstraintsBeenChecked = false;
    }


    /**
     * the type of a pattern is the concatenation of the types of its chunks
     * the pattern type is the type that is selected by the users ex : NC_VC_NC_
     */
    public void generatePatternType()
    {

        StringBuilder type = new StringBuilder();

        for(Chunk occ : _elementList)
        {
            type.append(occ.getChunkType().toString());
            type.append("_");
        }
        _patternType = type.toString();
    }

    public String getPatternType()
    {
        return _patternType;
    }


    public void generateInformations()
    {
        generatePatternType();
    }

    /**
     * add an element at the end of the pattern
     * @param el
     */
    public void addElement(Chunk el)
    {
        _elementList.add(el);
    }

    /**
     * add a slogan that respects this pattern (from which this pattern was derived)
     * this belongs to the pattern generation phase
     * @param value
     */
    public void addSloganOccurrence(String value)
    {
        _sloganOccurrences.add(value);
    }

    public ArrayList<String> getSloganOccurrences()
    {
        return _sloganOccurrences;
    }

    /**
     * the id identifies its pattern
     *  if 2 patterns have the same id they have to be merged
     *  as they can generate the same slogans
     * @return
     */
    public String getId()
    {

        String id = "";

        for(Chunk elem: _elementList)
        {
            id = id + elem.getGenericId() + "_";
        }

        return id;
    }

    /**
     * get the list of the chunk which the pattern is made of
     * @return
     */
    public List<Chunk> getPatternElementList()
    {
        return _elementList;
    }


    /**
     * merge 2 patterns that have the same id
     * ie they have their chunnks have the same ids
     * @param other
     */
    public void addOccurrence(Pattern other)
    {

        if(!getId().equals(other.getId()))
        {
            return;
        }

        _sloganOccurrences.addAll(other.getSloganOccurrences());
    }


    public String getElementIdAt(int ind)
    {
        return _elementList.get(ind).getGenericId();
    }

    /**
     * generate slogans
     * @param resources
     * @param numberOfSlogans
     * @return
     */
    public List<String> generateSlogans(Resources resources, int numberOfSlogans)
    {
        //generate slogans
        //retrievew the PatternSolution objects that encapsulate slogan solutions for this pattern
        List<PatternSolution> patternSolutions = generatePatternSolutions(resources, numberOfSlogans);
        List<String> slogans = new ArrayList<>();

        //convert these solutions into Strings
        for(PatternSolution ps : patternSolutions)
        {
            slogans.add(ps.generateText());
        }
        return slogans;
    }

    /**
     * generate the slogans in the for of {@link PatternSolution}. A slogan can be derictly derived
     * from a {@link PatternSolution} instance.
     * @param resources
     * @param numberOfSlogans
     * @return
     */
    public List<PatternSolution> generatePatternSolutions(Resources resources,int numberOfSlogans)
    {

        //generate the slogans in the form of PatternSolution

        List<PatternSolution> newPatternSolutions = new ArrayList<>();

        //we initialize the list of solution with an empty solution (because the solutions will
        //be incrementally constructed by a sort of a concatenation
        PatternSolution initialPatternSolution = new PatternSolution(this);
        newPatternSolutions.add(initialPatternSolution);

        for(Chunk element : _elementList)
        {
            List<PatternSolution> newPatternSolutionsTemp = new ArrayList<>();
            for(Chunk equiv : element.getSimilarChunkOccurrences(resources))
            {
                //for each chunk the pattern is made of, we extract all the chunk solutions and concatenate it to
                //the partial solutions that have been constructed so far
                List<ChunkSolution> chunkSolutions = equiv.generateChunkSolutions(resources, element);
                newPatternSolutionsTemp.addAll(PatternSolution.concatenate(resources,newPatternSolutions,chunkSolutions));
            }
            newPatternSolutions = newPatternSolutionsTemp;
        }

        //check if the generated slogans respect the requirements
        //nb : the pattern type requirement is necessarily respected by the pattern itself
        //(other there would have been no generation)

        List<PatternSolution> solutionsWithConstraints = new ArrayList<>();
        if(resources.hasSuggestedWordConstraints() || resources.hasBodyPartConstraint())
        {
            //if no requirement has to be respected, we don't do anything
            for(PatternSolution solution : newPatternSolutions)
            {
                if(resources.hasSuggestedWordConstraints() && !solution.hasSuggestedWordConstraint())
                {
                    //if there is a constraint over the suggested words and that this constraint
                    //is not respected, we don't keep this solution
                    continue;
                }

                if(resources.hasBodyPartConstraint() && !solution.hasBodyPart())
                {
                    //if there is a constraint over the body part and that it is not respected by
                    //the solution, we don't keep it
                    continue;
                }

                //otherwise we keep the solution
                solutionsWithConstraints.add(solution);
            }
        }
        else
        {
            solutionsWithConstraints = newPatternSolutions;
        }

        return Utils.getSubList(solutionsWithConstraints, numberOfSlogans);
    }


    /**
     * process the constraints over the current pattern (ie. check if at least one element
     * of the contained chunks can generate chunks containing at least one suggested word).
     * @param resources
     */
    public void checkForConstraints(Resources resources)
    {
        //a pattern can generate constraints if at least one of his chunks can generate a constraint
        if(!_haveConstraintsBeenChecked)
        {
            for(Chunk chunk : _elementList)
            {
                chunk.checkForGenericConstraints(resources);
                _hasConstraint = _hasConstraint || chunk.hasGenericConstraint();
            }
            _haveConstraintsBeenChecked = true;
        }
    }

    /**
     * return true if this object has constraints
     * call checkForConstraints() before using this.
     * @return
     */
    public boolean hasSuggestedWordConstraint()
    {
        return _hasConstraint;
    }

    /**
     * reset the chunkGeneric as if the constraints hadn't been processed (undo checkForConstraints())
     */
    public void releaseConstraints()
    {
        _hasConstraint = false;
        _haveConstraintsBeenChecked = false;
    }

    @Override
    public boolean equals(Object obj)
    {
        // two Pattern object are said equal if they have the same id, which mean to say if they have
        //chunks that have the same chunkGeneric which is to say if thay can generate the same slogans

        if(this == obj)
        {
            return true;
        }
        if(!(obj instanceof Pattern))
        {
            return false;
        }

        Pattern patt = (Pattern) obj;

        return getId().equals(patt.getId());
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();

        for(Chunk elem: _elementList)
        {
            output.append(elem.getGenericId());
            output.append(" _ ");
        }

        output.append(" [hasConstraint:");
        output.append(_hasConstraint);
        output.append("]");
        output.append(" [haveConstraintsBeenChecks:");
        output.append(_haveConstraintsBeenChecked);
        output.append("]");

        output.append("\n");

        for(String value : _sloganOccurrences)
        {
            output.append(value);
            output.append("\n");
        }

        return output.toString();
    }
}