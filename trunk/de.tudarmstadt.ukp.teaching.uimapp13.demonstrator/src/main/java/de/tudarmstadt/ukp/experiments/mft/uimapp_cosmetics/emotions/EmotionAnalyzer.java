package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.emotions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/*
 * responsible for reading the emotion information from a specific pdf document.
 * Written by Felix Rieger
 */

public class EmotionAnalyzer
{

    String _path;
    HashMap<String, EmotionModel> _emotions;

    public EmotionAnalyzer(final String path)
    {
        this._emotions = null;
        this._path = path;

        PDDocument pd = null;
        try {
            final File input = new File(path);
            pd = PDDocument.load(input);
            final PDFTextStripper stripper = new PDFTextStripper();

            stripper.setStartPage(6);
            final String extractedText = stripper.getText(pd);
            final String[] rawText = extractedText.split("\n");
            final ArrayList<EmotionModel> data = new ArrayList<EmotionModel>(rawText.length);

            double versionNumber = 0;
            final String versionNumberString = rawText[1]
                    .trim()
                    .substring(rawText[1].trim().indexOf("version") + "version".length(),
                            rawText[1].trim().length() - 1).trim();
            System.out.println("->" + versionNumberString + "<-");
            versionNumber = Double.parseDouble(versionNumberString);
            System.out.println(versionNumber);

            rawText[0] = "remove";
            rawText[1] = "remove";
            for (final String str : rawText) {
                final String tmpStr = str.trim();
                if (tmpStr.length() < 10) {
                    // drop strings < 10 (page numbers etc.)
                    continue;
                }
                else {
                    final String[] tmpData = tmpStr.split("\\s");
                    final EmotionModel tmpEm = new EmotionModel(tmpData[0].trim().toLowerCase(),
                            tmpData[1].trim().endsWith("1"), tmpData[2].trim().endsWith("1"),
                            tmpData[3].trim().endsWith("1"), tmpData[4].trim().endsWith("1"),
                            tmpData[5].trim().endsWith("1"), tmpData[6].trim().endsWith("1"),
                            tmpData[7].trim().endsWith("1"), tmpData[8].trim().endsWith("1"),
                            tmpData[9].trim().endsWith("1"), tmpData[10].trim().endsWith("1"));

                    data.add(tmpEm);
                }

            }

            this._emotions = new HashMap<String, EmotionModel>();
            for (final EmotionModel e : data) {
                this._emotions.put(e.getWord(), e);
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        finally {
            if (pd != null) {
                try {
                    pd.close();
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public EmotionModel getEmotion(final String word)
    {
        return this._emotions.get(word);
    }

}
