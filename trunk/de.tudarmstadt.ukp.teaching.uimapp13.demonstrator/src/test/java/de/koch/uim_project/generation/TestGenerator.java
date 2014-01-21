package de.koch.uim_project.generation;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class TestGenerator {

	@Test
	public void testNormalizeWeights(){
		Map<Character,Double> testMap = new HashMap<Character,Double>();
		testMap.put('a', 8.0);
		testMap.put('b', 8.0);
		testMap.put('c',4.0);
		Map<Character,Double> result1 = Generator.normalizeWeights(testMap);
		assertTrue(result1.get('a') == 0.4);
		assertTrue(result1.get('b')== 0.4);
		assertTrue(result1.get('c') == 0.2);
		
		testMap = new HashMap<Character,Double>();
		testMap.put('a', 0.1);
		testMap.put('b', 0.2);
		testMap.put('c', 0.1);
		testMap.put('d',0.1);
		result1 = Generator.normalizeWeights(testMap);
		assertTrue(result1.get('a') == 0.2);
		assertTrue(result1.get('b')== 0.4);
		assertTrue(result1.get('c') == 0.2);
		assertTrue(result1.get('d') == 0.2);
		
		testMap = new HashMap<Character,Double>();
		testMap.put('a', 0.0);
		testMap.put('b', 0.2);
		testMap.put('c', 0.0);
		testMap.put('d',0.3);
		result1 = Generator.normalizeWeights(testMap);
		assertTrue(result1.get('a') == 0.0);
		assertTrue(result1.get('b')== 0.4);
		assertTrue(result1.get('c') == 0.0);
		assertTrue(result1.get('d') == 0.6);
	}
}
