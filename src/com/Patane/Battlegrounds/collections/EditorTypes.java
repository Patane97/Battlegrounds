package com.Patane.Battlegrounds.collections;

import java.util.HashMap;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.editor.EditorInfo;
import com.Patane.Battlegrounds.arena.editor.EditorType;
import com.Patane.Battlegrounds.arena.editor.build.BuildEditor;
import com.Patane.Battlegrounds.arena.editor.classes.ClassEditor;
import com.Patane.Battlegrounds.arena.editor.region.GroundEditor;
import com.Patane.Battlegrounds.arena.editor.region.LobbyEditor;
import com.Patane.Battlegrounds.arena.editor.spawn.SpawnEditor;

public class EditorTypes {
	private static HashMap<String, Class< ? extends EditorType>> editorTypes;
	
	public static Class< ? extends EditorType> getEditorType(String editorType){
		for(String editorName : editorTypes.keySet()){
			if(editorType.contains(editorName))
				return editorTypes.get(editorName);
		}
		return null;
	}
	public static void registerAll() {
		editorTypes = new HashMap<String, Class< ? extends EditorType>>();
		register(BuildEditor.class);
		register(ClassEditor.class);
		register(SpawnEditor.class);
		register(GroundEditor.class);
		register(LobbyEditor.class);
	}
	private static void register(Class< ? extends EditorType> editorType){
		EditorInfo ETInfo = editorType.getAnnotation(EditorInfo.class);
		if(ETInfo == null) {
			Messenger.warning("An editorType is missing its attached CommandInfo Annotation!");
			return;
		}
		editorTypes.put(ETInfo.name(), editorType);
	}
}
