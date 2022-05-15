package app.ui;

import java.util.Map;

/**
 * This record has the function to wrap the possible options needed to construct a scheduling algorithm.
 * It's goal is to store the UI provided data to the controller and to be open for change.
 * This means that when new scheduling algorithms would be added to the system with new requirements to filter/prioritize/... the orders
 * we can add them without adding parameters to the controller methods. Instead just adding a field inside this record so that the controller can unpack them whatever he needs
 * at that specific moment for a specific scheduling algorithm.
 *
 * @param options a Map that serves as the chosen options to prioritize the orders for the specification batch algorithm.
 */
public record AlgorithmOptionsWrapper(Map<String, String> options) {
}
