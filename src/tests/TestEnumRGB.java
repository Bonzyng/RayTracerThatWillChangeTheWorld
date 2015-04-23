package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import e3.utils.eRGB;

public class TestEnumRGB {
	
	@Test
	public void testValue() {
		System.out.println(eRGB.values()[0]);
		System.out.println(eRGB.values()[1]);
		System.out.println(eRGB.values()[2]);
	}
}
