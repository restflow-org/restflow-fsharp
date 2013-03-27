package org.restflow.fsharp;

import java.util.HashMap;
import java.util.Map;

import org.restflow.WorkflowContext;
import org.restflow.actors.ActorBuilder;

public class FSharpActorBuilder implements ActorBuilder {
	
	private String				_name = "";
	private String 				_initialize = "";
	private String 				_step = "";
	private String 				_wrapup = "";
	private Map<String,Object>	_inputs = new HashMap<String,Object>(); 
	private Map<String,Object> 	_outputs = new HashMap<String,Object>(); 
	private Map<String,Object> 	_state = new HashMap<String,Object>();
	private Map<String,String> 	_types = new HashMap<String,String>();
	
	private WorkflowContext _context; 
		
	public FSharpActorBuilder state(String name) {
		_state.put(name,null);
		return this;
	}
	
	public FSharpActorBuilder initialize(String initialize) {
		_initialize = initialize;
		return this;
	}
	
	public FSharpActorBuilder step(String step) {
		_step = step;
		return this;
	}

	public FSharpActorBuilder wrapup(String wrapup) {
		_wrapup = wrapup;
		return this;
	}

	public FSharpActorBuilder input(String name) {
		_inputs.put(name, null);
		return this;
	}
	
	public FSharpActorBuilder type(String variableName, String type) {
		_types.put(variableName, type);
		return this;
	}

	public FSharpActorBuilder input(String name, Map<String,Object> properties) {
		_inputs.put(name, properties);
		return this;
	}	
	
	public FSharpActorBuilder output(String name) {
		_outputs.put(name, null);
		return this;
	}

	public FSharpActorBuilder output(String name, Map<String,Object> properties) {
		_outputs.put(name, properties);
		return this;
	}

	public FSharpActorBuilder name(String name) {
		_name = name;
		return this;
	}

	public FSharpActorBuilder context(WorkflowContext context) {
		_context = context;
		return this;
	}

	public ActorBuilder types(Map<String, String> types) {
		_types.putAll(types);
		return this;
	}
	
	public FSharpActor build() throws Exception {
		
		FSharpActor actor = new FSharpActor();
		
		actor.setName(_name);
		actor.setInputs(_inputs);
		actor.setOutputs(_outputs);
		actor.setState(_state);
		actor.setTypes(_types);
		actor.setInitialize(_initialize);
		actor.setStep(_step);
		actor.setWrapup(_wrapup);
		actor.setApplicationContext(_context);
		actor.afterPropertiesSet();
		
		return actor;
	}

}
