package org.restflow.fsharp;

import org.restflow.actors.ActorScriptBuilder;
import org.restflow.fsharp.FSharpActor;
import org.restflow.test.RestFlowTestCase;


public class TestFSharpActorScriptBuilder extends RestFlowTestCase {

	public void testAppendCode() throws Exception {

		ActorScriptBuilder builder = new FSharpActor.ScriptBuilder();
		
		assertEquals(
			"", 
			builder.toString()
			);

		builder.appendCode("Some code");
		
		assertEquals(
			"Some code" 			+ EOL, 
			builder.toString()
		);
		
		builder.appendCode("Some more code");
		
		assertEquals(
			"Some code" 			+ EOL +
			"Some more code" 		+ EOL,
			builder.toString()
		);
	}
	
	
	public void testAppendSeparator() throws Exception {

		ActorScriptBuilder builder = new FSharpActor.ScriptBuilder();
		
		builder.appendCode("Some code");
		builder.appendSeparator();
		builder.appendCode("Some more code");
		
		assertEquals(
			"Some code" 			+ EOL +
			"//####################################################################################" + EOL +
			"Some more code" 		+ EOL,
			builder.toString()
		);
	}
	
	public void testAppendBlankLine() throws Exception {

		ActorScriptBuilder builder = new FSharpActor.ScriptBuilder();
		
		builder.appendCode("Some code");
		builder.appendBlankLine();
		builder.appendCode("Some more code");
		
		assertEquals(
			"Some code" 			+ EOL +
			""						+ EOL +
			"Some more code" 		+ EOL,
			builder.toString()
		);
	}

	public void testAppendComment() throws Exception {

		ActorScriptBuilder builder = new FSharpActor.ScriptBuilder();
		
		builder.appendCode("Some code");
		builder.appendComment("A comment");
		builder.appendCode("Some more code");
		
		assertEquals(
			"Some code" 			+ EOL +
			"// A comment"			+ EOL +
			"Some more code" 		+ EOL,
			builder.toString()
		);
	}

	private void _testAppendLiteralAssignment(String name, Object value, String type, boolean mutable, boolean nullable, String expected) throws Exception {

		ActorScriptBuilder builder = new FSharpActor.ScriptBuilder();
		
		builder.appendLiteralAssignment(name, value, type, mutable, nullable);
		assertEquals(expected, builder.toString()
		);
	}
	
	
	/***************************************************/
	/* Test cases where type is null and value is null */
	/***************************************************/
	
	public void testAppendLiteralAssignment_NullType_NullValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, null, false, false, 
			"");
	}

	public void testAppendLiteralAssignment_NullType_NullValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, null, true, false, 
			"");
	}

	public void testAppendLiteralAssignment_NullType_NullValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, null, false, true,
			"let (v:'a option) = None" 					+ EOL);
	}
	
	public void testAppendLiteralAssignment_NullType_NullValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, null, true, true,
			"let mutable (v:'a option) = None" 			+ EOL);
	}

	
	/******************************************************/
	/* Test cases where type is null and value is integer */
	/******************************************************/
	
	public void testAppendLiteralAssignment_NullType_IntegerValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42, null, false, false,
			"let v = 42" 								+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_IntegerValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42, null, true, false,
			"let mutable v = 42" 						+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_IntegerValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42, null, false, true,
			"let (v:'a option) = Some(42)" 				+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_IntegerValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42, null, true, true,
			"let mutable (v:'a option) = Some(42)" 		+ EOL);
	}

	/*******************************************************/
	/* Test cases where type is null and value is a double */
	/*******************************************************/
	
	public void testAppendLiteralAssignment_NullType_DoubleValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42.3, null, false, false,
			"let v = 42.3" 								+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_DoubleValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42.3, null, true, false,
			"let mutable v = 42.3" 						+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_DoubleValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42.3, null, false, true,
			"let (v:'a option) = Some(42.3)" 			+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_DoubleValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42.3, null, true, true,
			"let mutable (v:'a option) = Some(42.3)" 	+ EOL);
	}
	
	
	/*****************************************************/
	/* Test cases where type is null and value is string */
	/*****************************************************/

	public void testAppendLiteralAssignment_NullType_StringValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", "s", null, false, false,
			"let v = \"\"\"s\"\"\"" 					+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_StringValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", "s", null, true, false,
			"let mutable v = \"\"\"s\"\"\"" 			+ EOL);
	}
	
	public void testAppendLiteralAssignment_NullType_StringValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", "s", null, false, true,
			"let (v:'a option) = Some(\"\"\"s\"\"\")" 	+ EOL);
	}
	
	public void testAppendLiteralAssignment_NullType_StringValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", "s", null, true, true,
			"let mutable (v:'a option) = Some(\"\"\"s\"\"\")" 	+ EOL);
	}
	
	
	/******************************************************/
	/* Test cases where type is null and value is boolean */
	/******************************************************/
	
	public void testAppendLiteralAssignment_NullType_TrueBooleanValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", true, null, false, false,
			"let v = true" 								+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_TrueBooleanValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", true, null, true, false,
			"let mutable v = true" 						+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_TrueBooleanValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", true, null, false, true,
			"let (v:'a option) = Some(true)" 			+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_TrueBooleanValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", true, null, true, true,
			"let mutable (v:'a option) = Some(true)" 	+ EOL);
	}
	
	public void testAppendLiteralAssignment_NullType_FalseBooleanValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", false, null, false, false,
			"let v = false" 							+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_FalseBooleanValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", false, null, true, false,
			"let mutable v = false" 					+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_FalseBooleanValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", false, null, false, true,
			"let (v:'a option) = Some(false)" 			+ EOL);
	}

	public void testAppendLiteralAssignment_NullType_FalseBooleanValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", false, null, true, true,
			"let mutable (v:'a option) = Some(false)" 	+ EOL);
	}

	/******************************************************/
	/* Test cases where type is Integer and value is null */
	/******************************************************/	
	
	public void testAppendLiteralAssignment_IntegerType_NullValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Integer", false, false,
			"");
	}

	public void testAppendLiteralAssignment_IntegerType_NullValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Integer", true, false,
			"");
	}

	public void testAppendLiteralAssignment_IntegerType_NullValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Integer", false, true,
			"let (v:int option) = None"						+ EOL);
	}

	public void testAppendLiteralAssignment_IntegerType_NullValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Integer", true, true,
			"let mutable (v:int option) = None"				+ EOL);
	}

	/*****************************************************/
	/* Test cases where type is Integer and value is int */
	/*****************************************************/	
	
	public void testAppendLiteralAssignment_IntegerType_IntegerValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42, "Integer", false, false,
			"let (v:int) = 42" 								+ EOL);
	}

	public void testAppendLiteralAssignment_IntegerType_IntegerValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42, "Integer", true, false,
			"let mutable (v:int) = 42" 						+ EOL);
	}

	public void testAppendLiteralAssignment_IntegerType_IntegerValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42, "Integer", false, true,
			"let (v:int option) = Some(42)"					+ EOL);
	}

	public void testAppendLiteralAssignment_IntegerType_IntegerValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42, "Integer", true, true,
			"let mutable (v:int option) = Some(42)"			+ EOL);
	}
	
	
	/******************************************************/
	/* Test cases where type is Double and value is null */
	/******************************************************/	
	
	public void testAppendLiteralAssignment_DoubleType_NullValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Double", false, false,
			"");
	}

	public void testAppendLiteralAssignment_DoubleType_NullValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Double", true, false,
			"");
	}

	public void testAppendLiteralAssignment_DoubleType_NullValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Double", false, true,
			"let (v:float option) = None"						+ EOL);
	}

	public void testAppendLiteralAssignment_DoubleType_NullValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Double", true, true,
			"let mutable (v:float option) = None"				+ EOL);
	}

	/********************************************************/
	/* Test cases where type is Double and value is  double */
	/********************************************************/	
	
	public void testAppendLiteralAssignment_DoubleType_DoubleValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42.3, "Double", false, false,
			"let (v:float) = 42.3" 								+ EOL);
	}

	public void testAppendLiteralAssignment_DoubleType_DoubleValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42.3, "Double", true, false,
			"let mutable (v:float) = 42.3" 						+ EOL);
	}

	public void testAppendLiteralAssignment_DoubleType_DoubleValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42.3, "Double", false, true,
			"let (v:float option) = Some(42.3)"					+ EOL);
	}

	public void testAppendLiteralAssignment_DoubleType_DoubleValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", 42.3, "Double", true, true,
			"let mutable (v:float option) = Some(42.3)"			+ EOL);
	}

	/******************************************************/
	/* Test cases where type is Boolean and value is null */
	/******************************************************/	
	
	public void testAppendLiteralAssignment_BooleanType_NullValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Boolean", false, false,
			"");
	}

	public void testAppendLiteralAssignment_BooleanType_NullValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Boolean", true, false,
			"");
	}

	public void testAppendLiteralAssignment_BooleanType_NullValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Boolean", false, true,
			"let (v:bool option) = None"						+ EOL);
	}

	public void testAppendLiteralAssignment_BooleanType_NullValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "Boolean", true, true,
			"let mutable (v:bool option) = None"				+ EOL);
	}	
	
	
	/*********************************************************/
	/* Test cases where type is Boolean and value is true    */
	/*********************************************************/	
	
	public void testAppendLiteralAssignment_BooleanType_TrueBooleanValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", true, "Boolean", false, false,
			"let (v:bool) = true" 								+ EOL);
	}

	public void testAppendLiteralAssignment_BooleanType_TrueBooleanValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", true, "Boolean", true, false,
			"let mutable (v:bool) = true" 						+ EOL);
	}

	public void testAppendLiteralAssignment_BooleanType_TrueBooleanValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", true, "Boolean", false, true,
			"let (v:bool option) = Some(true)"					+ EOL);
	}

	public void testAppendLiteralAssignment_BooleanType_TrueBooleanValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", true, "Boolean", true, true,
			"let mutable (v:bool option) = Some(true)"			+ EOL);
	}

	/*********************************************************/
	/* Test cases where type is Boolean and value is false    */
	/*********************************************************/	
	
	public void testAppendLiteralAssignment_BooleanType_FalseBooleanValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", false, "Boolean", false, false,
			"let (v:bool) = false" 								+ EOL);
	}

	public void testAppendLiteralAssignment_BooleanType_FalseBooleanValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", false, "Boolean", true, false,
			"let mutable (v:bool) = false" 						+ EOL);
	}

	public void testAppendLiteralAssignment_BooleanType_FalseBooleanValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", false, "Boolean", false, true,
			"let (v:bool option) = Some(false)"					+ EOL);
	}

	public void testAppendLiteralAssignment_BooleanType_FalseBooleanValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", false, "Boolean", true, true,
			"let mutable (v:bool option) = Some(false)"			+ EOL);
	}

	
	/*****************************************************/
	/* Test cases where type is String and value is null */
	/****************************************************/	
	
	public void testAppendLiteralAssignment_StringType_NullValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "String", false, false,
			"");
	}

	public void testAppendLiteralAssignment_StringType_NullValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "String", true, false,
			"");
	}

	public void testAppendLiteralAssignment_StringType_NullValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "String", false, true,
			"let (v:string option) = None"						+ EOL);
	}

	public void testAppendLiteralAssignment_StringType_NullValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", null, "String", true, true,
			"let mutable (v:string option) = None"				+ EOL);
	}	
	

	/*******************************************************/
	/* Test cases where type is String and value is string */
	/*******************************************************/	
	
	public void testAppendLiteralAssignment_StringType_StringValue() throws Exception {
		_testAppendLiteralAssignment(
			"v", "s", "String", false, false,
			"let (v:string) = \"\"\"s\"\"\""					+ EOL);
	}

	public void testAppendLiteralAssignment_StringType_StringValue_Mutable() throws Exception {
		_testAppendLiteralAssignment(
			"v", "s", "String", true, false,
			"let mutable (v:string) = \"\"\"s\"\"\""			+ EOL);
	}

	public void testAppendLiteralAssignment_StringType_StringValue_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", "s", "String", false, true,
			"let (v:string option) = Some(\"\"\"s\"\"\")"		+ EOL);
	}

	public void testAppendLiteralAssignment_StringType_StringValue_Mutable_Nullable() throws Exception {
		_testAppendLiteralAssignment(
			"v", "s", "String", true, true,
			"let mutable (v:string option) = Some(\"\"\"s\"\"\")"	+ EOL);
	}	

	


//	public void testAppendChangeDirectory() {
//
//		ActorScriptBuilder builder = new FSharpActor.ScriptBuilder();
//		
//		builder.appendChangeDirectory("test/temp");
//		assertEquals(
//			"os.chdir('test/temp')" 				+ EOL,
//			builder.toString()
//		);
//	}

	public void testAppendPrintStringStatement() {

		ActorScriptBuilder builder = new FSharpActor.ScriptBuilder();
		
		builder.appendPrintStringStatement("string to print");
		
		assertEquals(
			"printfn \"string to print\"" 				+ EOL,
			builder.toString()
		);
	}
	
	public void testAppendVariableYamlPrintStatement_StringType() {
		
		ActorScriptBuilder builder = new FSharpActor.ScriptBuilder();
		
		builder.appendVariableYamlPrintStatement("var_with_string_type", "String");
		
		assertEquals(
				"printfn \"var_with_string_type: %A\" var_with_string_type" +EOL, 	
				builder.toString()
		);		
	}

	public void testAppendVariableYamlPrintStatement_IntegerType() {
		
		ActorScriptBuilder builder = new FSharpActor.ScriptBuilder();
		
		builder.appendVariableYamlPrintStatement("var_with_integer_type", "Integer");
		
		assertEquals(
			"printfn \"var_with_integer_type: %A\" var_with_integer_type"		+ EOL,
			builder.toString()
		);		
	}

	public void testAppendVariableYamlPrintStatement_BooleanType() {
		
		ActorScriptBuilder builder = new FSharpActor.ScriptBuilder();
		
		builder.appendVariableYamlPrintStatement("var_with_boolean_type", "Boolean");
		
		assertEquals(
			"printfn \"var_with_boolean_type: %A\" var_with_boolean_type"		+ EOL,
			builder.toString()
		);		
	}
}
