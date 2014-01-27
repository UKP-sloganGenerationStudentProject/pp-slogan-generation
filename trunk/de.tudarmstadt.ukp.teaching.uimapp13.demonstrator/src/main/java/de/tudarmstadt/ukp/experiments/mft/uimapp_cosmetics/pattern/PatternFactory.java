package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.PatternGenerator.Resources;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.ChunkHeader;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.ChunkHeader.ChunkType;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.ChunkIndex;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern.ChunkOccurrence;

public class PatternFactory
{


    /*
     * pattern information
     */
    Hashtable<String,Pattern> _patterns;
    ChunkIndex _chunkIndex;
    Pattern _currentPattern;
    ChunkOccurrence _currentChunkOccurrence;
    ChunkType _currentChunkType;
    String _currentPatternValue;
    boolean _isCurrentPattern;




    public PatternFactory()
    {
        _patterns = new Hashtable<String, Pattern>();
        _chunkIndex = new ChunkIndex();
        _currentPattern = new Pattern();
        _currentChunkOccurrence = new ChunkOccurrence();
        _currentPatternValue = "";
        _isCurrentPattern = false;
    }


    public void startNewChunk(ChunkType type)
    {
        _currentChunkOccurrence = ChunkOccurrence.createChunkOccurrence(type);
        _currentChunkType = type;
    }

    public void addPartToChunk(ChunkPart chunkPart)
    {
        //get or add the corresponding part from/to the chunk part index
        _currentChunkOccurrence.addChunkPart(chunkPart);
    }

    public void finishChunk(Resources resources)
    {
        _currentChunkOccurrence.generateInformation(resources);

        //generate the ChunkHeader
        ChunkHeader header = ChunkHeader.createChunkHeader(_currentChunkType);
        header.generateHeader(_currentChunkOccurrence);
        ChunkHeader headerInIndex = _chunkIndex.addHeader(header);
        headerInIndex.addOccurrence(_currentChunkOccurrence);
        _currentChunkOccurrence.setHeader(headerInIndex);
        if(_isCurrentPattern)
        {
            _currentPattern.addElement(_currentChunkOccurrence);
        }
    }


    public void startNewPattern()
    {
        _currentPattern = new Pattern();
        _isCurrentPattern = true;
    }


    public void finishPattern()
    {

        _currentPattern.addValueOccurrence(_currentPatternValue);

        //retrieve the id of the pattern
        String patternId = _currentPattern.getId();

        Pattern simiPattern = _patterns.get(patternId);

        if(simiPattern != null)
        {
            simiPattern.addOccurrence(_currentPattern);
            simiPattern.generateInformations();
        }
        else
        {
            _currentPattern.generateInformations();
            _patterns.put(patternId,_currentPattern);
        }

        _isCurrentPattern = false;

    }

    public void setPatternValue( String value)
    {
        _currentPatternValue = value;
    }

    public void resetTheCacheData()
    {
        for(ChunkHeader header : _chunkIndex.getPatternElements())
        {
            header.resetCache();
        }
    }


    public List<String> generateSlogans(Resources resources, int nbrOfSlogans)
    {

        resetTheCacheData();

        //output all the parameters
        System.out.println("Pattern Generation STARTS... ");
        System.out.println("\nParameters :");

        System.out.println("\tproductName : "+resources.getProductName());

        System.out.print("\tSelectionned Patterns: ");
        for(String pattern : resources.getPatternsToGenerate())
        {
            System.out.print(pattern);
            System.out.print(" ; ");
        }

        System.out.println();

        System.out.print("\tSelectionned part of body : "+resources.getSelectedBodyPart());
        System.out.println();

        System.out.print("\tSuggested words : ");
        for(String word : resources.getSuggestedWords())
        {
            System.out.print(word);
            System.out.print(" ; ");
        }

        System.out.println();
        System.out.println("WARNING : the parameters for the generation are not taken into account yet");



        List<Pattern> filteredPatterns = new ArrayList<>();

        //filter the patterns and keep just those who correspond to the creteria

        for(Pattern pattern : _patterns.values())
        {

            if(resources.getPatternsToGenerate().size()>0)
            {
                if(!resources.getPatternsToGenerate().contains(pattern.getPatternType()))
                {
                    //if the pattern types to generate are precised and if the current pattern
                    //doesn't correspond to one of those types, don't generate the slogans
                    // associated to this pattern
                    continue;
                }
            }

            /* NOW THE BODYPART FILTERING IS DONE IN THE PATTERN CLASS

            if(!resources.getSelectedBodyPart().equals(""))
            {
                if(!pattern.isBodyPart())
                {
                    //if a body part has been precised we don't generate the slogans for the
                    //patterns that don't have body parts inside
                    continue;
                }
            }
            */

            filteredPatterns.add(pattern);
        }


        int slogToGenPerPattern = 1;
        boolean randomize = false;
        List<Integer> randomIndices = null;

        if(nbrOfSlogans < filteredPatterns.size())
        {
            randomize = true;
            slogToGenPerPattern = 1;
            randomIndices = randomIndices = Utils.getDistinctRandomIndices(_patterns.size(), nbrOfSlogans);
        }
        else
        {
            randomize = false;
            slogToGenPerPattern = nbrOfSlogans / filteredPatterns.size() + 1;
        }

        List<String> output = new ArrayList<String>();
        int incr = -1;

        for(Pattern pattern : _patterns.values())
        {

            if(randomize)
            {
                incr = incr + 1;


                if(!randomIndices.contains(new Integer(incr)))
                {
                    continue;
                }
            }

            output.addAll(pattern.generateSlogans(resources,slogToGenPerPattern));

        }

        output = Utils.getSubList(output, nbrOfSlogans);

        return output;
    }



    public String generateSlogansTest(Resources resources)
    {

        StringBuilder output = new StringBuilder();

        //output all the parameters
        System.out.println("Pattern Generation STARTS... ");
        System.out.println("\nParameters :");

        System.out.println("\tproductName : "+resources.getProductName());

        System.out.print("\tSelectionned Patterns: ");
        for(String pattern : resources.getPatternsToGenerate())
        {
            System.out.print(pattern);
            System.out.print(" ; ");
        }

        System.out.println();


        System.out.print("\tSelectionned part of body : "+resources.getSelectedBodyPart());
        System.out.println();


        System.out.print("\tSuggested words : ");
        for(String word : resources.getSuggestedWords())
        {
            System.out.print(word);
            System.out.print(" ; ");
        }

        System.out.println();
        System.out.println("WARNING : the parameters for the generation are not taken into account yet");



        for(Pattern pattern : _patterns.values())
        {
            if(resources.getPatternsToGenerate().size()>0)
            {
                if(!resources.getPatternsToGenerate().contains(pattern.getPatternType()))
                {
                    //if the pattern types to generate are precised and if the current pattern
                    //doesn't correspond to one of those types, don't generate the slogans
                    // associated to this pattern
                    continue;
                }
            }

            /* NOW THE BODYPART FILTERING IS DONE IN THE PATTERN CLASS

            if(!resources.getSelectedBodyPart().equals(""))
            {
                if(!pattern.isBodyPart())
                {
                    //if a body part has been precised we don't generate the slogans for the
                    //patterns that don't have body parts inside
                    continue;
                }
            }
            */


            StringBuilder partialOutput = new StringBuilder();

            partialOutput.append(pattern.toString()+"\n");

            partialOutput.append("****generated :\n");

            for(String slogan : pattern.generateSlogans(resources,-1))
            {
                partialOutput.append("\t"+slogan);
                partialOutput.append("\n");
            }

            output.append(partialOutput.toString());
        }

        return output.toString();



    }

    public Object getChunkIndex()
    {
        return _chunkIndex;
    }

    public int getNbrOfPatterns()
    {
        return _patterns.size();
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder();
        output.append("##################PATTERNS##################\n\n");
        for(Pattern pattern : _patterns.values())
        {
            output.append(pattern.toString());
            output.append("\n");
        }

        output.append("##################ElementIndex###############\n\n");
        output.append(_chunkIndex.toString());

        return output.toString();
    }


}
