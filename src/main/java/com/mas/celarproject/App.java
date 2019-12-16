package com.mas.celarproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;
import org.jdom2.Element;

public class App 
{
	//Creates the <agents> tag
	static public Element createAgentTag() {
		int i=1;
		Element agents = new Element("agents");
		try {
			List<String> fileLines = Files.readAllLines(Paths.get("var.txt"), Charset.defaultCharset());
            
            agents.setAttribute("nbAgents",""+fileLines.size());
            for ( String line : fileLines) {
            	Element agent = new Element("agent");
            	agent.setAttribute("name","agent"+i);
            	agents.addContent(agent);
            	i++;
            }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return agents;
		
	}
	
	//Creates the <domain> tag
	static public Element createDomainTag() {
		Element domains = new Element("domains");
		try {
			List<String> fileLines = Files.readAllLines(Paths.get("dom.txt"), Charset.defaultCharset());
			fileLines.remove(0);
			
			domains.setAttribute("nbDomains",""+fileLines.size());
			for(String line: fileLines) {
				String[] separator = line.trim().split("  ");
				Element domain = new Element("domain");
				domain.setAttribute("number",separator[0]);
				domain.setAttribute("nbValues",separator[1]);
				domain.setText(separator[2]+".."+separator[fileLines.size()+1]);
				domains.addContent(domain);
				//System.out.println(separator[fileLines.size()+1]);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return domains;
	}
	
	//Creates the <variables> tag
	static public Element createVariableTag() {
		Element variables = new Element("variables");
		try {
			List<String> fileLines = Files.readAllLines(Paths.get("var.txt"), Charset.defaultCharset());
			
			variables.setAttribute("nbVariables",""+fileLines.size());
			for(String line: fileLines) {
				String[] separator = line.trim().split("  ");
				Element variable = new Element("variable");
				variable.setAttribute("name","V"+separator[0]);
				variable.setAttribute("nbValues",separator[1]);
				variable.setAttribute("agent","agent"+separator[0]);
				variables.addContent(variable);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return variables;
	}
	
	//Creates the <constraints> tag
	static public Element createConstraintTag() {
		Element constraints = new Element("constraints");
		
		try {
			List<String> fileLines = Files.readAllLines(Paths.get("ctr.txt"), Charset.defaultCharset());
			int i=0;
			constraints.setAttribute("nbConstraints",""+fileLines.size());
			for(String line : fileLines) {
				i++;
				String separator[] = line.trim().split("[\\s]+");
				
				Element constraint = new Element("constraint");
                Element parameters = new Element("parameters");
                
				if(separator[3].equals("=")) {
					constraint.setAttribute("name","C"+i);
					constraint.setAttribute("scope","V"+separator[0]+" V"+separator[1]);
					constraint.setAttribute("arity","2");
					constraint.setAttribute("reference","equals");
					parameters.setText("V"+separator[0]+" V"+separator[1]);
					constraint.addContent(parameters);
					constraints.addContent(constraint);
				} else if (separator[3].equals(">")) {
					constraint.setAttribute("name","C"+i);
					constraint.setAttribute("scope","V"+separator[0]+" V"+separator[1]);
					constraint.setAttribute("arity","2");
					constraint.setAttribute("reference","greaterThan");
					parameters.setText("V"+separator[0]+" V"+separator[1]);
					constraints.addContent(parameters);
					constraints.addContent(constraint);
				}
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return constraints;
	}
	
	//Generates the file problem.xml to use with FRODO
	static public void generateProblem() {
		
        try {
			Element instance = new Element("instance");
			Document document = new Document(instance);
			
			Element presentation = new Element("presentation");
			presentation.setAttribute("name","CELAR Problem");
			presentation.setAttribute("maxConstraintArity","2");
			presentation.setAttribute("format","XCSP 2.1_FRODO");
			presentation.setAttribute("maximizing","false");
			
			instance.addContent(presentation);
			instance.addContent(createAgentTag());
			instance.addContent(createDomainTag());
			instance.addContent(createVariableTag());
			instance.addContent(createConstraintTag());

			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            xmlOutputter.output(document, new FileOutputStream("problem.xml"));
			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public static void main( String[] args )
    {
        generateProblem();
        //createConstraintTag();
    }
}
