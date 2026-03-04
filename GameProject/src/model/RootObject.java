package model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class RootObject {
    public final String name;
    public final int spriteId;
    public final int freq;
    public final Class<? extends GameEntity> targetedClass;

    /**
     * Constructs a {@code RootObject}.
     * 
     * @param name the name of this object.
     * @param spriteId the sprite id of this object.
     * @param freq the apparition frequency/rarity of this object.
     * @param targetedClass the class representing this object on the map.
     */
    public RootObject(String name, int spriteId, int freq, Class<? extends GameEntity> targetedClass) {
        this.name = name;
        this.spriteId = spriteId;
        this.freq = freq;
        this.targetedClass = targetedClass;
    }

    /**
     * Constructs a new instance of {@code targetedClass} if not null. 
     * @see GameEntity#GameEntity(int, int, boolean, RootObject)
     * 
     * @param x
     * @param y
     * @return
     * @throws NoSuchMethodException if a matching constructor is not found
     */
    public GameEntity constructEntity(int x, int y) throws NoSuchMethodException {
        if (targetedClass == null) return null;

        GameEntity res;
        Constructor<? extends GameEntity> constructor  = targetedClass.getConstructor(new Class[]{ Integer.class, Integer.class, RootObject.class});

        try {
            res = constructor.newInstance(x, y, this);
        } catch (InstantiationException |
                 IllegalAccessException |
                 IllegalArgumentException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RootObject) {
            RootObject rhs = (RootObject)obj;

            return  name.equals(rhs.name)       &&
                    spriteId == rhs.spriteId    &&
                    freq == rhs.freq            &&
                    (targetedClass == null && rhs.targetedClass == null || targetedClass.equals(rhs.targetedClass));
        }

        return false;
    }
}
