package org.pluto.ai.search.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchResult implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5014468157737416531L;

	private List<Entry> entries = new ArrayList<Entry>();
	public SearchResult() {
	}

	public void addEntry(final Entry entry) {
		entries.add(entry);
	}

	public List<Entry> getEntries()
	{
		return entries;
	}

	@Override
	public String toString() {
		return "SearchResult [entries=" + entries + "]";
	}

	public static class Entry
	{
		private long id;
		
		private String content;

		private String title;

		private String link;
		
		public Entry(long id, String title, String link, String content) {
			this.id = id;
			this.title = title;
			this.link = link;
			this.content = content;
		}
		
		public Entry() {
			
		}

		public void setId(long id) {
			this.id = id;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setLink(String link) {
			this.link = link;
		}

		
		
		public String getLink() {
			return link;
		}

		public long getId() {
			return id;
		}

		public String getContent() {
			return content;
		}

		public String getTitle() {
			return title;
		}

		@Override
		public String toString() {
			return "Entry [id=" + id + ", content=" + content + ", title=" + title + ", link=" + link + "]";
		}
	}
}
