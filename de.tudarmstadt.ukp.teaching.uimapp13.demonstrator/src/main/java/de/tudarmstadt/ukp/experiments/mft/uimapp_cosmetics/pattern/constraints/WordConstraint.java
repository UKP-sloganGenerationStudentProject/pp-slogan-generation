package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.constraints;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkPartType;
import de.tudarmstadt.ukp.lmf.api.Uby;
import de.tudarmstadt.ukp.lmf.exceptions.UbyInvalidArgumentException;
import de.tudarmstadt.ukp.lmf.model.core.LexicalEntry;
import de.tudarmstadt.ukp.lmf.model.core.Lexicon;
import de.tudarmstadt.ukp.lmf.model.core.Sense;
import de.tudarmstadt.ukp.lmf.model.enums.EPartOfSpeech;
import de.tudarmstadt.ukp.lmf.model.meta.SemanticLabel;

public class WordConstraint
{
    String _takenValue;
    String _lemma;
    ChunkPartType _chunkPartType;
    String _semantic;


    public WordConstraint()
    {
        this._takenValue = "";
        this._lemma = "";
        this._chunkPartType = ChunkPartType.UNDEFINED;
        this._semantic = "";
    }


    public WordConstraint(String takenValue, String lemma, ChunkPartType chunkPartType,
            String semantic)
    {
        super();
        this._takenValue = takenValue;
        this._lemma = lemma;
        this._chunkPartType = chunkPartType;
        this._semantic = semantic;
    }


    public static List<WordConstraint> generateWordConstraints(Uby uby, List<String> suggestedWords)
    {
        List<WordConstraint> output = new ArrayList<>();

        Lexicon lex;
        try {
            lex = uby.getLexiconByName("WordNet");
        }
        catch (UbyInvalidArgumentException e)
        {
            e.printStackTrace();
            return output;
        }

        for(String word : suggestedWords)
        {
            for(LexicalEntry entry : uby.getLexicalEntries(word, null, null))//lex))
            {
                String lemma = entry.getLemmaForm();
                EPartOfSpeech pos = entry.getPartOfSpeech();
                if(pos == null)
                {
                    continue;
                }

                ChunkPartType type = ChunkPartType.getTypeOf(pos);
                if(type.equals(ChunkPartType.UNDEFINED))
                {
                    continue;
                }
                ArrayList<String> sems = new ArrayList<>();
                for(Sense sens : entry.getSenses())
                {
                    for (SemanticLabel sem : sens.getSemanticLabels())
                    {
                        String[] semValueTable = sem.getLabel().split("\\.");
                        if(semValueTable.length>0)
                        {
                            String semValue = semValueTable[semValueTable.length-1];
                            if(!sems.contains(semValue))
                            {
                                sems.add(semValue);
                            }
                        }
                    }
                }

                for(String sem : sems)
                {
                   WordConstraint constraint = new WordConstraint(word,lemma,type,sem);
                   output.add(constraint);
                }
            }

        }

        return output;
    }

    public String getTakenValue()
    {
        return _takenValue;
    }
    public void setTakenValue(String takenValue)
    {
        this._takenValue = takenValue;
    }
    public String getLemma()
    {
        return _lemma;
    }
    public void setLemma(String lemma)
    {
        this._lemma = lemma;
    }
    public String getSemantic()
    {
        return _semantic;
    }
    public void setSemantic(String semantic)
    {
        this._semantic = semantic;
    }
    public ChunkPartType getChunkPartType()
    {
        return _chunkPartType;
    }
    public void setChunkPartType(ChunkPartType chunkPartType)
    {
        this._chunkPartType = chunkPartType;
    }

    @Override
    public String toString()
    {
        return "WordConstraint : "+_takenValue+" "+_lemma+" "+_chunkPartType.toString()+" "+_semantic;
    }

}
