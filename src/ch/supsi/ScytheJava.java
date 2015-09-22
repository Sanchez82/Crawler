package ch.supsi;

import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

public class ScytheJava {
	private PythonInterpreter interpreter;

	public ScytheJava() {
		interpreter = new PythonInterpreter();
	}
	
	private void scytheCategory(String category){
		PySystemState state = new PySystemState();
		state.argv.append (new PyString ("--category="+category));
		state.argv.append (new PyString ("--accountfile=accountfile.txt"));
		interpreter = new PythonInterpreter(null, state);
		interpreter.execfile("scythe.py"); 	
	}
	
	public void scytheSocial() {
		scytheCategory("social");
		//state.argv.append (new PyString ("--single=facebook.com"));
	}
	
	public void scytheMedia(){
		scytheCategory("media");
	}
	public void scytheForums(){
		scytheCategory("forums");
	}
	public void scytheDevelopment(){
		scytheCategory("development");
	}
	public void scytheCommerce(){
		scytheCategory("commerce");
	}
	public void scytheBlogs(){
		scytheCategory("blogs");
	}
	public void scytheEmail(){
		scytheCategory("email");
	}
}
