package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations;

/**
*
* @author Matthieu Fraissinet-Tachet
*
*/

public enum ChunkType
{
    NC("NC"), VC("VC"), ADJC("ADJC"), PC("PC"), ADVC("ADVC"), UNDEFINED("UNDEFINED");

    private String name = "";

    ChunkType(final String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}