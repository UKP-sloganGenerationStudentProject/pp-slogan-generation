package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPart.ChunkPart;
import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.POSForm.VerbForm;


public class VerbChunkHeader extends ChunkHeader
{

    VerbForm _form;


    public VerbChunkHeader()
    {
        super();
        _chunkType = ChunkType.VC;
        _form = VerbForm.NO_INFORMATION;
    }

    public void setForm(VerbForm form)
    {
        _form = form;
    }

    @Override
    public String getId()
    {
        StringBuilder id = new StringBuilder();
        id.append(super.getId());
        id.append(" [");
        id.append("form:");
        id.append(_form);
        id.append("]");
        return id.toString();
    }


    @Override
    public String getSpecificInformation()
    {
        StringBuilder output = new StringBuilder();
        output.append(" [");
        output.append("form:");
        output.append(_form);
        output.append("]");
        return output.toString();
    }


    @Override
    public void specializedHeaderGeneration(Chunk occurrence)
    {


        _isValueDerivable = true;
        _semanticValue = occurrence.getAt(occurrence.getPartsNbr()-1).getSemanticValue();


        ChunkPart part = occurrence.getAt(occurrence.getPartsNbr()-1);
        part.setDerivable(true);

        String pos = part.getPos();

        if(pos.equals("VB")||pos.equals("VV"))
        {
            setForm(VerbForm.LEMMA);

            if(occurrence.getPartsNbr()>1)
            {
                if(occurrence.getAt(occurrence.getPartsNbr()-2).getPos().equals("TO"))
                {
                    setForm(VerbForm.TO_);
                }
            }
        }

        if(pos.equals("VBD")||pos.equals("VBN")
                ||pos.equals("VVD")||pos.equals("VVN"))
        {
            //verb is past tense or past participle
            setForm(VerbForm.PAST);
        }

        if(pos.equals("VVG"))
        {
            setForm(VerbForm._ING);

            if(occurrence.getPartsNbr()>1)
            {
                String prevPosVal = occurrence.getAt(occurrence.getPartsNbr()-2).getPos();
                if(prevPosVal.startsWith("VB"))
                {
                    setForm(VerbForm.PRESENT);
                }
            }
        }

        if(pos.equals("VBP")||pos.equals("VBZ")
                ||pos.equals("VVP")||pos.equals("VVZ"))
        {
            setForm(VerbForm.PRESENT);
        }
    }

}
