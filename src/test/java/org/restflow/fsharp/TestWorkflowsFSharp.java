package org.restflow.fsharp;

import org.restflow.test.WorkflowTestCase;

public class TestWorkflowsFSharp extends WorkflowTestCase {

	public TestWorkflowsFSharp() {
		super("workflows");
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		_importSchemeToResourceMap.put("actors", "classpath:/org/restflow/fsharp/");
	}
	
	public void test_HelloWorld_FSharpActor_DataDrivenDirector() throws Exception {
		_loadAndRunWorkflow("HelloWorld", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}

	public void test_BranchingWorkflow_FSharpActor_DataDrivenDirector() throws Exception {

		_loadAndRunWorkflow("BranchingWorkflow", _dataDrivenDirector());
		assertEquals(_getExpectedStdout("stdout_data.txt"), _runner.getStdoutRecording());		
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
	}
	
	public void test_MergingWorkflow_FSharpActor_DataDrivenDirector() throws Exception {

		_loadAndRunWorkflow("MergingWorkflow", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());		
	}
	
	public void test_CountToThree_FSharpActor_DataDrivenDirector() throws Exception {

		_loadAndRunWorkflow("CountToThree", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}

	public void test_IntegerFilter_FSharpActor_DataDrivenDirector() throws Exception {
		
		_loadAndRunWorkflow("IntegerFilter", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}	
	
	public void test_AdderLoop_FSharpActor_DataDrivenDirector() throws Exception {

		_loadAndRunWorkflow("AdderLoop", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());		
		assertEquals(_getExpectedStdout(), _runner.getStdoutRecording());
	}		
	
	public void test_IntegerStreamMergeDuplicates_FSharpActor_DataDrivenDirector() throws Exception {
		
		_loadAndRunWorkflow("IntegerStreamMergeDuplicates", _dataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertEquals(_getExpectedStdout("stdout_data.txt"), _runner.getStdoutRecording());
		System.out.println(_runner.getStderrRecording());
	}	
	
	public void test_HammingSequence_FSharpActor_MTDataDrivenDirector() throws Exception {
		
		_loadAndRunWorkflow("HammingSequence", _MTDataDrivenDirector());
		assertEquals(_getExpectedTrace(), _runner.getTraceReport());
		assertEquals(_getExpectedStdout("stdout.txt"), _runner.getStdoutRecording());
	}	
}
