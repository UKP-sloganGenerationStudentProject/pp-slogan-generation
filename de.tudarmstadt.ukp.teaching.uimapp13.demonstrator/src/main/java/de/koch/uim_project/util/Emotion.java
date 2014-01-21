package de.koch.uim_project.util;

/**
 * Enum for all emotions which are recored in custom database with their ID used in database
 * @author Frerik Koch
 *
 */
public enum Emotion {
	ANGER(3), ANTICIPATION(4), DISGUST(5), FEAR(6), JOY(7), NEGATIVE(2), POSITIVE(1), SADNESS(8), SURPRISE(9), TRUST(10);

	public final int DATABASE_ID;

	Emotion(int dbId) {
		this.DATABASE_ID = dbId;
	}

	public static Emotion fromId(int id) {
		switch (id) {
		case 3:
			return ANGER;
		case 4:
			return ANTICIPATION;
		case 5:
			return DISGUST;
		case 6:
			return FEAR;
		case 7:
			return JOY;
		case 2:
			return NEGATIVE;
		case 1:
			return POSITIVE;
		case 8:
			return SADNESS;
		case 9:
			return SURPRISE;
		case 10:
			return TRUST;
		default:
			return null;
		}
	}

}
