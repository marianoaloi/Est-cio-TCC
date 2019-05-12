package br.com.aloi;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType (XmlAccessType.FIELD)
public class Node {

	private List<Node> node;
	@XmlAttribute(name="CREATED")
	String CREATED;


	@XmlAttribute(name="ID")
	String ID;


	@XmlAttribute(name="MODIFIED")
	String MODIFIED;


	@XmlAttribute(name="TEXT")
	String TEXT;

	Icon icon;
	//RichContent richcontent;
	String richcontent;

	@Override
	public String toString() {
		String result = "";
		if(node!=null)
		for (Node node2 : node) {
			result  += node2 + "\n";
		}
		return "Node [BUILTIN="+(icon!=null?icon.BUILTIN:"")+", CREATED=" + CREATED + ", ID=" + ID + ", MODIFIED=" + MODIFIED + ", TEXT=" + TEXT + "\n"
				+ "node=" + result + "\n"
						+ "RichContent="+(richcontent!=null?richcontent:"")+"]";
	}
	
	public List<Node> getNode() {
		return node;
	}

	public void setNode(List<Node> node) {
		this.node = node;
	}

}
