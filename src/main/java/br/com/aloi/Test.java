package br.com.aloi;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
static String pattern = ">\\{(?<x>[\\w ]*)}<";
	public static void main(String[] args) {
		String html = "<body lang=PT-BR style='tab-interval:35.4pt'>\n" + 
				"\n" + 
				"<div class=WordSection1>\n" + 
				"\n" + 
				"<p class=MsoNormal align=center style='text-align:center'><span\n" + 
				"style='font-size:24.0pt;line-height:105%'><o:p>&nbsp;</o:p></span></p>\n" + 
				"\n" + 
				"<p class=MsoNormal align=center style='text-align:center'><span\n" + 
				"style='font-size:24.0pt;line-height:105%'><o:p>&nbsp;</o:p></span></p>\n" + 
				"\n" + 
				"<p class=MsoNormal align=center style='text-align:center'><span\n" + 
				"style='font-size:24.0pt;line-height:105%'>{TITULO}<o:p></o:p></span></p>\n" + 
				"\n" + 
				"<p class=MsoNormal align=center style='text-align:center'><span\n" + 
				"style='font-size:18.0pt;line-height:105%'>{Nome do Autor}<o:p></o:p></span></p>\n" + 
				"\n" + 
				"<span style='font-size:18.0pt;font-family:\"Times New Roman\",serif;mso-fareast-font-family:\n" + 
				"Calibri;mso-fareast-theme-font:minor-latin;mso-bidi-theme-font:minor-bidi;\n" + 
				"mso-ansi-language:PT-BR;mso-fareast-language:EN-US;mso-bidi-language:AR-SA'><br\n" + 
				"clear=all style='mso-special-character:line-break;page-break-before:always'>\n" + 
				"</span>\n" + 
				"\n" + 
				"<p class=MsoNormal style='margin-bottom:0in;margin-bottom:.0001pt;line-height:\n" + 
				"normal'><span style='font-size:18.0pt'><o:p>&nbsp;</o:p></span></p>\n" + 
				"\n" + 
				"<h1>[[[button_ok]]]</h1>\n" + 
				"\n" + 
				"<h2 style='margin-left:.25in;text-indent:0in;mso-list:l1 level1 lfo2'><![if !supportLists]><span\n" + 
				"style='mso-fareast-font-family:\"Times New Roman\";mso-bidi-font-family:\"Times New Roman\"'><span\n" + 
				"style='mso-list:Ignore'>1)<span style='font:7.0pt \"Times New Roman\"'>&nbsp;&nbsp;&nbsp;&nbsp;\n" + 
				"</span></span></span><![endif]>[[[full-1]]]</h2>\n" + 
				"\n" + 
				"<p class=Head3 style='margin-left:.5in;text-indent:-12.8pt;mso-list:l0 level1 lfo3'><![if !supportLists]><span\n" + 
				"style='mso-fareast-font-family:\"Times New Roman\";mso-bidi-font-family:\"Times New Roman\"'><span\n" + 
				"style='mso-list:Ignore'>1)<span style='font:7.0pt \"Times New Roman\"'>&nbsp; </span></span></span><![endif]>[[[full-2]]]</p>\n" + 
				"\n" + 
				"<p class=MsoNormal>[[[normal]]]</p>\n" + 
				"\n" + 
				"<p class=MsoNormal>[[[go]]]</p>\n" + 
				"\n" + 
				"</div>\n" + 
				"\n" + 
				"</body>";

		String patternConfig = "\n\n.*>\\[\\[\\[(?<x>[^\\]]*)\\]\\]\\]<.*";
		Matcher m = Pattern.compile(patternConfig, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE).matcher(html);

		while (m.find()) {
			System.out.printf("%s = %s\n\n",m.group("x"), m.group());

		}
		
		
	}

}
