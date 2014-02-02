package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations;

public enum AdjForm
{
    LEMMA("AdjForm.LEMMA"),
    SUPER("AdjForm.SUPER"),
    COMP("AdjForm.COMP"),
    NO_INFORMATION("AdjForm.NO_INFORMATION");

    private String name ="";
    AdjForm(String name)
    {
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}