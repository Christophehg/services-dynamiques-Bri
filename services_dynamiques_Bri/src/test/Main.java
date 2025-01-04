package test;

import BRi.MyBRiService;
import BRi.InvalidBRiService;
import addService.BRiServiceManager;

public class Main {
    public static void main(String[] args) {
        try {
            BRiServiceManager.addService(MyBRiService.class);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            BRiServiceManager.addService(InvalidBRiService.class);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        BRiServiceManager.listServices();
    }
}
