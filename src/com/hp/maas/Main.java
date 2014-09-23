package com.hp.maas;

import com.hp.maas.apis.Server;
import com.hp.maas.commands.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: Nadav
 * Date: 9/9/14
 * Time: 9:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {


    public static void main(String[] args) {


        if (args.length != 3){
            System.out.println("Please use the following format: SAW [user] [password] [tenant]");
            System.out.println("For example: user@email.com myPass 1111111");
            System.exit(0);
        }

        Server server = new Server("https://mslon001pngx.saas.hp.com/",args[0],args[1],args[2]);
        server.authenticate();

        boolean run = true;

        Map<String,Command> commandsMap = new HashMap<String, Command>();
        commandsMap.put("1",new ShowApprovalsForChangeCmd(server));
        commandsMap.put("2",new DoApprovalForChangeCmd(server, DoApprovalForChangeCmd.ACTION.approve));
        commandsMap.put("3",new DoApprovalForChangeCmd(server, DoApprovalForChangeCmd.ACTION.deny));
        commandsMap.put("4",new DoTaskForChangeCmd(server));
        commandsMap.put("q",new QuiteCmd());

        while(run){
            System.out.println("Press '1' in order to view the approval status of a change.");
            System.out.println("Press '2' in order to approve a change.");
            System.out.println("Press '3' in order to deny a change.");
            System.out.println("Press '4' in order to complete a change task.");
            System.out.println("Press 'q' in order to quite.");


            Scanner scanner = new Scanner(System.in);
            String cmd = scanner.nextLine();

            Command command = commandsMap.get(cmd);

            if (command == null){
                System.out.println("Please select a valid option.");
                continue;
            }

            while(true){
                System.out.println(command.nextQuestion());
                cmd = scanner.nextLine();
                boolean option = command.validateInput(cmd);
                if (!option){
                    System.out.println("Please enter a valid value.");
                }else{
                    boolean b = command.run(cmd);
                    if (!b){
                       System.exit(-1);
                    }else{
                        System.out.println("Please press enter in order to go back to the menu.");
                        scanner.nextLine();
                        break;
                    }
                }
            }


        }

    }


}
