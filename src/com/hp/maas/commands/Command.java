package com.hp.maas.commands;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/17/14
 * Time: 3:17 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Command {

    public String nextQuestion();

    public boolean validateInput (String cmd);

    public boolean run(String cmd);

}
