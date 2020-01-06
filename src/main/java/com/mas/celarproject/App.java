package com.mas.celarproject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.Document;
import org.jdom2.Element;



public class App {
	//Retrieve the cst.txt
	//the file's path on line 26 only takes double digits numbers from "04" to "11"
	//from scen04 to scen11, since the cst files from scen00 to scen03 do not have anything other than plain text in them
	static public List readCst() {
		List<String> values = new ArrayList<String>();
		values.add("0");
		try {
			List<String> fileLines = Files.readAllLines(Paths.get("CELAR/scen09/cst.txt"), Charset.defaultCharset());
			for( String line : fileLines) {
				String[] separator = line.trim().split("[\\s]+", 3);
				values.add(separator[2].toString());

			}
			//System.out.println(values.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return values;
	}
	
	//Creates the <agents> tag
	static public Element createAgentTag() {
		//int i=1;
		Element agents = new Element("agents");
		try {
			//the file's path on line 48 only takes double digits numbers from "00" to "11"
			//from scen00 to scen11
			List<String> fileLines = Files.readAllLines(Paths.get("CELAR/scen00/VAR.TXT"), Charset.defaultCharset());
            
            agents.setAttribute("nbAgents",""+fileLines.size());
            for ( String line : fileLines) {
            	String[] separator = line.trim().split("[\\s]+",2);
            	Element agent = new Element("agent");
            	agent.setAttribute("name","agent"+separator[0]);
            	agents.addContent(agent);
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
			//the file's path on line 74 only takes double digits numbers from "00" to "11"
			//from scen00 to scen11
			List<String> fileLines = Files.readAllLines(Paths.get("CELAR/scen00/DOM.TXT"), Charset.defaultCharset());
			fileLines.remove(0);
			
			domains.setAttribute("nbDomains",""+fileLines.size());
			for(String line: fileLines) {
				String[] separator = line.trim().split("[\\s]+",3);
				Element domain = new Element("domain");
				domain.setAttribute("name",separator[0].toString());
				domain.setAttribute("nbValues",separator[1]);
				domain.setText(separator[2].toString());
				domains.addContent(domain);
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
			//the file's path on line 98 only takes double digits numbers from "00" to "11"
			//from scen00 to scen11
			List<String> fileLines = Files.readAllLines(Paths.get("CELAR/scen00/VAR.TXT"), Charset.defaultCharset());
			
			variables.setAttribute("nbVariables",""+fileLines.size());
			for(String line: fileLines) {
				String[] separator = line.trim().split("[\\s]+",2);
				Element variable = new Element("variable");
				variable.setAttribute("name","V"+separator[0].toString());
				variable.setAttribute("domain",separator[1].substring(0, 1));
				variable.setAttribute("agent","agent"+separator[0].toString());
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
			//the file's path on line 123 only takes double digits numbers from "00" to "11"
			//from scen00 to scen11
			List<String> fileLines = Files.readAllLines(Paths.get("CELAR/scen00/CTR.TXT"), Charset.defaultCharset());
			int i=0;
			constraints.setAttribute("nbConstraints",""+fileLines.size());
			for(String line : fileLines) {
				i++;
				String separator[] = line.trim().split("[\\s]+",6);

				Element constraint = new Element("constraint");
                Element parameters = new Element("parameters");
                
                String val = "0";
                String num = "";
                List<String> cst = readCst();
                int x = 0;
                if(separator.length==6) {
                	val = separator[5].toString();
                	
                	if(val.equals("0")) {
                		x=0;
                		num = cst.get(x);
                	} else if(val.equals("1")) {
                		x=1;
                		num = cst.get(x);
                	} else if(val.equals("2")) {
                		x=2;
                		num = cst.get(x);
                	} else if(val.equals("3")) {
                		x=3;
                		num = cst.get(x);
                	} else if(val.equals("4")) {
                		x=4;
                		num = cst.get(x);
                	} else if(val.equals("5")) {
                		x=5;
                		num = cst.get(x);
                	}
                } else {
                	num="0";
                }
                //System.out.println(num);
                //num = cst.get(x);
                
				if(separator[3].toString().equals("=")) {
					constraint.setAttribute("name","C"+i);
					constraint.setAttribute("scope","V"+separator[0].toString()+" V"+separator[1].toString());
					constraint.setAttribute("arity","2");
					constraint.setAttribute("reference","eq");
					//if(val!="0") {
						parameters.setText("V"+separator[0].toString()+" V"+separator[1].toString()+" "+separator[4].toString()+" "+num);
					//} else parameters.setText("V"+separator[0].toString()+" V"+separator[1].toString()+" "+separator[4].toString()+" "+val);
					constraint.addContent(parameters);
					constraints.addContent(constraint);
				} else if (separator[3].equals(">")) {
					constraint.setAttribute("name","C"+i);
					constraint.setAttribute("scope","V"+separator[0].toString()+" V"+separator[1].toString());
					constraint.setAttribute("arity","2");
					constraint.setAttribute("reference","gt");
					//if(val!="0") {
						parameters.setText("V"+separator[0].toString()+" V"+separator[1].toString()+" "+separator[4].toString()+" "+num);
					//} else parameters.setText("V"+separator[0].toString()+" V"+separator[1].toString()+" "+separator[4].toString()+" "+val);
					constraint.addContent(parameters);
					constraints.addContent(constraint);
				}	
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return constraints;
	}
	
	//Creates the <predicates> tag
	static public Element createPredicatesTag() {
		Element predicates = new Element("predicates");
		
		predicates.setAttribute("nbPredicates","2");
		
		//predicate "greater than - gt"
		Element predicate = new Element("predicate");
		predicate.setAttribute("name", "gt");
		predicate.setAttribute("return", "int");
		Element parameters = new Element("parameters");
		parameters.setText("int x int y int z int val");
		Element expression = new Element("expression");
		Element functional = new Element("functional");
		functional.setText("if(gt(abs(sub(x,y))),z),0,val)");
		
		expression.addContent(functional);
		predicate.addContent(parameters);
		predicate.addContent(expression);
		predicates.addContent(predicate);
		
		//predicate "equals - eq"
		Element predicate2 = new Element("predicate");
		predicate2.setAttribute("name", "eq");
		predicate2.setAttribute("return", "int");
		Element parameters2 = new Element("parameters");
		parameters2.setText("int x int y int z int val");
		Element expression2 = new Element("expression");
		Element functional2 = new Element("functional");
		functional2.setText("if(eq(abs(sub(x,y))),z),0,val)");
		
		expression2.addContent(functional2);
		predicate2.addContent(parameters2);
		predicate2.addContent(expression2);
		predicates.addContent(predicate2);
		
		return predicates;
	}
	
	//Generates the file problem.xml to use with FRODO
	static public void generateProblem() {
		//readCst("05");
        try {
			Element instance = new Element("instance");
			Document document = new Document(instance);
			
			Element presentation = new Element("presentation");
			presentation.setAttribute("name","celarProblem");
			presentation.setAttribute("maxConstraintArity","2");
			presentation.setAttribute("format","XCSP 2.1_FRODO");
			presentation.setAttribute("maximizing","false");
			
			instance.addContent(presentation);
			instance.addContent(createAgentTag());
			instance.addContent(createDomainTag());
			instance.addContent(createVariableTag());
			instance.addContent(createPredicatesTag());
			instance.addContent(createConstraintTag());

			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
            xmlOutputter.output(document, new FileOutputStream("problem00.xml"));
			
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public static void main( String[] args ){
        generateProblem();
    }
}