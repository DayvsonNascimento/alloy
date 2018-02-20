package alloyFromFile;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import edu.mit.csail.sdg.alloy4.A4Reporter;
import edu.mit.csail.sdg.alloy4.Err;
import edu.mit.csail.sdg.alloy4.ErrorSyntax;
import edu.mit.csail.sdg.alloy4compiler.ast.Command;
import edu.mit.csail.sdg.alloy4compiler.ast.Expr;
import edu.mit.csail.sdg.alloy4compiler.parser.CompModule;
import edu.mit.csail.sdg.alloy4compiler.parser.CompUtil;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Options;
import edu.mit.csail.sdg.alloy4compiler.translator.A4Solution;
import edu.mit.csail.sdg.alloy4compiler.translator.TranslateAlloyToKodkod;
import edu.mit.csail.sdg.alloy4viz.VizGUI;

public class ExampleUsingTheAPI {

	/*
	 * loads module from the desired file, calls the method to show the solutions
	 */
	public static void loadModule(String chosenFile) {

		A4Reporter rep = new A4Reporter();
		try {
			if (chosenFile != null) {
				CompModule loaded = CompUtil.parseEverything_fromFile(rep, null, chosenFile);
				runCommands(rep, loaded);
			}
		} catch (Err e) {
			e.printStackTrace();
		}
	}

	/*
	 * opens a dialog box to select the file
	 */
	public static String openDialog() {

		JFileChooser chooser = new JFileChooser();
		chooser.showOpenDialog(chooser);

		return chooser.getSelectedFile().getAbsolutePath();

	}

	/*
	 * run a command for the alloy model
	 */
	public static void runCommands(A4Reporter rep, CompModule loaded) {

		A4Options options = new A4Options();
		options.solver = A4Options.SatSolver.SAT4J;

		try {
			Command command = makeCommand(loaded);
			A4Solution ans = TranslateAlloyToKodkod.execute_commandFromBook(rep, loaded.getAllReachableSigs(), command,
					options);
			showSolutions(ans);
		} catch (Err e) {
			e.printStackTrace();
		}

	}

	/*
	 * shows a representation using VizGUI for the model only if it's satisfiable
	 * 
	 */
	public static void showSolutions(A4Solution ans) {
		if (ans.satisfiable()) {
			try {
				ans.writeXML("Sample/instance.xml");
				JMenu menuFile = new JMenu("File");
				VizGUI viz = new VizGUI(false, "Sample/instance.xml", menuFile);
			} catch (Err e) {
				e.printStackTrace();
			}

		}
	}

	/*
	 * makes a default run command for the alloy specification
	 */
	public static Command makeCommand(CompModule loaded) throws ErrorSyntax {
		Expr expr = loaded.getAllReachableFacts();
		return new Command(false, 3, 3, 3, expr);
	}

	/*
	 * simple main method to test the methods
	 */
	public static void main(String[] args) {
		String chosenFile = openDialog();
		loadModule(chosenFile);
	}
}
