package de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.adapters;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.ukp.teaching.uimapp13.demonstrator.model.Slogan;

public class GamesAdapter
    implements Adapter
{

    public static final String DEFAULT_GAME_NAME = "My Game";
    public static final int DEFAULT_NUM_SLOGANS = 40;

    public static final String EMOTION = "Emotion";

    public void initialize(final Map<String, Object> parameters)
    {

    }

    public List<Slogan> generateSlogans(final Map<String, Object> parameters)
    {
        return Arrays.asList(new Slogan("Test slogan for games"));
    }

}
