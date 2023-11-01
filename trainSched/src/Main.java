import java.util.Scanner;

// Train class
class Train {
    int trainID;
    String trainName;
    String source;
    String destination;
    double distance;
    double speed;
    String[] stations;
    int[] stationTimes;
    int[] departureTimes;
    int[] arrivalTimes;
    Train next;

    public Train(int trainID, String trainName, String source, String destination, double distance, double speed) {
        this.trainID = trainID;
        this.trainName = trainName;
        this.source = source;
        this.destination = destination;
        this.distance = distance;
        this.speed = speed;
        this.stations = new String[0];
        this.stationTimes = new int[0];
        this.departureTimes = new int[0];
        this.arrivalTimes = new int[0];
        this.next = null;
    }
}

// Train schedule class
class TrainSchedule {
    Train head;

    public TrainSchedule() {
        this.head = null;
    }

    // Add a train to the schedule
    // Time Complexity: O(1)
    // Space Complexity: O(N) - where N is the number of stations
    public void AddTrain(int trainID, String trainName, String source, String destination, double distance, double speed, String[] stations, int[] stationTimes, int[] departureTimes, int[] arrivalTimes) {
        Train newTrain = new Train(trainID, trainName, source, destination, distance, speed);
        newTrain.stations = stations;
        newTrain.stationTimes = stationTimes;
        newTrain.departureTimes = departureTimes;
        newTrain.arrivalTimes = arrivalTimes;
        newTrain.next = head;
        head = newTrain;
    }

    // Display the train schedule
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public void DisplaySchedule() {
        System.out.println("Train Schedule:");
        System.out.println("------------------------------------------------------------------");
        System.out.println("| Train ID | Source | Destination | Distance (km) | Speed (km/h) |");
        System.out.println("------------------------------------------------------------------");

        Train current = head;
        while (current != null) {
            System.out.printf("| %-9d| %-7s| %-12s| %-15.2f| %-12.2f|%n",
                    current.trainID, current.source, current.destination,
                    current.distance, current.speed);
            current = current.next;
        }

        System.out.println("------------------------------------------------------------------");
    }

    // Search for a train by its ID
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public Train SearchTrainByID(int trainID) {
        Train current = head;
        while (current != null) {
            if (current.trainID == trainID) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    // Search for a train by its destination
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public Train SearchByDestination(String destination) {
        Train current = head;
        while (current != null) {
            if (current.destination.equals(destination)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    // Cancel and remove a train from the schedule
    // Time Complexity: O(N)  , where N is the number of trains in the schedule
    // Space Complexity: O(1)
    public void CancelTrain(int trainID) {
        Train current = head;
        Train previous = null;

        while (current != null) {
            if (current.trainID == trainID) {
                // Found the train, mark it as canceled
                current.speed = 0; // Set speed to 0 to indicate cancellation
                System.out.println("Train " + current.trainName + " has been canceled due to a track error.");
                // Remove the train from the schedule
                if (previous == null) {
                    head = current.next; // If the canceled train is the first one
                } else {
                    previous.next = current.next;
                }
                return; // Train found and canceled, exit the method
            }
            previous = current;
            current = current.next;
        }
        System.out.println("Train not found in the schedule.");
    }


    // Handle constraints on a train's speed
    // Time Complexity: O(1)
    // Space Complexity: O(1)
    public void HandleConstraints(Train train, String constraintType) {
        double originalSpeed = train.speed;

        if ("Climate Change".equalsIgnoreCase(constraintType)) {
            boolean climateChangeWarning = true;
            if (climateChangeWarning) {
                train.speed *= 0.8; // Reduce speed by 20%
                System.out.println("Climate Change constraint applied.");
            } else {
                System.out.println("No climate change constraint detected.");
            }
        } else if ("Track Error".equalsIgnoreCase(constraintType)) {
            boolean trackError = true; // Checking for a track error
            if (trackError) {
                // if Track error detected, cancel the train
                CancelTrain(train.trainID);
                return;
            } else {
                System.out.println("No track error constraint detected.");
            }
        } else {
            System.out.println("Invalid constraint type.");
        }

        // Updating the train's speed in the timetable if it's not canceled
        if (train.speed > 0) {
            UpdateTimetableSpeed(train, originalSpeed);
            System.out.println("Original Speed: " + originalSpeed + " km/h");
            System.out.println("Updated Speed: " + train.speed + " km/h");
        }
    }

    // Update train's speed in the timetable
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    private void UpdateTimetableSpeed(Train train, double originalSpeed) {
        Train current = head;
        while (current != null) {
            if (current.trainID == train.trainID) {
                current.speed = train.speed;
            }
            current = current.next;
        }
    }


    // Calculate travel time for a given train
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public double CalculateTravelTime(int trainID) {
        Train current = head;
        while (current != null) {
            if (current.trainID == trainID) {
                return current.distance / current.speed;
            }
            current = current.next;
        }
        return -1; // Train not found
    }

    // Track the route of a train
    // Time Complexity: O(N)
    // Space Complexity: O(1)
    public void TrackTrainRoute(int trainID) {
        Train train = SearchTrainByID(trainID);
        if (train != null) {
            System.out.println("Train Route:");
            System.out.println("-------------------------------------------------");
            System.out.println("| Station | Time to Next Station (minutes) |");
            System.out.println("-------------------------------------------------");

            int totalMinutes = 0;
            int stationCount = train.stations.length;

            for (int i = 0; i < stationCount; i++) {
                String currStation = train.stations[i];
                int timeToNextStation = i < stationCount - 1 ? train.stationTimes[i] : 120; // Default 2 hours to destination

                System.out.printf("| %-7s| %-30d|%n", currStation, timeToNextStation);
                totalMinutes += timeToNextStation;
            }

            System.out.println("-------------------------------------------------");
            System.out.println("Total Travel Time from " + train.source + " to " + train.destination + ": " + totalMinutes + " minutes");
        } else {
            System.out.println("Train not found.");
        }
    }
}


public class Main {
    public static void main(String[] args) {
        TrainSchedule schedule = new TrainSchedule();
        Scanner scanner = new Scanner(System.in);

        // Hardcoding station names, times, departure times, and arrival times for a train with middle stations
        String[] puneToPanvelStations = {"Pune", "Station1", "Station2", "Station3", "Panvel"};
        int[] puneToPanvelTimes = {0, 10, 15, 20, 30};  // Times in minutes
        int[] puneToPanvelDepartureTimes = {800, 810, 825, 845, 915};  // Departure times in HHMM format
        int[] puneToPanvelArrivalTimes = {750, 805, 820, 840, 910};  // Arrival times in HHMM format

        schedule.AddTrain(1, "Pune to Panvel Express", "Pune", "Panvel", 150, 60, puneToPanvelStations, puneToPanvelTimes, puneToPanvelDepartureTimes, puneToPanvelArrivalTimes);

        int choice;
        do {
            System.out.println("\nTrain Scheduling and Tracking System Menu:");
            System.out.println("1. Add Train");
            System.out.println("2. Display Schedule");
            System.out.println("3. Search Train by ID");
            System.out.println("4. Search Train by Destination");
            System.out.println("5. Handle Constraints for a Train");
            System.out.println("6. Calculate Travel Time");
            System.out.println("7. Track Train Route");
            System.out.println("8. Exit");

            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter Train ID: ");
                    int trainID = scanner.nextInt();
                    System.out.print("Enter Train Name: ");
                    String trainName = scanner.next();
                    scanner.nextLine();
                    System.out.print("Enter Source: ");
                    String source = scanner.next();
                    System.out.print("Enter Destination: ");
                    String destination = scanner.next();
                    System.out.print("Enter Distance: ");
                    double distance = scanner.nextDouble();
                    System.out.print("Enter Speed: ");
                    double speed = scanner.nextDouble();
                    scanner.nextLine();

                    // Add custom stations and times
                    System.out.print("Enter station names separated by spaces: ");
                    String stationsInput = scanner.nextLine();
                    String[] customStations = stationsInput.split(" ");

                    System.out.print("Enter times in minutes separated by spaces: ");
                    String timesInput = scanner.nextLine();
                    String[] timesArray = timesInput.split(" ");
                    int[] times = new int[timesArray.length];

                    for (int i = 0; i < timesArray.length; i++) {
                        times[i] = Integer.parseInt(timesArray[i]);
                    }

                    schedule.AddTrain(trainID, trainName, source, destination, distance, speed, customStations, times, puneToPanvelDepartureTimes, puneToPanvelArrivalTimes);

                    break;
                case 2:
                    System.out.println("Existing Schedule:");
                    schedule.DisplaySchedule();
                    break;
                case 3:
                    System.out.print("Enter Train ID to search: ");
                    int searchTrainID = scanner.nextInt();
                    Train foundTrain = schedule.SearchTrainByID(searchTrainID);
                    if (foundTrain != null) {
                        System.out.println("Train found. Train Name: " + foundTrain.trainName);
                    } else {
                        System.out.println("Train not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter Destination to search: ");
                    String searchDestination = scanner.next();
                    Train foundByDestination = schedule.SearchByDestination(searchDestination);
                    if (foundByDestination != null) {
                        System.out.println("Train found. Train Name: " + foundByDestination.trainName);
                    } else {
                        System.out.println("Train not found.");
                    }
                    break;
                case 5:
                    System.out.print("Enter Train ID to handle constraints: ");
                    int trainIDToHandle = scanner.nextInt();
                    Train trainToHandle = schedule.SearchTrainByID(trainIDToHandle);
                    if (trainToHandle != null) {
                        System.out.println("Select the type of constraint:");
                        System.out.println("1. Climate Change");
                        System.out.println("2. Track Error");
                        System.out.print("Enter your choice: ");
                        int constraintChoice = scanner.nextInt();

                        if (constraintChoice == 1) {
                            // Handle climate change constraint
                            schedule.HandleConstraints(trainToHandle, "Climate Change");
                        } else if (constraintChoice == 2) {
                            // Handle track error constraint
                            schedule.HandleConstraints(trainToHandle, "Track Error");
                        } else {
                            System.out.println("Invalid choice for constraint type.");
                        }
                    } else {
                        System.out.println("Train not found.");
                    }
                    break;


                case 6:
                    System.out.print("Enter Train ID to calculate travel time: ");
                    int trainIDToCalculateTime = scanner.nextInt();
                    double travelTime = schedule.CalculateTravelTime(trainIDToCalculateTime);

                    if (travelTime != -1) {
                        System.out.println("Estimated travel time for the train: " + travelTime + " hours.");
                    } else {
                        System.out.println("Train not found.");
                    }
                    break;
                case 7:
                    System.out.print("Enter Train ID to track route: ");
                    int trainIDToTrackRoute = scanner.nextInt();
                    schedule.TrackTrainRoute(trainIDToTrackRoute);
                    break;
                case 8:
                    System.out.println("Exiting the system.");
                    break;
                case 9:
                    //Time Complexity: O(N)
                    //Space Complexity: O(1)
                    System.out.print("Enter Train ID to specify no middle stations: ");
                    int trainIDWithNMS = scanner.nextInt();
                    Train NoMiddleStations = schedule.SearchTrainByID(trainIDWithNMS);
                    if (NoMiddleStations != null) {
                        // Set time to next station and time to destination to 2 hours (120 minutes)
                        NoMiddleStations.stationTimes[0] = 120;
                        NoMiddleStations.stationTimes[NoMiddleStations.stationTimes.length - 1] = 120;
                        System.out.println("Time to Next Station and Time to Destination set to 2 hours.");
                    } else {
                        System.out.println("Train not found.");
                    }
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 8);
    }
}

//overall Time Complexity: O(N)
//overall Space Complexity: O(N)