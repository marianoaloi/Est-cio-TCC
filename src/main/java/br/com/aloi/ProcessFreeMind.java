package br.com.aloi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProcessFreeMind {

	public static void main(String[] args) {
		try {
			new ProcessFreeMind().init();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void init() throws JAXBException, IOException {

		FreeMind TCC = FreeMindFile.getTCC();

		StringBuilder templateData = makeHTML(TCC);
		// String data = new SimpleDateFormat("yyyyMMddhhmm").format(new Date());
		BufferedWriter bw = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(DOCBASE + "tcc_201503838781_" + /* data + */ ".htm"),
						Charset.forName("windows-1252")))
		// );
		;
		bw.write(templateData.toString());
		bw.close();
		// System.out.println(templateData);

	}

	final static String DOCBASE = "/home/maloi/Documents/";

	private StringBuilder makeHTML(FreeMind tCC) throws IOException {
		BufferedReader template = new BufferedReader(
				new InputStreamReader(new FileInputStream(DOCBASE + "template.htm"), Charset.forName("windows-1252")));
		StringBuilder templateData = new StringBuilder();
		String line;
		while ((line = template.readLine()) != null) {
			// templateData.append(StringEscapeUtils.escapeHtml4(line));
			templateData.append(line);
			templateData.append('\r');
			templateData.append('\n');
		}
		template.close();

		Map<String, String> config = new HashMap<String, String>();
		templateData = new StringBuilder(readConfig(templateData, config));
		// templateData = new
		// StringBuilder(Pattern.compile(patternConfig).matcher(templateData).replaceAll(""));

		changeMaket(tCC.getNode(), templateData);

		insertData(config, tCC, templateData);

		citation(config, templateData);

		return templateData;
	}

	private static final String citationPattern = "!##(?<a>[^(]*)\\((?<b>[^)]*)\\)##!";

	class cittation {
		String footpage;
		String mension;
		String text;

		public cittation(String mension, String footpage, String text) {
			this.mension = mension;
			this.footpage = footpage;
			this.text = text;

		}

	}

	private void citation(Map<String, String> config, StringBuilder templateData) {

		Matcher m = Pattern.compile(citationPattern).matcher(templateData);
		ArrayList<cittation> cittations = new ArrayList<cittation>();

		while (m.find()) {
			cittations.add(new cittation(m.group("a"), m.group("b"), m.group()));
		}
		int count = 1;
		String intorodapé = "";
		/* Fix rodapé info */

		String rodape = config.get("rodapé");
		rodape = rodape.substring(rodape.indexOf("<span"), rodape.lastIndexOf("/a>") + 3);
		config.put("rodapé", rodape);

		/* Fix rodapé info */

		for (Iterator<cittation> it = cittations.iterator(); it.hasNext(); count++) {
			cittation cittation = (cittation) it.next();

			String mensionText = getFieldIntoConfig("rodapé", config, cittation.mension, count);
			mensionText = mensionText.replace("[1]", "[" + count + "]");
			replaceStringBuilder(cittation.text, mensionText, templateData);

			String intorodapéinto = getFieldIntoConfig("intorodape", config, cittation.footpage, count);
			intorodapéinto = intorodapéinto.replace("[1]", "[" + count + "]");
			intorodapé += intorodapéinto;
		}

		templateData.insert(templateData.lastIndexOf("</div>"), intorodapé);
		

	}

	private String getFieldIntoConfig(String conf, Map<String, String> config, String textReplace, int count) {
		String resultText = config.get(conf).replace("[[[" + conf + "]]]", textReplace.trim());

		Matcher referenceMat = Pattern.compile("(?<a>[_]*)ftn(?<b>[ref]*)(?<n>\\d)").matcher(resultText);
		while (referenceMat.find()) {
			String key = referenceMat.group();
			resultText = resultText.replace(key, referenceMat.group("a") + "ftn" + referenceMat.group("b") + (count));

		}
		return resultText;
	}

	private void insertData(Map<String, String> config, FreeMind tCC, StringBuilder templateData) {
		StringBuilder result = new StringBuilder();
		InsertRecursibve(tCC.getNode(), result, config);
		templateData.insert(templateData.lastIndexOf("WordSection") + "WordSection".length() + 2, result);

	}

	private void InsertRecursibve(List<Node> listNode, StringBuilder result, Map<String, String> config) {
		if (listNode != null)
			for (Node internal : listNode) {
				if (internal.icon != null) {
					String BUILTIN = internal.icon.BUILTIN;
					if (config.containsKey(BUILTIN)) {
						result.append(config.get(BUILTIN).replace(">[[[" + BUILTIN + "]]]<",
								">" + (internal.TEXT != null && !"go".equals(BUILTIN) ? internal.TEXT : "") + "<"));
						if (internal.richcontent != null) {
							Document doc = Jsoup.parse(internal.richcontent);
							Elements body = doc.select("body");
							body.select("p").forEach(e -> {
								if (e.classNames().size() == 0)
									e.addClass("MsoNormal");
							});
							result.append(body.html());

						}
					}
				}

				InsertRecursibve(internal.getNode(), result, config);
			}

	}

	final static String patternConfig = ".*[>]*\\[\\[\\[(?<x>[^\\]]*)\\]\\]\\][<]*.*";

	private String readConfig(StringBuilder templateData, Map<String, String> config) {

		Document doc = Jsoup.parse(templateData.toString());

		selectIntoDocConf(config, "div[style='mso-element:footnote']", doc);
		selectIntoDocConf(config, "p", doc);
		selectIntoDocConf(config, "h1", doc);
		selectIntoDocConf(config, "h2", doc);

		return doc.html();
	}

	private void selectIntoDocConf(Map<String, String> config, String selector, Document doc) {
		Elements els = doc.select(selector);

		for (Element element : els) {
			Matcher m = Pattern.compile(patternConfig).matcher(element.text());

			while (m.find()) {
				config.put(m.group("x"), element.toString());
				element.remove();
			}
		}

	}

	private void prepare(StringBuilder templateData) {
		Matcher m = Pattern.compile(patternConfig).matcher(templateData);

		while (m.find()) {
			int beginFuckingTag = m.start();
			int last = m.end();

			boolean firstBreak = false;
			boolean secondBreak = false;
			do {
				firstBreak = templateData.charAt(beginFuckingTag--) == '\n';
				secondBreak = firstBreak && templateData.charAt(--beginFuckingTag) == '\n';
			} while (!secondBreak);

			String modified = templateData.substring(beginFuckingTag, last);
			modified = modified.replace('\n', ' ');
			modified = modified.replace('\r', ' ');
			templateData.replace(beginFuckingTag, last, modified);

		}

	}

	private void changeMaket(List<Node> listNode, StringBuilder templateData) {
		if (listNode != null)
			for (Node node : listNode) {
				changeMaket(node.getNode(), templateData);

				Matcher m = Pattern.compile("\\>\\{\\{\\{" + node.TEXT + "\\}\\}\\}\\<").matcher(templateData);

				while (m.find()) {

					String findString = m.group();
					int begin = templateData.indexOf(findString);

					if (begin >= 0)
						templateData.replace(begin, begin + findString.length(),
								">" + onlyText(node.richcontent) + "<");

				}

				replaceStringBuilder(">{{{" + node.TEXT + "}}}<", ">" + onlyText(node.richcontent) + "<", templateData);
			}

	}

	private void replaceStringBuilder(String findString, String replaceText, StringBuilder templateData) {
		int begin = templateData.indexOf(findString);

		if (begin >= 0)
			templateData.replace(begin, begin + findString.length(), replaceText);

	}

	private String onlyText(String tEXT) {
		if (tEXT == null)
			return "";
		Document doc = Jsoup.parse(tEXT);
		return StringEscapeUtils.escapeHtml4(doc.text().trim());
	}

}
