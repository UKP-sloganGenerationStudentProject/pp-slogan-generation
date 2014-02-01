package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.pattern;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.Utils;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.Chunk;

public class Pattern
{
    private final ArrayList<Chunk> _elementList;
    private final ArrayList<String> _valueOccurrences;
    private String _patternType;
    private boolean _hasConstraint;
    private boolean _haveConstraintsBeenChecked;


    public Pattern()
    {
        _elementList = new ArrayList<Chunk>();
        _valueOccurrences = new ArrayList<String>();
        _patternType = "UNDEFINED";
        _hasConstraint = false;
        _haveConstraintsBeenChecked = false;
    }


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

    public void addElement(Chunk el)
    {
        _elementList.add(el);
    }

    public void addValueOccurrence(String value)
    {
        _valueOccurrences.add(value);
    }

    public ArrayList<String> getValueOccurrences()
    {
        return _valueOccurrences;
    }

    public String getId()
    {
        String id = "";

        for(Chunk elem: _elementList)
        {
            id = id + elem.getHeaderId() + "_";
        }

        return id;
    }

    public List<Chunk> getPatternElementList()
    {
        return _elementList;
    }


    public void addOccurrence(Pattern other)
    {
        if(!getId().equals(other.getId()))
        {
            return;
        }

        _valueOccurrences.addAll(other.getValueOccurrences());
    }


    public String getElementIdAt(int ind)
    {
        return _elementList.get(ind).getHeaderId();
    }

    public List<String> generateSlogans(Resources resources, int numberOfSlogans)
    {
        List<PatternSolution> patternSolutions = generatePatternSolutions(resources, numberOfSlogans);
        List<String> slogans = new ArrayList<>();

        for(PatternSolution ps : patternSolutions)
        {
            slogans.add(ps.generateText());
        }
        return slogans;
    }

    public List<PatternSolution> generatePatternSolutions(Resources resources,int numberOfSlogans)
    {

        List<PatternSolution> newPatternSolutions = new ArrayList<>();
        PatternSolution initialPatternSolution = new PatternSolution(this);
        newPatternSolutions.add(initialPatternSolution);

        for(Chunk element : _elementList)
        {
            List<PatternSolution> newPatternSolutionsTemp = new ArrayList<>();
            for(Chunk equiv : element.getSimilarChunkOccurrences(resources))
            {
                newPatternSolutionsTemp.addAll(PatternSolution.concatenate(resources,newPatternSolutions,equiv.generateChunkSolutions(resources, element)));
            }
            newPatternSolutions = newPatternSolutionsTemp;
        }

        List<PatternSolution> solutionsWithConstraints = new ArrayList<>();
        if(resources.hasConstraints() || resources.hasBodyPartConstraint())
        {
            for(PatternSolution solution : newPatternSolutions)
            {
                if(resources.hasConstraints() && !solution.hasConstraint())
                {
                    continue;
                }

                if(resources.hasBodyPartConstraint() && !solution.hasBodyPart())
                {
                    continue;
                }

                solutionsWithConstraints.add(solution);
            }
        }
        else
        {
            solutionsWithConstraints = newPatternSolutions;
        }
        return Utils.getSubList(solutionsWithConstraints, numberOfSlogans);

    }

    public void checkForConstraints(Resources resources)
    {
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

    public boolean hasConstraint()
    {
        return _hasConstraint;
    }

    public void releaseConstraints()
    {
        _hasConstraint = false;
        _haveConstraintsBeenChecked = false;
    }

    @Override
    public boolean equals(Object obj)
    {
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
            output.append(elem.getHeaderId());
            output.append(" _ ");
        }

        output.append(" [hasConstraint:");
        output.append(_hasConstraint);
        output.append("]");
        output.append(" [haveConstraintsBeenChecks:");
        output.append(_haveConstraintsBeenChecked);
        output.append("]");

        output.append("\n");

        for(String value : _valueOccurrences)
        {
            output.append(value);
            output.append("\n");
        }

        return output.toString();
    }
}