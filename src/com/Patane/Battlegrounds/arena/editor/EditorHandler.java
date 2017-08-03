package com.Patane.Battlegrounds.arena.editor;

import java.util.HashMap;

import com.Patane.Battlegrounds.Messenger;
import com.Patane.Battlegrounds.arena.editor.types.BuildEditor;
import com.Patane.Battlegrounds.arena.editor.types.ClassEditor;
import com.Patane.Battlegrounds.arena.editor.types.GroundEditor;
import com.Patane.Battlegrounds.arena.editor.types.LobbyEditor;
import com.Patane.Battlegrounds.arena.editor.types.SettingsEditor;
import com.Patane.Battlegrounds.arena.editor.types.SpawnEditor;
import com.Patane.Battlegrounds.arena.editor.types.WavesEditor;

public class EditorHandler {
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
		register(SettingsEditor.class);
		register(WavesEditor.class);
	}
	private static void register(Class< ? extends EditorType> editorType){
		EditorInfo ETInfo = editorType.getAnnotation(EditorInfo.class);
		if(ETInfo == null) {
			Messenger.warning("An editorType is missing its attached EditorInfo Annotation!");
			return;
		}
		editorTypes.put(ETInfo.name(), editorType);
	}
}
