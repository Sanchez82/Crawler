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
		state.argv.append (new PyString ("--output=result1.txt"));
		interpreter = new PythonInterpreter(null, state);
		interpreter.execfile("scythe.py"); 
		interpreter.close();
//		PyCode m = interpreter.compile("scythe.py");
//		m.
//		String s = "ciao";
//		System.out.println(interpreter.compile("scythe.py")+s); 
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
