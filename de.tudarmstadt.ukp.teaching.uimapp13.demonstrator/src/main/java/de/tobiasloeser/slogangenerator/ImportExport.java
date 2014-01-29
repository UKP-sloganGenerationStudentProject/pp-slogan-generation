package de.tobiasloeser.slogangenerator;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

public class ImportExport {

	public void exportTemplates(List<SloganTemplate> templates, String target)
	{
		OutputStream fos = null;
		try
		{
		  fos = new FileOutputStream(target);
		  ObjectOutputStream o = new ObjectOutputStream(fos);
		  o.writeObject(templates);
		  fos.close();
		}
		catch ( IOException e ) 
		{
			System.err.println( e ); 
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<SloganTemplate> importTemplates(String source)
	{
		List<SloganTemplate> templates = null;
		InputStream fis = null;
		try
		{
		  fis = new FileInputStream(source);
		  ObjectInputStream o = new ObjectInputStream(fis);
		  templates = (List<SloganTemplate>)o.readObject();
		  fis.close();
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
		return templates;
	}
	
	public void exportEmotions(List<Emotion> emotions, String target)
	{
		OutputStream fos = null;
		try
		{
		  fos = new FileOutputStream(target);
		  ObjectOutputStream o = new ObjectOutputStream(fos);
		  o.writeObject(emotions);
		  fos.close();
		}
		catch ( IOException e ) 
		{
			System.err.println( e ); 
		}	
	}
	
	@SuppressWarnings("unchecked")
	public List<Emotion> importEmotions(String source)
	{
		List<Emotion> emotions = null;
		InputStream fis = null;
		try
		{
		  fis = new FileInputStream(source);
		  ObjectInputStream o = new ObjectInputStream(fis);
		  emotions = (List<Emotion>)o.readObject();
		  fis.close();
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
		return emotions;	
	}

}
