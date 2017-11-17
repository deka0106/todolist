package controllers;

import play.mvc.Controller;

public class Board extends Controller {

	public static void index() {
		render();
	}
	
	public static void addTask() {
		System.out.println(params.all().keySet());
		
		
	}

}
