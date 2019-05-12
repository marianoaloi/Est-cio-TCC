package br.com.aloi;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.commons.text.StringEscapeUtils;

public class FreeMindFile {
	static FreeMind getTCC() throws JAXBException, IOException {
//
		JAXBContext jaxbContext = JAXBContext.newInstance(FreeMind.class);
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

		// output pretty printed
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//		jaxbMarshaller.marshal(f, System.out);

		FreeMind tcc = (FreeMind) jaxbContext.createUnmarshaller().unmarshal(getSource());
		return tcc;
	}

	private static InputStream getSource() throws IOException {
		BufferedReader fileIn = new BufferedReader(new FileReader(ProcessFreeMind.DOCBASE + "TCC.mm"));
		StringBuilder result = new StringBuilder();
		String line;
		boolean encode = false;
		while ((line = fileIn.readLine()) != null) {
			if (line.contains("richcontent")) {
				encode = !encode;

				if (encode) {
					result.append("<richcontent>&lt;html&gt;");
				} else {
					result.append("&lt;/html&gt;</richcontent>");
				}
			}

			else if (encode) {
				result.append(StringEscapeUtils.escapeHtml4(line));
			} else {
				result.append(line);
			}
		}
		fileIn.close();
		return new ByteArrayInputStream(result.toString().getBytes(Charset.forName("UTF-8")));
	}

//	private FreeMind newFreeMind() {
//		FreeMind f = new FreeMind();
//		ArrayList<Node> nodes;
//
//		f.setNode(nodes = new ArrayList<Node>());
//
//		nodes.add(newNode());
//
//		return f;
//	}
//
//	private Node newNode() {
//		Node n = new Node();
//		n.CREATED = "reerer";
//		n.MODIFIED = "reerer";
//		n.ID = "reerer";
//		n.TEXT = "reerer";
//		ArrayList<Node> nan;
//		n.setNode(nan = new ArrayList<Node>());
//		Node nn;
//		nan.add(nn = new Node());
//		nn.CREATED = "reerer";
//		nn.MODIFIED = "reerer";
//		nn.ID = "reerer";
//		nn.TEXT = "reerer";
//
//		nn.richcontent = "<html>\n" + "  <head>\n" + "    \n" + "  </head>\n" + "  <body>\n" + "    <p>\n"
//				+ "      O trabalho visa trazer a hist&#243;ria das legisla&#231;&#245;es sobre o deficiente demonstrando que o nosso pais vem evoluindo com tratados e conven&#231;&#245;es internacionais, al&#233;m de leis do nosso congresso at&#233; os tratados internacionais de direitos humanos que no caso do deficiente &#233; equivalente a emenda constitucional sendo essas lei com poder constitucional. Assim &#233; passado para interpretar como toda a legisla&#231;&#227;o e o Estatuto do Deficiente fundamentou o deficiente no mercado de trabalho e vem a facilitar responder se a legisla&#231;&#227;o est&#225; aderente com as necessidades para igualar o trabalho do deficiente. A partir de uma vis&#227;o cotidiana &#233; trazida a ampla vis&#227;o de agentes do estatuto sobre as melhorias e dificuldades ainda encontradas. &#201; conclu&#237;do o trabalho com o uma analise de como a legisla&#231;&#227;o est&#225; ajudando o deficiente.\n"
//				+ "    </p>\n" + "  </body>\n" + "</html>";
//		return n;
//	}
}
