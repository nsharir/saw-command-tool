package com.hp.maas.commands;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.IdsFilterElement;
import com.hp.maas.apis.model.query.SimpleFilterElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/17/14
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuiteCmd implements Command {


    public QuiteCmd() {
    }

    @Override
    public String nextQuestion() {
        return null;
    }

    @Override
    public boolean validateInput(String cmd) {

        return true;
    }

    @Override
    public boolean run(String cmd) {
        return false;
    }
}
