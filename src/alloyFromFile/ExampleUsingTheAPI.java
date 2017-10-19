package alloyFromFile;


import javax.swing.JFileChooser;
import javax.swing.JMenu;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorSyntax;
import edu.mit.csail.sdg.alloy4.Pair;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4viz.VizGUI;


public class ExampleUsingTheAPI {

	public static void loadModule(String chosenFile) {

		A4Reporter rep = new A4Reporter();
		try {
			CompModule loaded = CompUtil.parseEverything_fromFile(rep, null, chosenFile);
			showSolutions(rep, loaded);
		} catch (Err e) {
			e.printStackTrace();
		}
	}

	public static String openDialog() {

		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(chooser);
		return chooser.getSelectedFile().getAbsolutePath();
	}

	public static void showSolutions(A4Reporter rep, CompModule loaded) {

		A4Options options = new A4Options();
		options.solver = A4Options.SatSolver.SAT4J;

		try {
			Command command = makeCommand(loaded);
			System.out.println("============ Command: " + command + ": ============");
			A4Solution ans = TranslateAlloyToKodkod.execute_commandFromBook(rep, loaded.getAllReachableSigs(), command,
					options);
			while(ans.satisfiable()) {
				ans.writeXML("Sample/instance.xml");
				JMenu menuFile = new JMenu("File");
				VizGUI viz = new VizGUI(false, "Sample/instance.xml", menuFile);

			}

		} catch (Err e) {
			e.printStackTrace();
		}

	}

	public static Command makeCommand(CompModule loaded) throws ErrorSyntax {
		Expr expr = null;
		for (Pair<String, Expr> exp : loaded.getAllFacts()) {
			expr = exp.b;
		}
		System.out.println(expr.toString());
		return new Command(false, 15, 3, 3, expr);
	}

	public static void main(String[] args) {
		String chosenFile = openDialog();
		loadModule(chosenFile);
	}
}
