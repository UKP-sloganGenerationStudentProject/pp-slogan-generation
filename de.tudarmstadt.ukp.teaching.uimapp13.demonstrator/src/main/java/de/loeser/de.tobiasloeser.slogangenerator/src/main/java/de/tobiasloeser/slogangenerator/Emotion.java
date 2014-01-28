package de.tobiasloeser.slogangenerator;

public class Emotion implements java.io.Serializable {

	// abandon positive:0 negative:1 anger:0 anticipation:0 disgust:0 fear:1 joy:0 sadness:1 surprise:0 trust:0

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String word;
	boolean positive;
	boolean negative;
	boolean anger;
	boolean anticipation;
	boolean disgust;
	boolean fear;
	boolean joy;
	boolean sadness;
	boolean surprise;
	boolean trust;
	
	
	
	public Emotion(String word, boolean positive, boolean negative,
			boolean anger, boolean anticipation, boolean disgust, boolean fear,
			boolean joy, boolean sadness, boolean surprise, boolean trust) {
		super();
		this.word = word;
		this.positive = positive;
		this.negative = negative;
		this.anger = anger;
		this.anticipation = anticipation;
		this.disgust = disgust;
		this.fear = fear;
		this.joy = joy;
		this.sadness = sadness;
		this.surprise = surprise;
		this.trust = trust;
	}

	public String toString() {
		
		return word + 
				" positive:" + (positive ? "1" : "0") +
				" negative:" + (negative ? "1" : "0") +
				" anger:" + (anger ? "1" : "0") +
				" anticipation:" + (anticipation ? "1" : "0") +
				" disgust:" + (disgust ? "1" : "0") +
				" fear:" + (fear ? "1" : "0") +
				" joy:" + (joy ? "1" : "0") +
				" sadness:" + (sadness ? "1" : "0") +
				" surprise:" + (surprise ? "1" : "0") +
				" trust:" + (trust ? "1" : "0");
	}
	
	public String getWord() {
		return word;
	}

	public boolean isPositive() {
		return positive;
	}

	public boolean isNegative() {
		return negative;
	}

	public boolean isAnger() {
		return anger;
	}

	public boolean isAnticipation() {
		return anticipation;
	}

	public boolean isDisgust() {
		return disgust;
	}

	public boolean isFear() {
		return fear;
	}

	public boolean isJoy() {
		return joy;
	}

	public boolean isSadness() {
		return sadness;
	}

	public boolean isSurprise() {
		return surprise;
	}

	public boolean isTrust() {
		return trust;
	}
	
	
	public boolean is(String emotion)
	{
		if(emotion.toLowerCase() == "positive")
			return isPositive();
		if(emotion.toLowerCase() == "negative")
			return isNegative();
		if(emotion.toLowerCase() == "anger")
			return isAnger();
		if(emotion.toLowerCase() == "anticipation")
			return isAnticipation();
		if(emotion.toLowerCase() == "disgust")
			return isDisgust();
		if(emotion.toLowerCase() == "fear")
			return isFear();
		if(emotion.toLowerCase() == "joy")
			return isJoy();
		if(emotion.toLowerCase() == "sadness")
			return isSadness();
		if(emotion.toLowerCase() == "surprise")
			return isSurprise();
		if(emotion.toLowerCase() == "trust")
			return isTrust();
		return false;
	}
	

	public boolean[] getFeatureVector() {
		return new boolean[]{positive, negative, anger, anticipation, disgust, fear, joy, sadness, surprise, trust};
	}
	
	public static double getEuclideanDistance(Emotion e1, Emotion e2) {
		boolean[] fv1 = e1.getFeatureVector();
		boolean[] fv2 = e2.getFeatureVector();
		
		double result = 0;
		
		for (int i = 0; i < fv1.length; i++) {
			result += Math.abs((fv1[i] ? 1 : 0) - (fv2[i] ? 1 : 0));
		}
		
		return Math.sqrt(result);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (anger ? 1231 : 1237);
		result = prime * result + (anticipation ? 1231 : 1237);
		result = prime * result + (disgust ? 1231 : 1237);
		result = prime * result + (fear ? 1231 : 1237);
		result = prime * result + (joy ? 1231 : 1237);
		result = prime * result + (negative ? 1231 : 1237);
		result = prime * result + (positive ? 1231 : 1237);
		result = prime * result + (sadness ? 1231 : 1237);
		result = prime * result + (surprise ? 1231 : 1237);
		result = prime * result + (trust ? 1231 : 1237);
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Emotion other = (Emotion) obj;
		if (anger != other.anger)
			return false;
		if (anticipation != other.anticipation)
			return false;
		if (disgust != other.disgust)
			return false;
		if (fear != other.fear)
			return false;
		if (joy != other.joy)
			return false;
		if (negative != other.negative)
			return false;
		if (positive != other.positive)
			return false;
		if (sadness != other.sadness)
			return false;
		if (surprise != other.surprise)
			return false;
		if (trust != other.trust)
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
}
