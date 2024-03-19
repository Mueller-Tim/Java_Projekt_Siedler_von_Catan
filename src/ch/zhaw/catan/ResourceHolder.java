package ch.zhaw.catan;

import java.util.Map;

/**
 * Abstract super class for resource holding entities
 *
 */
public abstract class ResourceHolder {
    private Map<Config.Resource, Integer> resources;

    /**
     * Constructor sets the start resources
     */
    public ResourceHolder(Map<Config.Resource, Integer> resources) {
        this.resources = resources;
    }

    /**
     * This method return a list with the number of resources of a player
     *
     * @return List with resources of a player
     */
    public Map<Config.Resource, Integer> getResources() {
        return resources;
    }

    /**
     * 
     * Adds the provided map of resources to its resource stock
     * @param Map<Config.Resource, Integer> resourceMap
     */
    public void addResource(Map<Config.Resource, Integer> resourceMap) {
        resourceMap.forEach((resource, amount) -> resources.put(resource, resources.get(resource) + amount));
    }

    /**
     * Subtracts the provided Map of resources from its resource stock
     * @param type
     * @param toDrop
     */
    public Map<Config.Resource, Integer> removeCards(Config.Resource type, int toDrop) {
        Integer currentAmount = resources.get(type);
        if (currentAmount != null && currentAmount >= toDrop) {
            resources.put(type, currentAmount - toDrop);
            return Map.of(type, toDrop);
        } else {
            throw new IllegalArgumentException("Resource holder owns less than " + toDrop + " Cards of type " + type);
        }
    }

    /**
     * Counts the amount of resource cards in its stock 
     * @return int amount of cards
     */
    public int countCards() {
        int sum = 0;
        for (int resourceType : resources.values()) {
            sum += resourceType;
        }
        return sum;
    }

    /**
     * Counts the amount of resource cards for a single resource
     * @param resource
     * @return int amount of cards of resource
     */
    public int getAmountOfResource(Config.Resource resource) {
        return resources.get(resource);
    }
}
