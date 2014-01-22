
/* First created by JCasGen Mon Dec 16 23:14:59 CET 2013 */
package de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Sat Jan 04 17:57:44 CET 2014
 * @generated */
public class EmotionAnnotation_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (EmotionAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = EmotionAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new EmotionAnnotation(addr, EmotionAnnotation_Type.this);
  			   EmotionAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new EmotionAnnotation(addr, EmotionAnnotation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = EmotionAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.EmotionAnnotation");
 
  /** @generated */
  final Feature casFeat_valence;
  /** @generated */
  final int     casFeatCode_valence;
  /** @generated */ 
  public String getValence(int addr) {
        if (featOkTst && casFeat_valence == null)
      jcas.throwFeatMissing("valence", "de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.EmotionAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_valence);
  }
  /** @generated */    
  public void setValence(int addr, String v) {
        if (featOkTst && casFeat_valence == null)
      jcas.throwFeatMissing("valence", "de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.EmotionAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_valence, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public EmotionAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_valence = jcas.getRequiredFeatureDE(casType, "valence", "uima.cas.String", featOkTst);
    casFeatCode_valence  = (null == casFeat_valence) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_valence).getCode();

  }
}



    