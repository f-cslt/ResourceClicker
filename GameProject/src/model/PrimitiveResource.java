package model;

/**
 * This class represents any game resource.
 */
public class PrimitiveResource extends Resource {
    public enum HarvestMode {
        BLOCKING,
        DIFFERED,
        IMMEDIATE,
    }

    private final HarvestMode harvestMode;
    private final long growthTime;
    private ResourceHarvestToken harvestToken;

    private long timeoutNextAvailable;

    /**
     * Creates a new resource of the specified type.
     * 
     * @param harvestMode
     * @param resourceType
     * @param timeUntilAvailable The amount of time it takes to produce a the resource. A negative value means a random time.
     */
    public PrimitiveResource(Integer x, Integer y, RootObject rootObject) {
        super(x, y, rootObject);

        this.harvestMode    = chooseHarvestMode(rootObject.freq);
        this.growthTime     = rootObject.freq*1000;

        this.timeoutNextAvailable   = 0;
        resetTimout(); // init timeout
    }

    /**
     * Gets the harvest mode of this resource.
     * 
     * @return the harvest mode
     */
    public HarvestMode getHarvestMode() { 
        return harvestMode; 
    }

    @Override
    public long getTimeLeftUntilReady() {
        long res = Math.max(0, timeoutNextAvailable - System.currentTimeMillis());

        if (res == 0) {
            setStatus(GameEntityStatus.READY);
        }

        return res;
    }

    @Override
    public long getTimeToComplete() {
        return growthTime;
    }

    @Override
    public void triggerAction(PlayerModel player) {
        if (isGrown()) {
            player.addResourceToken(getHarvestToken());
        }
    }

    /**
     * Gets a harvest token for this resource if available.
     * 
     * @return the harvest token or {@code null}
     */
    public ResourceHarvestToken getHarvestToken() {
        // Only one token can exist
        if (harvestToken == null && isGrown()) {
            setStatus(GameEntityStatus.READY);
            harvestToken = new ResourceHarvestToken(getRootObject().freq*100, this);
            return harvestToken;
        }

        return null;
    }

    @Override
    public PrimitiveResource clone() {
        return new PrimitiveResource(getX(), getY(), getRootObject());
    }

    @Override
    public String toString() {
        return String.format(
                    "%s(%d, %d, %s, %d, %d)", 
                    getClass().getName(),
                    getX(),
                    getY(),
                    getHarvestMode(),
                    growthTime,
                    getSpriteId()
                );
    }

    private HarvestMode chooseHarvestMode(int freq) {
        if (freq < 3) {
            return HarvestMode.IMMEDIATE;
        } else if (freq < 5) {
            return HarvestMode.DIFFERED;
        }

        return HarvestMode.BLOCKING;
    }

    private void resetTimout() { 
        setStatus(GameEntityStatus.BUSY);
        timeoutNextAvailable = System.currentTimeMillis() + growthTime; 
    }

    private boolean isGrown() { 
        return getTimeLeftUntilReady() == 0;
    }

    /**
     * This class is to be returned when a player harvests a resource.
     */
    public class ResourceHarvestToken {
        private final long availabilityTime;
        private boolean isConsumed = false;

        private ResourceHarvestToken(long availabilityTimeout, Resource resource) {
            this.availabilityTime   = System.currentTimeMillis() + availabilityTimeout;
        }

        public HarvestMode getHarvestMode() { 
            return harvestMode;       
        }

        public boolean isready() { 
            return System.currentTimeMillis() >= availabilityTime; 
        }

        /**
         * A call to this method will consume the token and invalidate it.
         * 
         * @return the type of the harvested resource
         */
        public RootObject harvest() {
            if (isConsumed) {
                System.err.println("Resource can only be harvested once");
                return null;
            }

            if (isready()) {
                resetTimout();
                isConsumed = true;
                harvestToken = null;
                return getRootObject();
            }

            return null;
        }
    }
}
