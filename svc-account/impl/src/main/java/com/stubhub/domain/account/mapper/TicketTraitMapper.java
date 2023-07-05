package com.stubhub.domain.account.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.solr.common.SolrDocument;

import com.stubhub.domain.account.common.enums.TicketTrait;

public class TicketTraitMapper implements DataMapper {

	@Override
	public Object map(SolrDocument row, RowMeta meta) throws Exception {
		List<String> ticketTraitInfo = (List<String>)row.get(meta.getFieldNames()[0]);
		if(ticketTraitInfo==null || ticketTraitInfo.size()<1){
			return null;
		}
		String seatTrait = "";		
		seatTrait = (String) ticketTraitInfo.get(0);		
		if ("|||".equals(seatTrait)) {
			return null;
		}
		Set<TicketTrait> traitSet = new HashSet<TicketTrait>();
		String[] ticketTraitIds, ticketTraits,  seatTraitTypeIds, ticketTraitTypes;
		StringTokenizer tokenizer = new StringTokenizer(seatTrait, "|");
		ticketTraitIds = tokenizer.nextToken().split(",");
		String ticketTraitsNamesRaw = tokenizer.nextToken();
		ticketTraits = ticketTraitsNamesRaw.split(",");
		boolean possibleWrongTicketTrait = false;

		if (ticketTraits.length > ticketTraitIds.length) {
			if (ticketTraitIds.length == 1) {
				ticketTraits[0] = ticketTraitsNamesRaw;
			} else {
				//as the trait name itself contains ',' when MCI store in this way , it is already wrong
				possibleWrongTicketTrait = true;
			}
		}
		seatTraitTypeIds = tokenizer.nextToken().split(",");
		ticketTraitTypes = tokenizer.nextToken().split(",");
		int arraySize = ticketTraitIds.length;
		TicketTrait traitObj;
		for (int i = 0; i < arraySize; i++) {
			traitObj = new TicketTrait();
			traitObj.setId(ticketTraitIds[i]);
			if (!possibleWrongTicketTrait) {
				//in this case, not set the possible wrong name, will be handled when API response
				traitObj.setName(ticketTraits[i]);
			}
			traitObj.setType(ticketTraitTypes[i]);
			traitSet.add(traitObj);
		}
		return traitSet;
	}

}
