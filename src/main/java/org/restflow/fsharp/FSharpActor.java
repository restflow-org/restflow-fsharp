package org.restflow.fsharp;

import org.restflow.actors.ActorScriptBuilder;
import org.restflow.actors.AugmentedScriptActor;

public class FSharpActor extends AugmentedScriptActor {

	@Override
	public ActorScriptBuilder getNewScriptBuilder() {
		return new FSharpActor.ScriptBuilder();
	}
	
	@Override
	public synchronized String getScriptRunCommand() {
		return "fsi.exe --quiet";
	}

	@Override
	public DataSerializationFormat getOutputSerializationFormat() {
		return DataSerializationFormat.YAML;
	}

	@Override
	protected String adjustStderr(String completeStderr) {
		String trimmedStderr = completeStderr.trim();
		if (trimmedStderr.trim().endsWith("- Exit...")) {
			return trimmedStderr.substring(0, trimmedStderr.length() - 9).trim();
		} else {
			return completeStderr;
		}
	}
	
	public static class ScriptBuilder implements ActorScriptBuilder {

		private StringBuilder _script = new StringBuilder();
		private final static String EOL = System.getProperty("line.separator");

		public ActorScriptBuilder appendCode(String code) {
			_script.append(		code	)
				   .append(		EOL		);
			return this;
		}

		public ScriptBuilder appendSeparator() {
			_script.append(		"//####################################################################################"	)
				   .append(		EOL																							);
			return this;
		}

		public ScriptBuilder appendBlankLine() {
			_script.append(		EOL		);
			return this;
		}
	
		public ScriptBuilder appendComment(String text) {
			_script.append(		"// "	)
				   .append(		text	)
			   	   .append(		EOL		);
			return this;
		}
		
		public void appendScriptHeader(ActorScriptBuilder script, String scriptType) {
			
			appendComment(		"reference required assemblies"									);
			_script.append(		"#r \""															)
			       .append(		System.getProperty("user.home").replaceAll("\\\\", "/")			)
			       .append(		"/.m2/dll/Newtonsoft/Json/Newtonsoft.Json.dll\""		+ EOL	);
			appendBlankLine();
			
			appendComment(		"access namespaces"												);
			_script.append(		"open System"											+ EOL	)
				   .append(		"open System.IO"										+ EOL	)
				   .append(		"open System.Collections.Generic"						+ EOL 	)
				   .append(		"open Newtonsoft.Json" 									+ EOL 	);
			appendBlankLine();
		}
		
		private Object _convertToType(Object value, String type) {
			
			if (type == null || value == null) {
				return value;
			}
			
			if (type.equals("String")) {
				return value.toString();
			}
			
			return value;
		}

		public ScriptBuilder appendLiteralAssignment(String name, Object value, String type, boolean mutable, boolean nullable) throws Exception {
			
			value = _convertToType(value, type);
			
			if (type == null) {
				_assignUntypedLiteral(name, value, mutable, nullable);
			} else if (type.equals("String")) {
				_assignTypedLiteral(name, value, "string", mutable, nullable);
			} else if (type.equals("Boolean")) {
				_assignTypedLiteral(name, value, "bool", mutable, nullable);
			} else if (type.equals("Integer")) {
				_assignTypedLiteral(name, value, "int", mutable, nullable);
			} else if (type.equals("Double")) {
				_assignTypedLiteral(name, value, "float", mutable, nullable);
			} else {
				_assignUntypedLiteral(name, value, mutable, nullable);
			}
			return this;
		}

		private void _assignUntypedLiteral(String name, Object value, boolean mutable, boolean nullable) {
			
			if (value == null && !nullable) return;
			
			_script.append(		"let "			);
			
			if (mutable) _script
					.append( 	"mutable "		);
			
			if (nullable) _script	
					.append( 	"("				)
					.append(	name			)
					.append( 	":'a option)"	);
			else _script
					.append(	name			);
			
			_script	.append( 	" = "			);
			
			if (value == null) _script
				   .append( 	"None"			);
			
			else if (nullable) _script
					.append(	"Some("			)
					.append(	_otos(value)	)
					.append(	")"				);
			
			else _script
				   .append( 	_otos(value) 	);
			
			_script.append(		EOL				);
		}

		private String _otos(Object value) {
			if (value instanceof String)
				return "\"\"\"" + value.toString() +  "\"\"\"";
			else
				return value.toString();
		}		

		private void _assignTypedLiteral(String name, Object value, String fsharpType, boolean mutable, boolean nullable) throws Exception {
			
			if (value == null && !nullable) return;

			_script	.append(	"let "			);
			
			if (mutable) _script
					.append( 	"mutable "		);
			
			_script	.append( 	"("				)
				   	.append(	name			)
				   	.append( 	":"				)
				   	.append( 	fsharpType		);
		
			if (nullable)  {

				_script
					.append(	" option"		)
					.append( 	")"				)
				   	.append( 	" = "			);
			
				if (value == null) _script
			
					.append(	"None"			);

				else _script
					.append(	"Some("			)
				   	.append( 	_otos(value)	)
					.append(	")"				);
			
			} else _script
			
					.append( 	")"				)
				   	.append( 	" = "			)
				   	.append( 	_otos(value)	);
			
			_script	.append(	EOL				);
		}

		public ScriptBuilder appendChangeDirectory(String path) {
			_script.append(		"Directory.SetCurrentDirectory "	)
				   .append(		"\"\"\""							)
				   .append( 	path								)
				   .append(		"\"\"\""							)
				   .append(		EOL									);
			return this;
		}

		public ScriptBuilder appendPrintStringStatement(String string) {
			_script.append(		"printfn \""	)
				   .append( 	string			)
				   .append(		"\""			)
				   .append(		EOL				);
			return this;
		}

		public ActorScriptBuilder appendSerializationBeginStatement() {
			appendCode(	"let outputMap = new Dictionary<string, Object>()" );
			return this;
		}

		public ActorScriptBuilder appendSerializationEndStatement() {
			
			appendCode( "let outputJson = JsonConvert.SerializeObject(outputMap)"	);	
			appendCode( "Console.WriteLine(outputJson)"								);

			return this;
		}

		public ScriptBuilder appendVariableSerializationStatement(String name, String type) {
			_script.append(		"outputMap.Add(\""	)
				   .append(		name				)
				   .append(		"\", "				)
				   .append(		name				)
				   .append(		")"					)
				   .append(		EOL					);	
			return this;
		}
		
		public ActorScriptBuilder appendNonNullStringVariableSerializationPrintStatement(String name) {
			return appendVariableSerializationStatement(name, null);
		}

		public ScriptBuilder appendInputControlFunctions() {

			appendComment("initialize input control variables");
			appendCode( "let mutable enabledInputs   = \"\"" );
			appendCode( "let mutable disabledInputs  = \"\"" );
			appendBlankLine();

			appendComment("define functions for enabling and disabling actor inputs");
			appendCode( "let enableInput input  =  enabledInputs <- enabledInputs + \" \" + input");
			appendCode( "let disableInput input =  disabledInputs <- disabledInputs + \" \" + input");

			return this;
		}

		public ScriptBuilder appendOutputControlFunctions() {
			
			appendComment("initialize output control variables");
			appendCode( "let mutable enabledOutputs   = \"\"" );
			appendCode( "let mutable disabledOutputs  = \"\"" );
			appendBlankLine();

			appendComment("define functions for enabling and disabling actor outputs");
			appendCode( "let enableOutput output  =  enabledOutputs <- enabledOutputs + \" \" + output");
			appendCode( "let disableOutput output =  disabledOutputs <- disabledOutputs + \" \" + output");

			return this;
		}

		@Override
		public ScriptBuilder appendScriptExitCommend() {
			
			appendComment("exit the fsharp interpreter");
			appendCode("#quit");
			
			return this;
		}
		
		
		public String toString() {

			return _script.toString();
		}
	}
}
