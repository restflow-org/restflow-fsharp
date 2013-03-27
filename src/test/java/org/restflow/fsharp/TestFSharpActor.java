package org.restflow.fsharp;

import org.restflow.WorkflowContext;
import org.restflow.WorkflowContextBuilder;
import org.restflow.fsharp.FSharpActor;
import org.restflow.fsharp.FSharpActorBuilder;
import org.restflow.test.RestFlowTestCase;
import org.restflow.util.StdoutRecorder;


public class TestFSharpActor extends RestFlowTestCase {

	private WorkflowContext _context;
	
	public void setUp() throws Exception {
		super.setUp();
		_context = new WorkflowContextBuilder()
			.build();
	}
	
	public void testAugmentedStepScript_NoInputsOutputsOrState() throws Exception {

		final FSharpActor actor = new FSharpActorBuilder()
			.context(_context)
			.name("Hello")
			.step("printfn \"Hello world!!!\";;")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();
				
		assertEquals(
			"// AUGMENTED STEP SCRIPT FOR ACTOR Hello" 													+ EOL +
			"" 																							+ EOL +
			"// BEGINNING OF ORIGINAL SCRIPT" 															+ EOL +
			"" 																							+ EOL +
			"printfn \"Hello world!!!\";;"																+ EOL +
			"" 																							+ EOL +
			"// END OF ORIGINAL SCRIPT" 																+ EOL +
			"" 																							+ EOL +
			"// signal end of output from original script" 												+ EOL +
			"printfn \"__END_OF_SCRIPT_OUTPUT__\"" 														+ EOL +
			"" 																							+ EOL
			, actor.getAugmentedStepScript());
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {actor.step();}});
			
		// confirm expected stdout showing three values printed
		assertEquals(
			"Hello world!!!" 					+ EOL ,
			recorder.getStdoutRecording());
		
		// confirm stderr is empty
		assertEquals("" , recorder.getStderrRecording());
	}
		
	public void testAugmentedStepScript_WithInputs_NoOutputsOrState() throws Exception {

		final FSharpActor actor = new FSharpActorBuilder()
			.context(_context)
			.name("Hello")
			.input("greeting")
			.step("printfn \"%s world!\" greeting")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();
		
		actor.setInputValue("greeting", "Goodbye");
		
		assertEquals(
			"// AUGMENTED STEP SCRIPT FOR ACTOR Hello" 													+ EOL +
			"" 																							+ EOL +
			"// initialize input control variables"		 												+ EOL +
			"let mutable enabledInputs   = \"\"" 														+ EOL +
			"let mutable disabledInputs  = \"\"" 														+ EOL +
			"" 																							+ EOL +
			"// define functions for enabling and disabling actor inputs" 								+ EOL +
			"let enableInput input  =  enabledInputs <- enabledInputs + \" \" + input" 					+ EOL +
			"let disableInput input =  disabledInputs <- disabledInputs + \" \" + input" 				+ EOL +
			"" 																							+ EOL +
			"// initialize actor input variables"														+ EOL +
			"let greeting = \"Goodbye\""																+ EOL +
			""																							+ EOL +
			"// BEGINNING OF ORIGINAL SCRIPT"															+ EOL +
			""																							+ EOL +
			"printfn \"%s world!\" greeting"															+ EOL +
			"" 																							+ EOL +
			"// END OF ORIGINAL SCRIPT" 																+ EOL +
			"" 																							+ EOL +
			"// signal end of output from original script" 												+ EOL +
			"printfn \"__END_OF_SCRIPT_OUTPUT__\"" 														+ EOL +
			"" 																							+ EOL +
			"// render actor input control variables as yaml" 											+ EOL +
			"printfn \"enabledInputs: %s\" enabledInputs"												+ EOL +
			"printfn \"disabledInputs: %s\" disabledInputs" 											+ EOL +
			""																							+ EOL
			, actor.getAugmentedStepScript());
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {actor.step();}});
			
		// confirm stderr is empty
		assertEquals("" , recorder.getStderrRecording());

		// confirm expected stdout showing three values printed
		assertEquals(
			"Goodbye world!" 					+ EOL ,
			recorder.getStdoutRecording());
	}

	public void testGetAugmentedStepScript_WithOutputs_NoInputsOrState() throws Exception {

		final FSharpActor actor = new FSharpActorBuilder()
			.context(_context)
			.name("Hello")
			.step("let greeting = \"Nice to meet you.\"")
			.output("greeting")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();
		
		assertEquals(
			"// AUGMENTED STEP SCRIPT FOR ACTOR Hello" 													+ EOL +
			"" 																							+ EOL +
			"// initialize output control variables"		 											+ EOL +
			"let mutable enabledOutputs   = \"\"" 														+ EOL +
			"let mutable disabledOutputs  = \"\"" 														+ EOL +
			"" 																							+ EOL +
			"// define functions for enabling and disabling actor outputs" 								+ EOL +
			"let enableOutput output  =  enabledOutputs <- enabledOutputs + \" \" + output" 					+ EOL +
			"let disableOutput output =  disabledOutputs <- disabledOutputs + \" \" + output" 					+ EOL +
			"" 																							+ EOL +
			"// BEGINNING OF ORIGINAL SCRIPT" 															+ EOL +
			"" 																							+ EOL +
			"let greeting = \"Nice to meet you.\""														+ EOL +
			"" 																							+ EOL +
			"// END OF ORIGINAL SCRIPT" 																+ EOL +
			"" 																							+ EOL +
			"// signal end of output from original script" 												+ EOL +
			"printfn \"__END_OF_SCRIPT_OUTPUT__\"" 														+ EOL +
			"" 																							+ EOL +
			"// render output variables as yaml" 														+ EOL +
			"printfn \"greeting: %A\" greeting"															+ EOL +
			"" 																							+ EOL +
			"// render actor output control variables as yaml" 											+ EOL +
			"printfn \"enabledOutputs: %s\" enabledOutputs" 											+ EOL +
			"printfn \"disabledOutputs: %s\" disabledOutputs" 											+ EOL +
			""																							+ EOL
			, actor.getAugmentedStepScript());
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {actor.step();}});
			
		// confirm expected stdout showing three values printed
		assertEquals("", recorder.getStdoutRecording());
		
		assertEquals("Nice to meet you.", actor.getOutputValue("greeting"));
	}

	public void testGetAugmentedStepScript_WithState_NoInputsOrOutput() throws Exception {

		final FSharpActor actor = new FSharpActorBuilder()
			.context(_context)
			.name("Hello")
			.state("greeting")
			.step("let greeting = \"Nice to meet you.\"")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();
		
		assertEquals(
			"// AUGMENTED STEP SCRIPT FOR ACTOR Hello" 													+ EOL +
			"" 																							+ EOL +
			"// initialize actor state variables" 														+ EOL +
			""							 																+ EOL +
			"// BEGINNING OF ORIGINAL SCRIPT" 															+ EOL +
			""							 																+ EOL +
			"let greeting = \"Nice to meet you.\""														+ EOL +
			""							 																+ EOL +
			"// END OF ORIGINAL SCRIPT" 																+ EOL +
			""							 																+ EOL +
			"// signal end of output from original script" 												+ EOL +
			"printfn \"__END_OF_SCRIPT_OUTPUT__\"" 														+ EOL +
			""							 																+ EOL +
			"// render state variables as yaml" 														+ EOL +
			"printfn \"greeting: %A\" greeting"															+ EOL +
			"" 																							+ EOL
			, actor.getAugmentedStepScript());
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {actor.step();}});
			
		// confirm expected stdout showing three values printed
		assertEquals("", recorder.getStdoutRecording());
		
		assertEquals("Nice to meet you.", actor.getStateValue("greeting"));
	}
	
	public void testGetAugmentedStepScript_WithInputsAndOutput_NoState() throws Exception {

		final FSharpActor actor = new FSharpActorBuilder()
			.context(_context)
			.name("Multiplier")
			.input("x")
			.input("y")
			.step("let z = x * y")
			.output("z")
			.type("x", "Integer")
			.type("y", "Integer")
			.type("z", "Integer")
			.build();

		actor.elaborate();
		actor.configure();
		actor.initialize();
		
		actor.setInputValue("x", 3);
		actor.setInputValue("y", 12);
		
		assertEquals(
			"// AUGMENTED STEP SCRIPT FOR ACTOR Multiplier"												+ EOL +
			""																							+ EOL +
			"// initialize input control variables"		 												+ EOL +
			"let mutable enabledInputs   = \"\"" 														+ EOL +
			"let mutable disabledInputs  = \"\"" 														+ EOL +
			"" 																							+ EOL +
			"// define functions for enabling and disabling actor inputs" 								+ EOL +
			"let enableInput input  =  enabledInputs <- enabledInputs + \" \" + input" 							+ EOL +
			"let disableInput input =  disabledInputs <- disabledInputs + \" \" + input" 						+ EOL +
			""																							+ EOL +
			"// initialize output control variables"		 											+ EOL +
			"let mutable enabledOutputs   = \"\"" 														+ EOL +
			"let mutable disabledOutputs  = \"\"" 														+ EOL +
			"" 																							+ EOL +
			"// define functions for enabling and disabling actor outputs" 								+ EOL +
			"let enableOutput output  =  enabledOutputs <- enabledOutputs + \" \" + output" 					+ EOL +
			"let disableOutput output =  disabledOutputs <- disabledOutputs + \" \" + output" 					+ EOL +
			""																							+ EOL +
			"// initialize actor input variables"														+ EOL +
			"let (y:int) = 12"																			+ EOL +
			"let (x:int) = 3"																			+ EOL +
			""																							+ EOL +
			"// BEGINNING OF ORIGINAL SCRIPT"															+ EOL +
			""																							+ EOL +
			"let z = x * y"																				+ EOL +
			""																							+ EOL +
			"// END OF ORIGINAL SCRIPT"																	+ EOL +
			""																							+ EOL +
			"// signal end of output from original script"												+ EOL +
			"printfn \"__END_OF_SCRIPT_OUTPUT__\""														+ EOL +
			""																							+ EOL +
			"// render output variables as yaml"														+ EOL +
			"printfn \"z: %A\" z"																		+ EOL +
			""																							+ EOL +
			"// render actor input control variables as yaml" 											+ EOL +
			"printfn \"enabledInputs: %s\" enabledInputs"												+ EOL +
			"printfn \"disabledInputs: %s\" disabledInputs" 											+ EOL +
			""																							+ EOL +
			"// render actor output control variables as yaml" 											+ EOL +
			"printfn \"enabledOutputs: %s\" enabledOutputs" 											+ EOL +
			"printfn \"disabledOutputs: %s\" disabledOutputs" 											+ EOL +
			"" 																							+ EOL,
			actor.getAugmentedStepScript());
		
		// run the workflow while capturing stdout and stderr 
		StdoutRecorder recorder = new StdoutRecorder(new StdoutRecorder.WrappedCode() {
			public void execute() throws Exception {actor.step();}});
			
		// confirm expected stdout showing three values printed
		assertEquals("", recorder.getStdoutRecording());
		
		assertEquals(36, actor.getOutputValue("z"));
	}
}
