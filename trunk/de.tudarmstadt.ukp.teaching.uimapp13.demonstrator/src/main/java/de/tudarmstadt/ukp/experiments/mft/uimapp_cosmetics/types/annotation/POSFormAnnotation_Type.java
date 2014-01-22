
/* First created by JCasGen Sat Jan 04 17:57:44 CET 2014 */
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
public class POSFormAnnotation_Type extends Annotation_Type {
  /** @generated */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (POSFormAnnotation_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = POSFormAnnotation_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new POSFormAnnotation(addr, POSFormAnnotation_Type.this);
  			   POSFormAnnotation_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new POSFormAnnotation(addr, POSFormAnnotation_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = POSFormAnnotation.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.POSFormAnnotation");
 
  /** @generated */
  final Feature casFeat_form;
  /** @generated */
  final int     casFeatCode_form;
  /** @generated */ 
  public String getForm(int addr) {
        if (featOkTst && casFeat_form == null)
      jcas.throwFeatMissing("form", "de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.POSFormAnnotation");
    return ll_cas.ll_getStringValue(addr, casFeatCode_form);
  }
  /** @generated */    
  public void setForm(int addr, String v) {
        if (featOkTst && casFeat_form == null)
      jcas.throwFeatMissing("form", "de.tudarmstadt.ukp.experiments.mft.uimapp_cosmetics.types.annotation.POSFormAnnotation");
    ll_cas.ll_setStringValue(addr, casFeatCode_form, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public POSFormAnnotation_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_form = jcas.getRequiredFeatureDE(casType, "form", "uima.cas.String", featOkTst);
    casFeatCode_form  = (null == casFeat_form) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_form).getCode();

  }
}



    