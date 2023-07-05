package com.stubhub.domain.account.biz.impl;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.domain.inventory.v2.DTO.Product;

public class ProductSeatUtilTest {

	private Product getProduct(String seat) {
		Product p = new Product();
		p.setSeat(seat);
		return p;
	}

	@Test
	public void testSort() {
		List<Product> rs = ProductSeatUtil.sortBySeat(null);
		Assert.assertNull(rs);

		List<Product> src = new ArrayList<Product>();

		rs = ProductSeatUtil.sortBySeat(src);
		Assert.assertTrue(rs.isEmpty());

		src.add(getProduct("0"));
		src.add(getProduct("4"));
		src.add(getProduct("2"));
		src.add(getProduct("3"));
		src.add(getProduct("1"));

		rs = ProductSeatUtil.sortBySeat(src);

		Assert.assertTrue(rs.get(0).getSeat().compareTo("0") == 0);
		Assert.assertTrue(rs.get(1).getSeat().compareTo("1") == 0);
		Assert.assertTrue(rs.get(2).getSeat().compareTo("2") == 0);
		Assert.assertTrue(rs.get(3).getSeat().compareTo("3") == 0);
		Assert.assertTrue(rs.get(4).getSeat().compareTo("4") == 0);

		src.add(getProduct(null));
		src.add(getProduct(null));

		rs = ProductSeatUtil.sortBySeat(src);

		Assert.assertNull(rs.get(6).getSeat());
		Assert.assertNull(rs.get(5).getSeat());
		Assert.assertTrue(rs.get(0).getSeat().compareTo("0") == 0);
		Assert.assertTrue(rs.get(1).getSeat().compareTo("1") == 0);
		Assert.assertTrue(rs.get(2).getSeat().compareTo("2") == 0);
		Assert.assertTrue(rs.get(3).getSeat().compareTo("3") == 0);
		Assert.assertTrue(rs.get(4).getSeat().compareTo("4") == 0);

		src.clear();

		src.add(getProduct("AAA"));
		src.add(getProduct("ZZZ"));
		src.add(getProduct("DMC"));
		src.add(getProduct("AAB"));

		rs = ProductSeatUtil.sortBySeat(src);

		Assert.assertTrue(rs.get(0).getSeat().compareTo("AAA") == 0);
		Assert.assertTrue(rs.get(1).getSeat().compareTo("AAB") == 0);
		Assert.assertTrue(rs.get(2).getSeat().compareTo("DMC") == 0);
		Assert.assertTrue(rs.get(3).getSeat().compareTo("ZZZ") == 0);

	}

	@Test
	public void testSort2() {
		
		List<Product> src = new ArrayList<Product>();

		src.add(getProduct("5"));
		src.add(getProduct("9"));
		src.add(getProduct("8"));
		src.add(getProduct("10"));
		src.add(getProduct("7"));
		src.add(getProduct("8A"));
		src.add(getProduct("B"));
		src.add(getProduct("8A22"));
		src.add(getProduct("6"));
		src.add(getProduct("B1"));
		src.add(getProduct(null));
		src.add(getProduct("8B"));
		src.add(getProduct("8A12"));
		src.add(getProduct("D11"));
		src.add(getProduct("A31"));
		src.add(getProduct("A1"));
		src.add(getProduct("A11"));
		src.add(getProduct("A21"));
		// right order: 5, 6, 7, 8, 8A, 8A12, 8A22, 8B, 9, 10, A1, A11, A21, A31, B, B1, D11, null
		List<Product> rs = ProductSeatUtil.sortBySeat(src);
		
		
		Assert.assertTrue(rs.get(0).getSeat().compareTo("5") == 0);
		Assert.assertTrue(rs.get(1).getSeat().compareTo("6") == 0);
		Assert.assertTrue(rs.get(2).getSeat().compareTo("7") == 0);
		Assert.assertTrue(rs.get(3).getSeat().compareTo("8") == 0); 
		Assert.assertTrue(rs.get(4).getSeat().compareTo("8A") == 0); 
		Assert.assertTrue(rs.get(5).getSeat().compareTo("8A12") == 0);
		Assert.assertTrue(rs.get(6).getSeat().compareTo("8A22") == 0);
		Assert.assertTrue(rs.get(7).getSeat().compareTo("8B") == 0); 
		Assert.assertTrue(rs.get(8).getSeat().compareTo("9") == 0);
		Assert.assertTrue(rs.get(9).getSeat().compareTo("10") == 0); 
		Assert.assertTrue(rs.get(10).getSeat().compareTo("A1") == 0); 
		Assert.assertTrue(rs.get(11).getSeat().compareTo("A11") == 0);
		Assert.assertTrue(rs.get(12).getSeat().compareTo("A21") == 0);
		Assert.assertTrue(rs.get(13).getSeat().compareTo("A31") == 0);
		Assert.assertTrue(rs.get(14).getSeat().compareTo("B") == 0);
		Assert.assertTrue(rs.get(15).getSeat().compareTo("B1") == 0);
		Assert.assertTrue(rs.get(16).getSeat().compareTo("D11") == 0); 
		Assert.assertTrue(rs.get(17).getSeat() == null);
		
		
	}
}
