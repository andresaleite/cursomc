package com.andresa.cursomc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class URL {

	public URL() {
		// TODO Auto-generated constructor stub
	}
	
	public static String decodeParam(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	public static List<Integer> decodeIntList(String s) {
		String[] vetor = s.split(",");
		List<Integer> lista = new ArrayList<Integer>();
		for (int i=0; i<vetor.length;i++) {
			lista.add(Integer.parseInt(vetor[i]));
		}
		return lista;
	}

}
