package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.ChunkType;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.enumerations.VerbForm;

public class VerbChunkGeneric
    extends ChunkGeneric
{

    private static final long serialVersionUID = -7294589898479992115L;
    VerbForm _form;

    public VerbChunkGeneric()
    {
        super();
        this._chunkType = ChunkType.VC;
        this._form = VerbForm.NO_INFORMATION;
    }

    public void setForm(final VerbForm form)
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

        final ChunkPart part = occurrence.getAt(occurrence.getPartsNbr() - 1);
        part.setDerivable(true);

        final String pos = part.getPos();

        if (pos.equals("VB") || pos.equals("VV")) {
            this.setForm(VerbForm.LEMMA);

            if (occurrence.getPartsNbr() > 1) {
                if (occurrence.getAt(occurrence.getPartsNbr() - 2).getPos().equals("TO")) {
                    this.setForm(VerbForm.TO_);
                }
            }
        }

        if (pos.equals("VBD") || pos.equals("VBN") || pos.equals("VVD") || pos.equals("VVN")) {
            // verb is past tense or past participle
            this.setForm(VerbForm.PAST);
        }

        if (pos.equals("VVG")) {
            this.setForm(VerbForm._ING);

            if (occurrence.getPartsNbr() > 1) {
                final String prevPosVal = occurrence.getAt(occurrence.getPartsNbr() - 2).getPos();
                if (prevPosVal.startsWith("VB")) {
                    this.setForm(VerbForm.PRESENT);
                }
            }
        }

        if (pos.equals("VBP") || pos.equals("VBZ") || pos.equals("VVP") || pos.equals("VVZ")) {
            this.setForm(VerbForm.PRESENT);
        }
    }

}
