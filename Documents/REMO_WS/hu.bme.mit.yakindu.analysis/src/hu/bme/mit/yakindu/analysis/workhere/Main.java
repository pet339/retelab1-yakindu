package hu.bme.mit.yakindu.analysis.workhere;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;
import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		ArrayList<EventDefinition> edList = new ArrayList<>();
		ArrayList<VariableDefinition> vdList = new ArrayList<>();
		
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			
			if(content instanceof VariableDefinition) {
				VariableDefinition vd = (VariableDefinition) content;
				vdList.add(vd);
			}
			if(content instanceof EventDefinition) {
				EventDefinition ed = (EventDefinition) content;
				edList.add(ed);
			}
			
			
//			if(content instanceof VariableDefinition) {
//				VariableDefinition vd = (VariableDefinition) content;
//				System.out.println(vd.getName());
//			}
//			
//			if(content instanceof EventDefinition) {
//				EventDefinition ed = (EventDefinition) content;
//				System.out.println(ed.getName());
//			}
			
			
//			if(content instanceof State) {
//				State state = (State) content;
//				System.out.println(state.getName());
//				
//				for (Transition t : state.getOutgoingTransitions()) {
//					State outGoing = (State) t.getTarget();
//					System.out.println(state.getName() + " -> " + outGoing.getName());
//				}
//				
//				if (state.getOutgoingTransitions().size() == 0) {
//					System.out.println("Csapda: " + state.getName());
//				}
//				
//				if ("".equals(state.getName())) {
//					state.setName("NoName");
//					System.out.println("Aj??nlott n??v: "+state.getName());
//				}
//			}
		}
		
		
		System.out.println(
				"public static void main(String[] args) throws IOException {\n"+
				"	ExampleStatemachine s = new ExampleStatemachine();\n"+
				"	s.setTimer(new TimerService());\n"+
				"	RuntimeService.getInstance().registerStatemachine(s, 200);\n"+
				"	s.init();\n"+
				"	s.enter();\n"+
				"	while(true) {\n"+
				"		Scanner sc = new Scanner(System.in);\n"+
				"		String line = sc.nextLine();\n"+
				"		switch(line) {"
				);
		
				for(int i = 0; i < edList.size(); i++) {
					StringBuilder sb = new StringBuilder(edList.get(i).getName());
					sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
					System.out.println(
							"		case \""+edList.get(i).getName()+"\": {\n"+
							"			s.raise"+sb.toString()+"();\n"+
							"			s.runCycle();\n"+
							"			print(s);\n"+
							"		}break;"
					);
				}
				System.out.println(
				"		case \"exit\": {     \n"+
				"			System.exit(0);\n"+
				"		}break;\n"+  
				"		}\n"+
				"	}\n"+
				"}"
				);
				System.out.println("public static void print(IExampleStatemachine s) {");
				for(int i = 0; i < vdList.size(); i++) {
					StringBuilder sb = new StringBuilder(vdList.get(i).getName());
					sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
					System.out.println("	System.out.println(\""+sb.charAt(0)+" = \" + s.getSCInterface().get"+sb.toString()+"());");
				}
				System.out.println("}");
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
