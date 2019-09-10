

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import rps.Constants;

import java.io.*;
import java.util.*;

public class CompileCode {

	/**
	 * Attempts to compile the java file <player_name>.java
	 * @param filename that determined the file to be compiled player_name + ".java"
	 * @return true if compiled successfully, otherwise false
	 */
	public static String compile(String filename) {

		try {
			Thread t = new Thread(new CompileThread(filename));
			t.start();
			t.join();
		} catch (InterruptedException e) {
			Constants.log("Exception thrown while compiling " + filename + " " + e.getMessage());
		}
		return CompileThread.getCompileResults();
	}

	private static class CompileThread implements Runnable {

		String filename;
		static String results = "";

		public CompileThread(String filename) {
			results = "";
			this.filename = filename;
		}

		public static String getCompileResults() {
			return results;
		}
		@Override
		public void run() {

			try {
				File root = new File(Constants.PLAYERS_FILEPATH);
				File sourceFile = new File(root, filename);
				sourceFile.getParentFile().mkdirs();

				JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
				DiagnosticCollector<JavaFileObject> diagnosticsCollector = new DiagnosticCollector<JavaFileObject>();
				StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnosticsCollector, null, null);
				Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
				JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticsCollector, null, null, compilationUnits);

				boolean success = task.call();
				if (!success) {
					List<Diagnostic<? extends JavaFileObject>> diagnostics = diagnosticsCollector.getDiagnostics();
					String errors = "";
					for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics) {
						errors += "Error line " + diagnostic.getLineNumber() + "\n" + diagnostic.getMessage(null) + "\n\n";
					}
					fileManager.close();

					results = errors;

				} else {

					fileManager.close();
				}

			} catch(Exception ex) {
				Constants.log("Failed when compiling " + filename + "\n" + ex.getMessage());
			}
		}
	}

	public static void main(String[] args) {
		CompileCode.compile("player1.java");
	}
}