package org.example;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LogisticsApp {

    public static void main(String[] args) throws IOException {
        List<Package> packages = loadPackages("packages.txt");

        Map<String, Map<LocalDate, List<Package>>> groupedPackages = new HashMap<>();

        for (Package p : packages) {
            groupedPackages
                    .computeIfAbsent(p.getLocation(), k -> new HashMap<>())
                    .computeIfAbsent(p.getDeliveryDate(), k -> new ArrayList<>())
                    .add(p);
        }

        for (Map.Entry<String, Map<LocalDate, List<Package>>> locationEntry : groupedPackages.entrySet()) {
            for (Map.Entry<LocalDate, List<Package>> dateEntry : locationEntry.getValue().entrySet()) {
                String location = locationEntry.getKey();
                LocalDate date = dateEntry.getKey();
                List<Package> packageGroup = dateEntry.getValue();

                int groupValue = packageGroup.stream().mapToInt(Package::getValue).sum();
                int groupRevenue = packageGroup.stream().mapToInt(Package::getDistance).sum();

                Thread deliveryThread = new Thread(() -> {
                    try {
                        Thread.sleep(packageGroup.get(0).getDistance() * 1000);
                        System.out.println("--------------------------------------------------");
                        System.out.printf("[Delivering for %s and date %s in %d seconds]\n",
                                location, date, packageGroup.get(0).getDistance());
                        System.out.println("--------------------------------------------------");
                        System.out.printf("Group value for %s on %s: %d\n", location, date, groupValue);
                        System.out.printf("Group revenue for %s on %s: %d\n", location, date, groupRevenue);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                deliveryThread.start();
            }
        }

        int totalValue = packages.stream().mapToInt(Package::getValue).sum();
        int totalRevenue = packages.stream().mapToInt(Package::getDistance).sum();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("Total value of all delivered packages: %d\n", totalValue);
        System.out.printf("Total revenue for all deliveries: %d\n", totalRevenue);
    }

    public static List<Package> loadPackages(String fileName) throws IOException {
        List<Package> packages = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            String location = parts[0];
            int distance = Integer.parseInt(parts[1]);
            int value = Integer.parseInt(parts[2]);
            LocalDate date = LocalDate.parse(parts[3], formatter);

            packages.add(new Package(location, distance, value, date));
        }

        reader.close();
        return packages;
    }
}