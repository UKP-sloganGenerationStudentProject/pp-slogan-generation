package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.Chunk;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunk.ChunkGeneric;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.chunkPart.ChunkPartGeneric;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.index.Index;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.pattern.Pattern;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

public class PatternFactory
    implements Serializable
{

    private static final long serialVersionUID = -7074314512406365028L;

    /*
     * pattern information
     */
    Hashtable<String, Pattern> _patterns;
    Index<ChunkGeneric> _chunkIndex;
    Index<ChunkPartGeneric> _chunkPartIndex;
    Pattern _currentPattern;
    Chunk _currentChunkOccurrence;
    ChunkType _currentChunkType;
    String _currentPatternValue;
    boolean _isCurrentPattern;

    public PatternFactory()
    {
        this._patterns = new Hashtable<String, Pattern>();
        this._chunkIndex = new Index<ChunkGeneric>();
        this._chunkPartIndex = new Index<ChunkPartGeneric>();
        this._currentPattern = new Pattern();
        this._currentChunkOccurrence = new Chunk();
        this._currentPatternValue = "";
        this._isCurrentPattern = false;
    }

    public void startNewChunk(final ChunkType type)
    {
        this._currentChunkOccurrence = Chunk.createChunkOccurrence(type);
        this._currentChunkType = type;
    }

    public void addPartToChunk(ChunkPartType type,final ChunkPart chunkPart)
    {
        // get or add the corresponding part from/to the chunk part index
        final ChunkPartGeneric header = ChunkPartGeneric.createChunkHeader(type, chunkPart.getSemanticValue());
        final ChunkPartGeneric headerInIndex = _chunkPartIndex.addElement(header);
        headerInIndex.addOccurrence(chunkPart);
        chunkPart.setHeader(headerInIndex);
        chunkPart.setContainingChunk(_currentChunkOccurrence);
        this._currentChunkOccurrence.addChunkPart(chunkPart);
    }

    public void finishChunk(final Resources resources)
    {
        this._currentChunkOccurrence.generateInformation(resources);

        // generate the ChunkHeader
        final ChunkGeneric header = ChunkGeneric.createChunkHeader(this._currentChunkType);
        header.generateHeader(this._currentChunkOccurrence);
        final ChunkGeneric headerInIndex = this._chunkIndex.addElement(header);
        headerInIndex.addOccurrence(this._currentChunkOccurrence);
        this._currentChunkOccurrence.setHeader(headerInIndex);
        if (this._isCurrentPattern) {
            this._currentPattern.addElement(this._currentChunkOccurrence);
            this._currentChunkOccurrence.setContainingPattern(this._currentPattern);
        }
    }

    public void startNewPattern()
    {
        this._currentPattern = new Pattern();
        this._isCurrentPattern = true;
    }

    public void finishPattern()
    {

        this._currentPattern.addValueOccurrence(this._currentPatternValue);

        // retrieve the id of the pattern
        final String patternId = this._currentPattern.getId();

        final Pattern simiPattern = this._patterns.get(patternId);

        if (simiPattern != null) {
            simiPattern.addOccurrence(this._currentPattern);
            simiPattern.generateInformations();
        }
        else {
            this._currentPattern.generateInformations();
            this._patterns.put(patternId, this._currentPattern);
        }

        this._isCurrentPattern = false;

    }

    public void setPatternValue(final String value)
    {
        this._currentPatternValue = value;
    }

    public void resetTheCacheData()
    {
        for (final ChunkGeneric header : this._chunkIndex.getElements()) {
            header.resetCache();
        }
    }

    public List<String> generateSlogans(final Resources resources, final int nbrOfSlogans)
    {

        this.resetTheCacheData();

        this.checkForConstraints(resources);

        // output all the parameters
        System.out.println("Pattern Generation STARTS... ");
        System.out.println("\nParameters :");

        System.out.println("\tproductName : " + resources.getProductName());

        System.out.print("\tSelectionned Patterns: "+ resources.getPatternToGenerate());
        System.out.println();

        System.out.println();

        System.out.print("\tSelectionned part of body : " + resources.getSelectedBodyPart());
        System.out.println();

        System.out.print("\tSuggested words : ");
        for (final String word : resources.getSuggestedWords()) {
            System.out.print(word);
            System.out.print(" ; ");
        }

        System.out.println();
        System.out
                .println("WARNING : the parameters for the generation are not taken into account yet");

        final List<Pattern> filteredPatterns = new ArrayList<>();

        // filter the patterns and keep just those who correspond to the creteria

        for (final Pattern pattern : this._patterns.values())
        {


            if(resources.hasConstraints() && !pattern.hasConstraint())
            {
                //this pattern doens't include one of the constraints
                continue;
            }

            if (!resources.getPatternToGenerate().equals("") && !resources.getPatternToGenerate().equals(PatternGenerator.DONT_CARE) ) {
                if (!resources.getPatternToGenerate().equals(pattern.getPatternType())) {
                    // if the pattern types to generate are precised and if the current pattern
                    // doesn't correspond to one of those types, don't generate the slogans
                    // associated to this pattern
                    continue;
                }
            }

            System.out.println("Selected patterns : ");
            System.out.println(pattern.toString());

            filteredPatterns.add(pattern);
        }


        /*
         * filter the pattern that can produce constraints over the selected words
         * and produce those constraints
         */



        Collections.shuffle(filteredPatterns);

        List<String> output = new ArrayList<>();

        /*
         * generate the patterns
         */
        int nbrPatterns = 0;
        for (final Pattern pattern : filteredPatterns)
        {
            nbrPatterns = nbrPatterns + 1;

            output.addAll(pattern.generateSlogans(resources, nbrOfSlogans));
            if(output.size()>nbrOfSlogans*nbrOfSlogans/1.3)
            {
                break;
            }

        }

        this.releaseConstraints();

        output = Utils.getSubList(output, nbrOfSlogans);

        return output;
    }

    public void releaseConstraints()
    {
        for(Pattern pattern : _patterns.values())
        {
            pattern.releaseConstraints();
        }
        for(ChunkGeneric header : _chunkIndex.getElements())
        {
            header.releaseConstraints();
        }
        for(ChunkPartGeneric header : _chunkPartIndex.getElements())
        {
            header.releaseConstraints();
        }
    }

    public void checkForConstraints(Resources resources)
    {
        if(resources.hasConstraints())
        {
            for(Pattern pattern : _patterns.values())
            {
                pattern.checkForConstraints(resources);
            }
        }
    }

    public String generateSlogansTest(final Resources resources)
    {

        final StringBuilder output = new StringBuilder();

        // output all the parameters
        System.out.println("Pattern Generation STARTS... ");
        System.out.println("\nParameters :");

        System.out.println("\tproductName : " + resources.getProductName());

        System.out.print("\tSelectionned Patterns: "+ resources.getPatternToGenerate());
        System.out.println();

        System.out.println();

        System.out.print("\tSelectionned part of body : " + resources.getSelectedBodyPart());
        System.out.println();

        System.out.print("\tSuggested words : ");
        for (final String word : resources.getSuggestedWords()) {
            System.out.print(word);
            System.out.print(" ; ");
        }

        System.out.println();
        System.out
                .println("WARNING : the parameters for the generation are not taken into account yet");

        for (final Pattern pattern : this._patterns.values())
        {
            if (!resources.getPatternToGenerate().equals("") && !resources.getPatternToGenerate().equals(PatternGenerator.DONT_CARE) ) {
                if (!resources.getPatternToGenerate().equals(pattern.getPatternType())) {
                    // if the pattern types to generate are precised and if the current pattern
                    // doesn't correspond to one of those types, don't generate the slogans
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

            final StringBuilder partialOutput = new StringBuilder();

            partialOutput.append(pattern.toString() + "\n");

            partialOutput.append("****generated :\n");

            for (final String slogan : pattern.generateSlogans(resources, -1)) {
                partialOutput.append("\t" + slogan);
                partialOutput.append("\n");
            }

            output.append(partialOutput.toString());
        }

        return output.toString();

    }

    public Object getChunkIndex()
    {
        return this._chunkIndex;
    }

    public int getNbrOfPatterns()
    {
        return this._patterns.size();
    }

    @Override
    public String toString()
    {
        final StringBuilder output = new StringBuilder();
        output.append("##################PATTERNS##################\n\n");
        for (final Pattern pattern : this._patterns.values()) {
            output.append(pattern.toString());
            output.append("\n");
        }

        output.append("\n\n##################ChunkIndex###############\n\n");
        output.append(this._chunkIndex.toString());

        output.append("\n\n##################ChunkPartIndex###############\n\n");
        output.append(this._chunkPartIndex.toString());



        return output.toString();
    }

}
