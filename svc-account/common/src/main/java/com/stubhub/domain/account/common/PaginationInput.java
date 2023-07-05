package com.stubhub.domain.account.common;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "paginationInput")
@XmlType(name = "", propOrder = {
		"start",
		"rows"
		})
public class PaginationInput {
	public static int DEFAULT_ENTRIES_PERPAGE=250;
	
	@XmlElement(name = "rows", required = false)
	private int rows = DEFAULT_ENTRIES_PERPAGE;
	@XmlElement(name = "start", required = false)
	private int start = 0;
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("PaginationInput{");
		sb.append("rows=").append(rows);
		sb.append(", start=").append(start);
		sb.append('}');
		return sb.toString();
	}
}
