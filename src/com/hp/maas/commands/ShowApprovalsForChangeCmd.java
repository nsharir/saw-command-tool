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
public class ShowApprovalsForChangeCmd implements Command {

    private Server server;
    private EntityInstance change;

    public ShowApprovalsForChangeCmd(Server server) {
        this.server = server;
    }

    @Override
    public String nextQuestion() {
        return "You have selected to view the approval status of a change. please enter the change Id";
    }

    @Override
    public boolean validateInput(String cmd) {
        int id;

        try{
            id = Integer.parseInt(cmd);
        }catch (NumberFormatException e){
            System.out.println("Please provide a valid change number.");
            return false;
        }

        List<EntityInstance> changes = server.getEntityReaderAPI().readFullLayout("Change", new FilterBuilder(new IdsFilterElement(id)));

        if (changes.isEmpty()){
            System.out.println("There is no such change with id "+id);
            return false;
        }


        change = changes.get(0);

        return true;
    }

    @Override
    public boolean run(String cmd) {
        //ParentEntityType
        //ParentEntityId
        //PlatformTaskType+%3D+%27Approval%27
        //PhaseId
        //
        //

        FilterBuilder filter =
        new FilterBuilder(new SimpleFilterElement("ParentEntityType","=","Change"))
                     .and(new SimpleFilterElement("ParentEntityId","=",change.getFieldValue("Id")))
                     .and(new SimpleFilterElement("PlatformTaskType","=","Approval"));

        List<String> layout = new ArrayList<String>();
        layout.add("Id");
        layout.add("Assignee");
        layout.add("Assignee.Name");
        layout.add("PhaseId");
        layout.add("ApproverComment");


        List<EntityInstance> tasks = server.getEntityReaderAPI().readEntities("Task",layout, filter);

        System.out.println("There are "+tasks.size()+" approvals for change number "+change.getFieldValue("Id")+":");
        int count =1;
        for (EntityInstance task : tasks) {
            System.out.print("Approver number " + count + ". Name - " + task.getRelatedEntity("Assignee").getFieldValue("Name") + ".  Status - " + task.getFieldValue("PhaseId")+".");
            Object approverComment = task.getFieldValue("ApproverComment");
            if (approverComment != null){
                System.out.print(" Comment - " + approverComment);
            }
            System.out.print("\n");
            count++;
        }

        System.out.println("");
        return true;
    }
}
