package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class EmotionAnalizer
{
    String _path;
    HashMap<String, EmotionModel>  _emotions;

    public EmotionAnalizer(String path)
    {
        _emotions = null;
        _path = path;

        try
        {
            File input = new File(path);
            PDDocument pd = PDDocument.load(input);
            PDFTextStripper stripper = new PDFTextStripper();

            stripper.setStartPage(6);
            String extractedText = stripper.getText(pd);
            String[] rawText = extractedText.split("\n");
            ArrayList<EmotionModel> data= new ArrayList<EmotionModel>(rawText.length);

            double versionNumber = 0;
            String versionNumberString = rawText[1].trim().substring(rawText[1].trim().indexOf("version") + "version".length(),rawText[1].trim().length()-1).trim();
            System.out.println("->" + versionNumberString + "<-");
            versionNumber = Double.parseDouble(versionNumberString);
            System.out.println(versionNumber);

            rawText[0] = "remove";
            rawText[1] = "remove";
            for (String str : rawText)
            {
                String tmpStr = str.trim();
                if (tmpStr.length() < 10)
                {
                    // drop strings < 10 (page numbers etc.)
                    continue;
                } else {
                    String[] tmpData = tmpStr.split("\\s");
                    EmotionModel tmpEm = new EmotionModel(tmpData[0].trim().toLowerCase(),
                            tmpData[1].trim().endsWith("1"),
                            tmpData[2].trim().endsWith("1"),
                            tmpData[3].trim().endsWith("1"),
                            tmpData[4].trim().endsWith("1"),
                            tmpData[5].trim().endsWith("1"),
                            tmpData[6].trim().endsWith("1"),
                            tmpData[7].trim().endsWith("1"),
                            tmpData[8].trim().endsWith("1"),
                            tmpData[9].trim().endsWith("1"),
                            tmpData[10].trim().endsWith("1"));

                    data.add(tmpEm);
                }

            }

            _emotions = new HashMap<String, EmotionModel>();
            for (EmotionModel e : data) {
                _emotions.put(e.getWord(), e);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    public EmotionModel getEmotion(String word)
    {
        return _emotions.get(word);
    }

}
