package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

// represents order
class Order {
    private int id, quantity;
    private String type, company;

    public Order(int id, String type, String company, int quantity) {
        this.setId(id);
        this.setQuantity(quantity);
        this.setType(type);
        this.setCompany(company);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.toLowerCase();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company.toLowerCase();
    }

    @Override
    public String toString() {
        return "Order: { " + getId() + ", " + getType() + ", "  + getCompany() + ", " + getQuantity() + " }";
    }
}

// executes the order
public class OrderProcessor {

    HashMap<String, Integer> buyBucket = new HashMap<String, Integer>();
    HashMap<String, Integer> sellBucket = new HashMap<String, Integer>();

    int buyOrder(String company, int quantity) {

        if(sellBucket.containsKey(company)) {
            int availableQuantity = sellBucket.get(company);
            if(quantity <= availableQuantity) {
                sellBucket.put(company, sellBucket.get(company) - quantity);
                return 0;
            }
            else {
                // --todo am i supposed to execute whatever available quantity ?
                // sellBucket.put(company, 0);

                int difference = quantity - availableQuantity;
                return difference;
            }
        }
        // --todo am i supposed to execute initial orders even if they are not in inventory?
        else {
            if( buyBucket.containsKey(company) )
                buyBucket.put(company, buyBucket.get(company) + quantity);
            else
                buyBucket.put(company, quantity);
            return 0;
        }
    }

    int sellOrder(String company, int quantity) {
        if(buyBucket.containsKey(company)) {
            int availableQuantity = buyBucket.get(company);
            if(quantity <= availableQuantity) {
                buyBucket.put(company, buyBucket.get(company) - quantity);
                return 0;
            }
            else {
                // --todo am i supposed to execute whatever available quantity ?
                // buyBucket.put(company, 0);

                int difference = quantity - availableQuantity;
                return difference;
            }
        }
        // --todo am i supposed to execute initial orders even if they are not in inventory?
        else {
            if( sellBucket.containsKey(company) )
                sellBucket.put(company, sellBucket.get(company) + quantity);
            else
                sellBucket.put(company, quantity);
            return 0;
        }
    }

    int executeOrder(Order order) {

        if(order.getType().equals("buy")) {
            return buyOrder(order.getCompany(), order.getQuantity());
        }
        else {
            return sellOrder(order.getCompany(), order.getQuantity());
        }
    }

    ArrayList<String> getCSVLines(String filename) {
        ArrayList<String> lines = new ArrayList<String>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(filename)));
            boolean skipLine = true;

            while(reader.ready()) {
                String line = reader.readLine().trim();
                // skip first line
                if(!skipLine)
                    lines.add(line);
                skipLine = false;
            }
        }
        catch (Exception e) {}

        return lines;
    }

    public static void main(String[] args) {

        OrderProcessor orderProcessor = new OrderProcessor();

        // get file content lines
        ArrayList<String> orderLines = orderProcessor.getCSVLines("input.csv");

        ArrayList<Order> orders = new ArrayList<Order>();

        System.out.println("\nINPUT: ");

        for(String orderLine : orderLines) {
            String[] temp = orderLine.split(",");

            // initialize order objects
            Order order = new Order(Integer.parseInt(temp[0]), temp[1], temp[2], Integer.parseInt(temp[3]));
            orders.add(order);

            // print input order
            System.out.println(order);
        }

        System.out.println("\nOUTPUT: ");

        for(Order order : orders) {

            // execute an order
            int pendingCount = orderProcessor.executeOrder(order);

            String status = pendingCount > 0 ? "OPEN" : "CLOSED";

            // print order status
            System.out.println(order + " Remaining: " + pendingCount + " , " + "Status: " + status);
        }
    }

}
