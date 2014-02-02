package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.jweb1t.JWeb1TSearcher;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionAnalyzer;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.sloganGeneration.constraints.WordConstraint;
import de.tudarmstadt.ukp.lmf.api.Uby;

public class Resources
{



    Uby _uby;
    EmotionAnalyzer _emotionAnalizer;
    JWeb1TSearcher _web1tWordStatistic;
    JWeb1TSearcher _allowedWordsSearcher;
    File _rejectedWordsOutput;

    String _productName;
    String _patternToGenerate;
    String _selectedPartOfBody;
    List<String> _suggestedWords;
    List<WordConstraint> _suggestedWordsConstraints;

    boolean _useUbyGeneration;




    public Resources()
    {
        this._uby = null;
        this._emotionAnalizer = null;
        this._web1tWordStatistic = null;
        this._allowedWordsSearcher = null;
        this._rejectedWordsOutput = null;
        this._productName = "productName";
        this._patternToGenerate = "";
        this._selectedPartOfBody = "";
        this._suggestedWords = new ArrayList<String>();
        this._useUbyGeneration = true;
        this._suggestedWordsConstraints = new ArrayList<>();
    }

    public Resources(final Uby uby, final EmotionAnalyzer emotionAnalizer,
            final JWeb1TSearcher web1tSearcher)
    {
        super();
        this._uby = uby;
        this._emotionAnalizer = emotionAnalizer;
        this._web1tWordStatistic = web1tSearcher;
    }

    public Uby getUby()
    {
        return this._uby;
    }

    public void setUby(final Uby uby)
    {
        this._uby = uby;
    }

    public void useUbyForNewWords(final boolean tof)
    {
        this._useUbyGeneration = tof;
    }

    public boolean isUbyGernationAllowed()
    {
        return this._useUbyGeneration;
    }

    public EmotionAnalyzer getEmotionAnalizer()
    {
        return this._emotionAnalizer;
    }

    public void setEmotionAnalizer(final EmotionAnalyzer emotionAnalizer)
    {
        this._emotionAnalizer = emotionAnalizer;
    }

    public JWeb1TSearcher getWordStatistic()
    {
        return this._web1tWordStatistic;
    }

    public void setWordStatistic(final JWeb1TSearcher web1tSearcher)
    {
        this._web1tWordStatistic = web1tSearcher;
    }

    public String getProductName()
    {
        return this._productName;
    }

    public void setProductName(final String productName)
    {
        this._productName = productName;
    }

    public String getPatternToGenerate()
    {
        return this._patternToGenerate;
    }

    public void setPatternToGenerate(final String patternToGenerate)
    {
        this._patternToGenerate = patternToGenerate;
    }

    public boolean hasPatternTypeConstraint()
    {
        return !getPatternToGenerate().equals("") && !getPatternToGenerate().equals(Parameters.DONT_CARE);
    }

    public String getSelectedBodyPart()
    {
        return this._selectedPartOfBody;
    }

    public void setSelectedPartOfBody(final String part)
    {
        this._selectedPartOfBody = part;
    }

    public List<String> getSuggestedWords()
    {
        return this._suggestedWords;
    }

    public void setSuggestedWords(final List<String> suggestedWords)
    {
        this._suggestedWords = suggestedWords;
    }

    public boolean hasBodyPartConstraint()
    {
        return !getSelectedBodyPart().equals(Parameters.NO_BODY_PART) && !getSelectedBodyPart().equals("");
    }

    public void generateConstraints()
    {
        if(_uby == null)
        {
            System.out.println("class Resources : Uby have not been defined. We can't generate the constraints yet.");
            return ;
        }
        _suggestedWordsConstraints = WordConstraint.generateWordConstraints(_uby, _suggestedWords);
    }

    public List<WordConstraint> getConstraints()
    {
        return _suggestedWordsConstraints;
    }

    public boolean hasSuggestedWordConstraints()
    {
        return _suggestedWordsConstraints.size()>0;
    }

    public void printConstraints()
    {
        System.out.println("Constraints :");
        System.out.println();
        for(WordConstraint constraint : _suggestedWordsConstraints)
        {
            System.out.println(constraint.toString());
        }
    }

    public void setAllowedWordSearcher(JWeb1TSearcher allowedWordsSearcher)
    {

        _allowedWordsSearcher = allowedWordsSearcher;
    }

    public void setAllowedWordSearcher(JWeb1TSearcher allowedWordsSearcher,File rejectedWordsOutput)
    {
        _allowedWordsSearcher = allowedWordsSearcher;
        _rejectedWordsOutput = rejectedWordsOutput;
    }


    public boolean checkLemmaInCosmeticsCorpus(String lemma)
    {
        //by default, everything is allowed
        if(_allowedWordsSearcher == null)
        {
            System.out.println("allowed word searcher is null");
            return true;
        }

        try {


            long frequency = _allowedWordsSearcher.getFrequency(lemma);
            if(frequency>0)
            {
                return true;
            }
            else
            {
                if(_rejectedWordsOutput!= null)
                {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(_rejectedWordsOutput,true)));
                    out.println(lemma);
                    out.close();
                }
                else
                {
                    System.out.println("The lemma "+lemma+" has been rejected but the rejectedWordWriter doesn't exist.");
                }
                return false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

}