package com.stubhub.domain.account.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.solr.common.SolrDocument;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.stubhub.domain.account.common.enums.TicketTrait;

public class TicketTraitMapperTest {
	@Test
	public void testMap() throws Exception{
		TicketTraitMapper mapper = new TicketTraitMapper();
		RowMeta meta = new RowMeta(new String[]{"traits"}, null, null, null);
		SolrDocument row = new SolrDocument();
		List<String> traitsString = new ArrayList<String>();		
		traitsString.add("203,2001,3248,3288,5370,5518,5560,13734|Alcohol-free seating,Possible obstruction (printed on ticket),Limited side view (printed on ticket),Side view (printed on ticket),Partial view (printed on ticket),16-person suite,20-person suite,Restricted view  (printed on ticket)|2,2,2,2,2,3,3,2|Listing Disclosure,Listing Disclosure,Listing Disclosure,Listing Disclosure,Listing Disclosure,Seller Comments,Seller Comments,Listing Disclosure");
		row.addField("traits", traitsString);
		
		Set<TicketTrait> traits = (Set<TicketTrait>)mapper.map(row, meta);
		Assert.assertEquals(traits.size(), 8);
		
		traitsString = new ArrayList<String>();
		traitsString.add(0,"|||");
		row.remove("traits");
		row.addField("traits", traitsString);
		traits = (Set<TicketTrait>)mapper.map(row, meta);
		Assert.assertNull(traits);

		traitsString = new ArrayList<String>();
		traitsString.add(0,"102|Parking pass|1|Ticket Feature");
		row.remove("traits");
		row.addField("traits", traitsString);
		traits = (Set<TicketTrait>)mapper.map(row, meta);
		Assert.assertEquals(traits.toArray(new TicketTrait[1])[0].getName(), "Parking pass");
		Assert.assertEquals(traits.toArray(new TicketTrait[1])[0].getType(), "Ticket Feature");

		traitsString = new ArrayList<String>();
		traitsString.add(0,"14100|Includes limited alcoholic beverages - beer, wine and liquor|3|Seller Comments");
		row.remove("traits");
		row.addField("traits", traitsString);
		traits = (Set<TicketTrait>)mapper.map(row, meta);
		Assert.assertEquals(traits.toArray(new TicketTrait[1])[0].getName(), "Includes limited alcoholic beverages - beer, wine and liquor");
		Assert.assertEquals(traits.toArray(new TicketTrait[1])[0].getType(), "Seller Comments");

		traitsString = new ArrayList<String>();
		traitsString.add(0,"101,14100|Aisle,Includes limited alcoholic beverages - beer, wine and liquor|1,3|Ticket Feature,Seller Comments");
		row.remove("traits");
		row.addField("traits", traitsString);
		traits = (Set<TicketTrait>)mapper.map(row, meta);
		TicketTrait[] ticketTraits = traits.toArray(new TicketTrait[2]);
		Assert.assertEquals(ticketTraits[0].getName(), null);
		Assert.assertEquals(ticketTraits[1].getName(), null);

	}
}
