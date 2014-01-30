package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations;

public enum VerbForm
{
    LEMMA("VerbForm.LEMMA"),  //imperative ?
    PRESENT("VerbForm.PRESENT"),
    _ING("VerbForm._ING"),
    PAST("VerbForm.PAST"),
    TO_("VerbForm.TO_"),
    NO_INFORMATION("VerbForm.NO_INFORMATION");

    private String name ="";
    VerbForm(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}