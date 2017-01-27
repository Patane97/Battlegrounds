package com.Patane.Battlegrounds.arena.editor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface EditorInfo {
	public String name();
	public String permission();

}
