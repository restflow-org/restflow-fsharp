package org.restflow.fsharp;

import org.restflow.nodes.ActorNodeBuilder;
import org.restflow.nodes.ActorWorkflowNode;

public class FSharpNodeBuilder extends ActorNodeBuilder {
	
	private FSharpActorBuilder _fsharpActorBuilder = new FSharpActorBuilder();

	public FSharpNodeBuilder step(String script) {
		_fsharpActorBuilder.step(script);
		return this;
	}
	
	public FSharpNodeBuilder state(String name) {
		_fsharpActorBuilder.state(name);
		return this;
	}
	
	public ActorWorkflowNode build() throws Exception {
		_fsharpActorBuilder.context(_context);
		_fsharpActorBuilder.types(_types);
		_actor = _fsharpActorBuilder.build();
		return super.build();
	}

}
