package de.tobiasloeser.slogangenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;


public class textextraction {

	public List<Emotion> getEmotions()
	{
		return getEmotionsByPath("input/NRCemotionlexicon.pdf");
	}
	
	public List<Emotion> getEmotionsByPath(String path) {

		try {
        File input = new File(path);
        PDDocument pd = PDDocument.load(input);
        PDFTextStripper stripper = new PDFTextStripper();

        stripper.setStartPage(6);
        String extractedText = stripper.getText(pd);
        String[] rawText = extractedText.split("\n");
        ArrayList<Emotion> data = new ArrayList<Emotion>(rawText.length);
        
        double versionNumber = 0;
        String versionNumberString = rawText[1].trim().substring(rawText[1].trim().indexOf("version") + "version".length(),rawText[1].trim().length()-1).trim();
        System.out.println("->" + versionNumberString + "<-");
        versionNumber = Double.parseDouble(versionNumberString);
        System.out.println(versionNumber);
        
        
        rawText[0] = "remove";
        rawText[1] = "remove";
        for (String str : rawText) {
        	String tmpStr = str.trim();
        	if (tmpStr.length() < 10) {
        		// drop strings < 10 (page numbers etc.)
        		continue;
        	} else {
        		String[] tmpData = tmpStr.split("\\s");
        		Emotion tmpEm = new Emotion(tmpData[0].trim().toLowerCase(), 
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
        		System.out.println(tmpStr);
        		System.out.println(tmpEm);
        		System.out.println("\n");
        	}
        	
		}
        
        HashMap<String, Emotion> emotions = new HashMap<String, Emotion>();
        for (Emotion e : data) {
        	emotions.put(e.getWord(), e);
        }
        
        System.out.println(emotions.get("love"));
        System.out.println(emotions.get("hate"));
        System.out.println(Emotion.getEuclideanDistance(emotions.get("love"), emotions.get("hate")));
        
        return data;
        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
