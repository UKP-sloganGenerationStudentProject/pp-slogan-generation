package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern;

import java.io.Serializable;
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
    implements Serializable
{

    private static final long serialVersionUID = -7074314512406365028L;

    /*
     * pattern information
     */
    Hashtable<String, Pattern> _patterns;
    ChunkIndex _chunkIndex;
    Pattern _currentPattern;
    ChunkOccurrence _currentChunkOccurrence;
    ChunkType _currentChunkType;
    String _currentPatternValue;
    boolean _isCurrentPattern;

    public PatternFactory()
    {
        this._patterns = new Hashtable<String, Pattern>();
        this._chunkIndex = new ChunkIndex();
        this._currentPattern = new Pattern();
        this._currentChunkOccurrence = new ChunkOccurrence();
        this._currentPatternValue = "";
        this._isCurrentPattern = false;
    }

    public void startNewChunk(final ChunkType type)
    {
        this._currentChunkOccurrence = ChunkOccurrence.createChunkOccurrence(type);
        this._currentChunkType = type;
    }

    public void addPartToChunk(final ChunkPart chunkPart)
    {
        // get or add the corresponding part from/to the chunk part index
        this._currentChunkOccurrence.addChunkPart(chunkPart);
    }

    public void finishChunk(final Resources resources)
    {
        this._currentChunkOccurrence.generateInformation(resources);

        // generate the ChunkHeader
        final ChunkHeader header = ChunkHeader.createChunkHeader(this._currentChunkType);
        header.generateHeader(this._currentChunkOccurrence);
        final ChunkHeader headerInIndex = this._chunkIndex.addHeader(header);
        headerInIndex.addOccurrence(this._currentChunkOccurrence);
        this._currentChunkOccurrence.setHeader(headerInIndex);
        if (this._isCurrentPattern) {
            this._currentPattern.addElement(this._currentChunkOccurrence);
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
        for (final ChunkHeader header : this._chunkIndex.getPatternElements()) {
            header.resetCache();
        }
    }

    public List<String> generateSlogans(final Resources resources, final int nbrOfSlogans)
    {

        this.resetTheCacheData();

        // output all the parameters
        System.out.println("Pattern Generation STARTS... ");
        System.out.println("\nParameters :");

        System.out.println("\tproductName : " + resources.getProductName());

        System.out.print("\tSelectionned Patterns: ");
        for (final String pattern : resources.getPatternsToGenerate()) {
            System.out.print(pattern);
            System.out.print(" ; ");
        }

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

        for (final Pattern pattern : this._patterns.values()) {

            if (resources.getPatternsToGenerate().size() > 0) {
                if (!resources.getPatternsToGenerate().contains(pattern.getPatternType())) {
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

            filteredPatterns.add(pattern);
        }

        int slogToGenPerPattern = 1;
        boolean randomize = false;
        List<Integer> randomIndices = null;

        if (nbrOfSlogans < filteredPatterns.size()) {
            randomize = true;
            slogToGenPerPattern = 1;
            randomIndices = randomIndices = Utils.getDistinctRandomIndices(this._patterns.size(),
                    nbrOfSlogans);
        }
        else {
            randomize = false;
            slogToGenPerPattern = nbrOfSlogans / filteredPatterns.size() + 1;
        }

        List<String> output = new ArrayList<String>();
        int incr = -1;

        for (final Pattern pattern : filteredPatterns) {

            if (randomize) {
                incr = incr + 1;

                if (!randomIndices.contains(new Integer(incr))) {
                    continue;
                }
            }

            output.addAll(pattern.generateSlogans(resources, slogToGenPerPattern));

        }

        output = Utils.getSubList(output, nbrOfSlogans);

        return output;
    }

    public String generateSlogansTest(final Resources resources)
    {

        final StringBuilder output = new StringBuilder();

        // output all the parameters
        System.out.println("Pattern Generation STARTS... ");
        System.out.println("\nParameters :");

        System.out.println("\tproductName : " + resources.getProductName());

        System.out.print("\tSelectionned Patterns: ");
        for (final String pattern : resources.getPatternsToGenerate()) {
            System.out.print(pattern);
            System.out.print(" ; ");
        }

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

        for (final Pattern pattern : this._patterns.values()) {
            if (resources.getPatternsToGenerate().size() > 0) {
                if (!resources.getPatternsToGenerate().contains(pattern.getPatternType())) {
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

        output.append("##################ElementIndex###############\n\n");
        output.append(this._chunkIndex.toString());

        return output.toString();
    }

}
