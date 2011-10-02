package de.l3s.boilerpipe.demo;

import java.net.URL;
import java.util.List;

import de.l3s.boilerpipe.BoilerpipeExtractor;
import de.l3s.boilerpipe.extractors.CommonExtractors;
import de.l3s.boilerpipe.sax.ImageExtractor;

public class ImageExtractorDemo {
	public static void main(String[] args) throws Exception {
		URL url = new URL(
				"http://www.spiegel.de/wissenschaft/natur/0,1518,789176,00.html");
		
		// choose from a set of useful BoilerpipeExtractors...
		final BoilerpipeExtractor extractor = CommonExtractors.ARTICLE_EXTRACTOR;
//		final BoilerpipeExtractor extractor = CommonExtractors.DEFAULT_EXTRACTOR;
//		final BoilerpipeExtractor extractor = CommonExtractors.CANOLA_EXTRACTOR;
//		final BoilerpipeExtractor extractor = CommonExtractors.LARGEST_CONTENT_EXTRACTOR;

		final ImageExtractor ie = ImageExtractor.INSTANCE;
		
		List<String> imgUrls = ie.getEnclosedImages(url, extractor);
		for(String u : imgUrls) {
			System.out.println("* "+u);
		}

	}
}
