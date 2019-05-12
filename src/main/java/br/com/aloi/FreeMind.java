package br.com.aloi;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="map")
@XmlAccessorType (XmlAccessType.FIELD)
public class FreeMind {

	private List<Node> node;

	public List<Node> getNode() {
		return node;
	}

	public void setNode(List<Node> node) {
		this.node = node;
	}

	@Override
	public String toString() {
		String result = "";
		for (Node node2 : node) {
			result  += node2 + "\n";
		}
		return "FreeMind [node=" + result + "]";
	}

}
