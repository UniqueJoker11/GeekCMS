package org.marker.mushroom.interceptor;

public class Test {

	public static void main(String[] args) {
		String a = "application/json, text/javascript, */*; q=0.01";
		System.out.println(a.matches(".*application/json.*"));
		
	}
}
