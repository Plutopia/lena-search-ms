package org.pluto.ai.search.core;

/**
 * Das ist eine Java Klasse
 */

import java.util.Iterator;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.params.SolrParams;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

@Stateless
@LocalBean
@Path("google")
public class LenaSearchBean {

	public static Logger logger = LogManager.getLogger();

	public String GOOGLE_API_KEY = "AIzaSyCjKO-kyZXwYd4tnLQLWqfz5mrIoFvbgYk";
	
	public String SEARCH_ENGINE_ID = "bda3770ef750f4c66";
	
	public String QUERY = "";

	private static final int HTTP_REQUEST_TIMEOUT = 3 * 600000;

	@GET
	@Path("execute/{searchParam}")
	@Produces(MediaType.APPLICATION_JSON)
	public SearchResult search(@PathParam("searchParan") String searchString) {
		List<Result> results = searchGoogle();
		SearchResult searchResult = new SearchResult();
	
		for (int i=0; i<results.size() ;i++) {
			Result result = results.get(i);
			searchResult.addEntry(new SearchResult.Entry(++i, result.getTitle(),
											  			 result.getSnippet(),
											  			 result.getLink()));
		}

		return searchResult;

	}

	public List<Result> searchGoogle() {

		Customsearch customsearch = null;

		try {
			customsearch = new Customsearch(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
				public void initialize(HttpRequest httpRequest) {
					try {
						// set connect and read timeouts
						httpRequest.setConnectTimeout(HTTP_REQUEST_TIMEOUT);
						httpRequest.setReadTimeout(HTTP_REQUEST_TIMEOUT);

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<Result> resultList = null;
		try {
			Customsearch.Cse.List list = customsearch.cse().list("lena meyer-landrut");
			list.setKey(GOOGLE_API_KEY);
			list.setCx(SEARCH_ENGINE_ID);
			Search results = list.execute();
			resultList = results.getItems();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public void initSolr() {

		String zkHostString = "zkServerA:2181,zkServerB:2181,zkServerC:2181/solr";

		// Using already running Solr nodes
		SolrClient solr = new CloudSolrClient.Builder().withSolrUrl("http://localhost:15000/solr").build();
		try {
			solr.query(new SolrParams() {

				@Override
				public String[] getParams(String param) {
					// TODO Auto-generated method stub
					return new String[] { "q", "lena" };
				}

				@Override
				public Iterator<String> getParameterNamesIterator() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String get(String param) {
					// TODO Auto-generated method stub
					return null;
				}
			});
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}
}
