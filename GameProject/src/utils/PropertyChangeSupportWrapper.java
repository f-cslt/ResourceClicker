package utils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * This is a utility class that wrappes basic functionalities of {@link PropertyChangeSupport}.
 * 
 * <p>Example usage
 * <pre>
 * public class MyClass extends PropertyChangeSupportWrapper {
 *      public int setValue(int newValue) { 
 *          firePropertyChange(propertyName, oldValue, newValue); 
 *          ... 
 *      }
 * }
 * </pre>
 * </p>
 */
public abstract class PropertyChangeSupportWrapper {
    protected final PropertyChangeSupport propertyChangeSupport;

    public PropertyChangeSupportWrapper() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }
}
