package com.hp.maas.commands;

import com.hp.maas.apis.Server;
import com.hp.maas.apis.model.entity.EntityInstance;
import com.hp.maas.apis.model.query.FilterBuilder;
import com.hp.maas.apis.model.query.IdsFilterElement;
import com.hp.maas.apis.model.query.SimpleFilterElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/17/14
 * Time: 3:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DoTaskForChangeCmd implements Command {


    private Server server;
    private EntityInstance change;

    public DoTaskForChangeCmd(Server server) {
        this.server = server;
    }

    @Override
    public String nextQuestion() {
        return "You have selected to complete a task of a change. please enter the change Id";
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

        FilterBuilder filter =
        new FilterBuilder(new SimpleFilterElement("ParentEntityType","=","Change"))
                     .and(new SimpleFilterElement("ParentEntityId","=",change.getFieldValue("Id")))
                     .and(new SimpleFilterElement("PlatformTaskType","=","ManualTask"))
                     .and(new SimpleFilterElement("Assignee.Upn","=",server.getUserName())).
                      and(new SimpleFilterElement("PhaseId","=","InProgress"));


        List<String> layout = new ArrayList<String>();
        layout.add("Id");
        layout.add("Assignee");
        layout.add("Assignee.Name");
        layout.add("PhaseId");


        List<EntityInstance> tasks = server.getEntityReaderAPI().readEntities("Task",layout, filter);

        if (tasks.isEmpty()){
            System.out.println("There are no pending tasks on you for this change.");
            return true;
        }

        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Are you sure you want to mark this task as completed? please enter y to proceed. or click n to go back to the main menu.");
            String answer = scanner.nextLine();

            if ("y".equals(answer) ) {
                break;
            }

            if ("n".equals(answer) ) {
                return true;
            }
        }



       /* String comment;
        while (true){
            System.out.println("Please enter a comment:");
            comment = scanner.nextLine();
            if (comment != null && comment.trim().length() > 0){
                break;
            }
        }*/

        for (EntityInstance task : tasks) {
            String phase = "Completed";
            task.setFieldValue("PhaseId",phase);
        }

        server.getEntityWriterAPI().updateEntities(tasks);

        System.out.println("Done!");

        return true;

    }
}
