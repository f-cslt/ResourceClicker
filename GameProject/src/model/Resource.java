package model;

/**
 * This class represents any game resource.
 */
public class Resource extends GameEntity {
    /**
     * Creates a new resource of the specified type.
     * 
     * @param harvestMode
     * @param resourceType
     * @param timeUntilAvailable The amount of time it takes to produce a the resource. A negative value means a random time.
     */
    public Resource(Integer x, Integer y, RootObject rootObject) {
        super(x, y, false, rootObject);
    }

    @Override
    public Resource clone() {
        return new Resource(getX(), getY(), getRootObject());
    }
}
