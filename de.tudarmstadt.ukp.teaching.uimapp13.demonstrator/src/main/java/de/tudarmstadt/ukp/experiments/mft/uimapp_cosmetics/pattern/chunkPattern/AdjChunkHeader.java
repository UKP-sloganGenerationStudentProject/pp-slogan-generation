package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.pattern.chunkPattern;

import de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.POSForm.AdjForm;


public class AdjChunkHeader extends ChunkHeader
{

    AdjForm _form;

    public AdjChunkHeader()
    {
        super();
        _chunkType = ChunkType.ADJC;
    }

    public void setForm(AdjForm form)
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

        String pos = occurrence.getAt(occurrence.getPartsNbr()-1).getPos();

        if(pos.equals("JJR"))
        {
            setForm(AdjForm.COMP);
        }

        if(pos.equals("JJS"))
        {
            setForm(AdjForm.SUPER);
        }

        if(pos.equals("JJ"))
        {
            setForm(AdjForm.LEMMA);

            if(occurrence.getPartsNbr()>1)
            {
                String prevPosVal = occurrence.getAt(occurrence.getPartsNbr()-1).getPos();
                if(prevPosVal.equals("RBR"))
                {
                    setForm(AdjForm.COMP);
                }

                if(prevPosVal.equals("RBS"))
                {
                    setForm(AdjForm.SUPER);
                }
             }
        }

    }

}
