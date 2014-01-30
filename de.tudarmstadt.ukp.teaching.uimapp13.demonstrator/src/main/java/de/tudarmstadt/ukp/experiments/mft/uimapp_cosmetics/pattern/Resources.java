package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.jweb1t.JWeb1TSearcher;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions.EmotionAnalyzer;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.constraints.WordConstraint;
import de.tudarmstadt.ukp.lmf.api.Uby;

public class Resources
{
    Uby _uby;
    EmotionAnalyzer _emotionAnalizer;
    JWeb1TSearcher _web1tWordStatistic;

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

    public void generateConstraints()
    {
        if(_uby == null)
        {
            System.out.println("class Resources : Uby have not been defined. We can't generate the constraints yet.");
            return ;
        }
        _suggestedWordsConstraints = WordConstraint.generateWordConstraints(_uby, _suggestedWords);
    }

}
