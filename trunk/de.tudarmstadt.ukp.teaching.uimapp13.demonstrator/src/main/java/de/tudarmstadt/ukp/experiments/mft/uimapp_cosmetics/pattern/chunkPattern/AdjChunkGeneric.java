package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.AdjForm;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;

public class AdjChunkGeneric
    extends ChunkGeneric
{

    private static final long serialVersionUID = -3863899448651154362L;
    AdjForm _form;

    public AdjChunkGeneric()
    {
        super();
        this._chunkType = ChunkType.ADJC;
    }

    public void setForm(final AdjForm form)
    {
        this._form = form;
    }

    @Override
    public String getId()
    {
        final StringBuilder id = new StringBuilder();
        id.append(super.getId());
        id.append(" [");
        id.append("form:");
        id.append(this._form);
        id.append("]");
        return id.toString();
    }

    @Override
    public String getSpecificInformation()
    {
        final StringBuilder output = new StringBuilder();
        output.append(" [");
        output.append("form:");
        output.append(this._form);
        output.append("]");

        return output.toString();
    }

    @Override
    public void specializedHeaderGeneration(final Chunk occurrence)
    {
        this._isValueDerivable = true;
        this._semanticValue = occurrence.getAt(occurrence.getPartsNbr() - 1).getSemanticValue();

        final String pos = occurrence.getAt(occurrence.getPartsNbr() - 1).getPos();

        if (pos.equals("JJR")) {
            this.setForm(AdjForm.COMP);
        }

        if (pos.equals("JJS")) {
            this.setForm(AdjForm.SUPER);
        }

        if (pos.equals("JJ")) {
            this.setForm(AdjForm.LEMMA);

            if (occurrence.getPartsNbr() > 1) {
                final String prevPosVal = occurrence.getAt(occurrence.getPartsNbr() - 1).getPos();
                if (prevPosVal.equals("RBR")) {
                    this.setForm(AdjForm.COMP);
                }

                if (prevPosVal.equals("RBS")) {
                    this.setForm(AdjForm.SUPER);
                }
            }
        }

    }

}
