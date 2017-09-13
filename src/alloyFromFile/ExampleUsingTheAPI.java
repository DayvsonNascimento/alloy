package alloyFromFile;

import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.ConstList;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;

public class ExampleUsingTheAPI {

	public static void loadModule(String fileName) {

		A4Reporter rep = new A4Reporter();
		
		try {
			CompModule loaded = CompUtil.parseEverything_fromFile(rep, null, fileName);
			printSolutions(rep, loaded);
		} catch (Err e) {
			e.printStackTrace();
		}
	}

	public static void printSolutions(A4Reporter rep, CompModule loaded) {

		A4Options options = new A4Options();
		options.solver = A4Options.SatSolver.SAT4J;
		
		try {
			for (Command command : loaded.getAllCommands()) {
				System.out.println("============ Command: " + command + ": ============");
				A4Solution ans = TranslateAlloyToKodkod.execute_commandFromBook(rep, loaded.getAllReachableSigs(),
						command, options);
				System.out.println(ans);
			}
		} catch (Err e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		loadModule("Sample/test.als");
	}
}
