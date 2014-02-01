package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPattern.Chunk;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPattern.NounChunk;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

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
        List<PatternSolution> noBodyPart = new ArrayList<>();
        List<PatternSolution> withBodyPart = new ArrayList<>();
        PatternSolution initPatternSolution1 = new PatternSolution(this);
        PatternSolution initPatternSolution2 = new PatternSolution(this);
        noBodyPart.add(initPatternSolution1);
        withBodyPart.add(initPatternSolution2);

        //tells us if the slogans have to include a specific bodypart
        boolean mustBeBodyPart = !resources.getSelectedBodyPart().equals(PatternGenerator.NO_BODY_PART) && !resources.getSelectedBodyPart().equals("");

        boolean isNoBodyPartValid = true;
        boolean isWithBodyPartValid = true;

        boolean hasBeenBodyPartOnceUsed = false;

        if(_valueOccurrences.contains("Choose the rainbow colors for your nails"))
        {
            System.out.println("STOP HERE !");
        }

        for(Chunk elem : _elementList)
        {
            List<PatternSolution> noBodyPartTEMP = new ArrayList<>();
            List<PatternSolution> withBodyPartTEMP = new ArrayList<>();

            boolean isNoBodyPartTEMPValid = false;
            boolean isWithBodyPartTEMPValid = false;

            for(Chunk occ : elem.getSimilarChunkOccurrences(resources))
            {
                if(occ.getChunkType().equals(ChunkType.NC))
                {
                    NounChunk nounOcc = (NounChunk) occ;
                    if(nounOcc.isBodyPart())
                    {
                        if(mustBeBodyPart)
                        {

                            if(isNoBodyPartValid)
                            {
                                withBodyPartTEMP.addAll(PatternSolution.concatenate(noBodyPart, nounOcc.generateChunkSolutions(resources,elem)));
                                isWithBodyPartTEMPValid = true;
                                hasBeenBodyPartOnceUsed = true;
                            }

                            /*
                            if(nounOcc.getBodyPartName().equals(resources.getSelectedBodyPart()))
                            {
                                //we use this occurrence because it contains exactly the bodypart we are looking for
                                // we concatenate it to the slogan parts that don't have any body part
                                // because we want only one slogan per bodypart
                                if(isNoBodyPartValid)
                                {
                                    withBodyPartTEMP.addAll(Utils.concatenate(noBodyPart, nounOcc.generateSloganParts(resources)));
                                    isWithBodyPartTEMPValid = true;
                                }
                            }
                            else
                            {
                                //we don't use this occurrence because it corresponds to another part of the body
                            }
                            */
                        }
                        else
                        {
                            //we don't want any body part so we don't use this occurrence
                        }

                    }
                    else
                    {
                        //this is not a body part so we can use it anywayocc
                        if(isNoBodyPartValid)
                        {
                            noBodyPartTEMP.addAll(PatternSolution.concatenate(noBodyPart, occ.generateChunkSolutions(resources,elem)));
                            isNoBodyPartTEMPValid = true;
                        }
                        if(mustBeBodyPart)
                        {
                            if(isWithBodyPartValid && hasBeenBodyPartOnceUsed)
                            {
                                //if we are looking for slogans with body parts, we update also the table
                                // used to create the slogans with bodypart included
                                withBodyPartTEMP.addAll(PatternSolution.concatenate(withBodyPart, occ.generateChunkSolutions(resources,elem)));
                                isWithBodyPartTEMPValid = true;
                            }
                        }

                    }
                }
                else
                {
                    //this is not a body part so we can use it anyway
                    if(isNoBodyPartValid)
                    {
                        noBodyPartTEMP.addAll(PatternSolution.concatenate(noBodyPart, occ.generateChunkSolutions(resources,elem)));
                        isNoBodyPartTEMPValid = true;
                    }
                    if(isWithBodyPartValid)
                    {
                        if(mustBeBodyPart && hasBeenBodyPartOnceUsed)
                        {
                            //if we are looking for slogans with body parts, we update also the table
                            // used to create the slogans with bodypart included
                            withBodyPartTEMP.addAll(PatternSolution.concatenate(withBodyPart, occ.generateChunkSolutions(resources,elem)));
                            isWithBodyPartTEMPValid = true;
                        }
                    }
                }
            }



            if(isWithBodyPartTEMPValid)
            {
                withBodyPart = withBodyPartTEMP;
            }
            if(isNoBodyPartTEMPValid)
            {
                 noBodyPart = noBodyPartTEMP;
            }

            isWithBodyPartValid = isWithBodyPartTEMPValid;
            isNoBodyPartValid = isNoBodyPartTEMPValid;
        }

        List<PatternSolution> slogansSublist = new ArrayList<>();

        if(isWithBodyPartValid && mustBeBodyPart)
        {
            slogansSublist = withBodyPart;
        }

        if(isNoBodyPartValid && !mustBeBodyPart)
        {
            slogansSublist = noBodyPart;
        }

        slogansSublist = Utils.getSubList(slogansSublist, numberOfSlogans);

        return slogansSublist;
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