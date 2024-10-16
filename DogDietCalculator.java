import java.util.Scanner;

/**
 * This class calculates the daily caloric needs and food amount for dogs
 * based on their weight, activity level, and weight management goals.
 */
public class DogDietCalculator {
    private final double currentWeight; // Dog's current weight in kg
    private final double goalWeight; // Dog's goal weight in kg
    private final ActivityLevel activityLevel;
    private final double currentFoodAmount; // Current daily food amount in cups
    
    /**
     * Enum representing different activity levels for dogs.
     */
    public enum ActivityLevel {
        LOW, MODERATE, HIGH
    }
    
    /**
     * Constructor for DogDietCalculator.
     * @param currentWeight The dog's current weight in kg.
     * @param goalWeight The dog's goal weight in kg.
     * @param activityLevel The dog's activity level.
     * @param currentFoodAmount The current daily food amount in cups.
     */
    public DogDietCalculator(double currentWeight, double goalWeight, ActivityLevel activityLevel, double currentFoodAmount) {
        this.currentWeight = currentWeight;
        this.goalWeight = goalWeight;
        this.activityLevel = activityLevel;
        this.currentFoodAmount = currentFoodAmount;
    }
    
    /**
     * Calculates the daily caloric needs for the dog.
     * @return The recommended daily calorie intake.
     */
    public double calculateDailyCalories() {
        // Use the average of current and goal weight for calorie calculation
        double averageWeight = (currentWeight + goalWeight) / 2;
        double baseCalories = 30 * averageWeight + 70;
        
        // Adjust based on activity level
        return switch (activityLevel) {
            case LOW -> baseCalories * 0.8;
            case HIGH -> baseCalories * 1.2;
            default -> baseCalories;
        }; // MODERATE
    }
    
    /**
     * Calculates the daily food amount based on caloric needs and food density.
     * @param caloriesPerCup The caloric density of the dog food (calories per cup).
     * @return The recommended daily food amount in cups.
     */
    public double calculateDailyFoodAmount(double caloriesPerCup) {
        double dailyCalories = calculateDailyCalories();
        return dailyCalories / caloriesPerCup;
    }

    /**
     * Calculates a titration plan to gradually adjust the dog's food intake.
     * @param caloriesPerCup The caloric density of the dog food (calories per cup).
     * @return A string describing the titration plan.
     */
    public String calculateTitrationPlan(double caloriesPerCup) {
        double targetFoodAmount = calculateDailyFoodAmount(caloriesPerCup);
        double weeklyAdjustment = (targetFoodAmount - currentFoodAmount) / 4; // Adjust over 4 weeks

        StringBuilder plan = new StringBuilder();
        plan.append("4-Week Titration Plan:\n");
        for (int week = 1; week <= 4; week++) {
            double adjustedAmount = currentFoodAmount + (weeklyAdjustment * week);
            plan.append(String.format("Week %d: %.2f cups per day\n", week, adjustedAmount));
        }
        plan.append(String.format("Final target: %.2f cups per day", targetFoodAmount));

        return plan.toString();
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Do you have a fat doge? Let's create a diet plan for the goodest boi/gurl.\n");
            
            double currentWeightInKg = getDogWeight(scanner, "current");
            double goalWeightInKg = getDogWeight(scanner, "goal");
            ActivityLevel activityLevel = getDogActivityLevel(scanner);
            double currentFoodAmount = getCurrentFoodAmount(scanner);
            double caloriesPerCup = getFoodCaloricDensity(scanner);
            
            DogDietCalculator calculator = new DogDietCalculator(currentWeightInKg, goalWeightInKg, activityLevel, currentFoodAmount);
            double dailyCalories = calculator.calculateDailyCalories();
            double dailyFoodAmount = calculator.calculateDailyFoodAmount(caloriesPerCup);
            String titrationPlan = calculator.calculateTitrationPlan(caloriesPerCup);
            
            displayResults(dailyCalories, dailyFoodAmount, titrationPlan);
        }
    }

    private static double getDogWeight(Scanner scanner, String weightType) {
        double weightInKg = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.printf("Enter your dog's %s weight followed by a space and then 'kg' or 'lbs' (e.g., '30 kg' or '66 lbs') then press enter: ", weightType);
            String input = scanner.nextLine().trim().toLowerCase();

            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid input format. Please try again.");
                continue;
            }

            try {
                double weightInput = Double.parseDouble(parts[0]);
                String unit = parts[1];

                switch (unit) {
                    case "lbs" -> {
                        weightInKg = weightInput * 0.453592;
                        System.out.println("Weight converted to kg: " + String.format("%.2f", weightInKg) + " kg");
                        validInput = true;
                    }
                    case "kg" -> {
                        weightInKg = weightInput;
                        validInput = true;
                    }
                    default -> System.out.println("Invalid unit. Please use 'kg' or 'lbs'.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format. Please enter a valid number for weight.");
            }
        }
        return weightInKg;
    }

    private static ActivityLevel getDogActivityLevel(Scanner scanner) {
        ActivityLevel activityLevel = ActivityLevel.MODERATE;
        boolean validInput = false;
        while (!validInput) {
            System.out.println("Select your dog's activity level:");
            System.out.println("1. Low");
            System.out.println("2. Moderate");
            System.out.println("3. High");
            System.out.print("Enter the number (1-3): ");
            String input = scanner.nextLine().trim();

            try {
                int activityChoice = Integer.parseInt(input);
                switch (activityChoice) {
                    case 1 -> {
                        activityLevel = ActivityLevel.LOW;
                        validInput = true;
                    }
                    case 2 -> {
                        activityLevel = ActivityLevel.MODERATE;
                        validInput = true;
                    }
                    case 3 -> {
                        activityLevel = ActivityLevel.HIGH;
                        validInput = true;
                    }
                    default -> System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
        return activityLevel;
    }

    private static double getCurrentFoodAmount(Scanner scanner) {
        double currentAmount = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter the current amount of food you're feeding your dog per day (in cups): ");
            String input = scanner.nextLine().trim();

            try {
                currentAmount = Double.parseDouble(input);
                if (currentAmount > 0) {
                    validInput = true;
                } else {
                    System.out.println("Amount must be a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return currentAmount;
    }

    private static double getFoodCaloricDensity(Scanner scanner) {
        double caloriesPerCup = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter the caloric density of the dog food (calories per cup), this information should be in on the back of the food bag: ");
            String input = scanner.nextLine().trim();

            try {
                caloriesPerCup = Double.parseDouble(input);
                if (caloriesPerCup > 0) {
                    validInput = true;
                } else {
                    System.out.println("Caloric density must be a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return caloriesPerCup;
    }

    private static void displayResults(double dailyCalories, double dailyFoodAmount, String titrationPlan) {
        System.out.println("\nResults:");
        System.out.println("Daily calories: " + String.format("%.2f", dailyCalories));
        System.out.println("Target food amount per day: " + String.format("%.2f", dailyFoodAmount) + " cups");
        System.out.println("\nTitration Plan:");
        System.out.println(titrationPlan);
    }
}