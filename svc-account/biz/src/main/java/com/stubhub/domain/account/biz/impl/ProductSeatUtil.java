package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.stubhub.domain.inventory.v2.DTO.Product;

public class ProductSeatUtil {

	private static ProductSeatComparator productSeatComparator = new ProductSeatComparator();

	public static List<Product> sortBySeat(List<Product> src) {

		if ((src == null) || src.isEmpty()) {
			return src;
		} else {
			List<Product> dst = new ArrayList<Product>();
			dst.addAll(src);
			Collections.sort(dst, productSeatComparator);
			return dst;
		}
	}

	// alphanum sorting
	private static class ProductSeatComparator implements Comparator<Product> {

		private final boolean isDigit(char ch) {
			return ch >= 48 && ch <= 57;
		}

		private final String getChunk(String s, int slength, int m) {
			StringBuilder chunk = new StringBuilder();
			int marker = m;
			char c = s.charAt(marker);
			chunk.append(c);
			marker++;
			if (isDigit(c)) {
				while (marker < slength) {
					c = s.charAt(marker);
					if (!isDigit(c))
						break;
					chunk.append(c);
					marker++;
				}
			} else {
				while (marker < slength) {
					c = s.charAt(marker);
					if (isDigit(c))
						break;
					chunk.append(c);
					marker++;
				}
			}
			return chunk.toString();
		}

		public int compare(Product product1, Product product2) {

			// nulls will be at the end
			if (product1.getSeat() == null)
				return 1;
			else if (product2.getSeat() == null)
				return -1;

			String s1 = product1.getSeat();
			String s2 = product2.getSeat();

			int thisMarker = 0;
			int thatMarker = 0;
			int s1Length = s1.length();
			int s2Length = s2.length();

			while (thisMarker < s1Length && thatMarker < s2Length) {
				
				String thisChunk = getChunk(s1, s1Length, thisMarker);
				thisMarker += thisChunk.length();

				String thatChunk = getChunk(s2, s2Length, thatMarker);
				thatMarker += thatChunk.length();

				int result = 0;
				if (isDigit(thisChunk.charAt(0))
						&& isDigit(thatChunk.charAt(0))) {

					int thisChunkLength = thisChunk.length();
					result = thisChunkLength - thatChunk.length();

					if (result == 0) {
						for (int i = 0; i < thisChunkLength; i++) {
							result = thisChunk.charAt(i) - thatChunk.charAt(i);
							if (result != 0) {
								return result;
							}
						}
					}
				} else {
					result = thisChunk.compareTo(thatChunk);
				}

				if (result != 0)
					return result;
			}

			return s1Length - s2Length;
		}
	}
}