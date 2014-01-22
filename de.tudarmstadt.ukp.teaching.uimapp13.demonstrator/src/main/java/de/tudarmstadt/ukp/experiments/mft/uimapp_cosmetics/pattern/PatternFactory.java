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
        _currentChunkOccurrence = new ChunkOccurrence();
        _currentChunkType = type;
    }

    public void addPartToChunk(ChunkPart chunkPart)
    {
        //get or add the corresponding part from/to the chunk part index
        _currentChunkOccurrence.addChunkPart(chunkPart);
    }

    public void finishChunk()
    {
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
        }
        else
        {
            _patterns.put(patternId,_currentPattern);
        }

        _isCurrentPattern = false;

    }

    public void setPatternValue( String value)
    {
        _currentPatternValue = value;
    }


    public List<String> generatePatterns(Resources resources)
    {

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

        System.out.print("\tSelectionned parts of body : ");
        for(String pob : resources.getSelectedPartsOfBody())
        {
            System.out.print(pob);
            System.out.print(" ; ");
        }
        System.out.println();


        System.out.print("\tSuggested words : ");
        for(String word : resources.getSuggestedWords())
        {
            System.out.print(word);
            System.out.print(" ; ");
        }

        System.out.println();
        System.out.println("WARNING : the parameters for the generation are not taken into account yet");


        List<String> output = new ArrayList<String>();

        int incr = -1;

        List<Integer> randomIndices = Utils.getDistinctRandomIndices(_patterns.size(), 4);

        for(Pattern pattern : _patterns.values())
        {
            incr = incr + 1;

            if(!randomIndices.contains(new Integer(incr)))
            {
                continue;
            }
            //output the created slogans

            /*
            output.append(pattern.getId());
            output.append("\n");
            output.append("\n");

            output.append("Models :");
            output.append("\n");
            for(String patternOccurrence : pattern.getValueOccurrences())
            {
                output.append("\t"+patternOccurrence);
                output.append("\n");
            }

            output.append("\n");


            output.append("Generated patterns :");
            output.append("\n");
            output.append("\n");

            */

            output.addAll(pattern.generateSlogans(resources,5));

//            output.append("\n");
        }

        return output;
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
