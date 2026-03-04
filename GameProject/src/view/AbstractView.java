package view;

import java.awt.Component;
import java.awt.Graphics2D;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;
import java.util.List;

import utils.PropertyChangeSupportWrapper;


/**
 * Base class for any view. Subclasses must only handle UI logic 
 * and delegate to the model through a controller otherwise.
 */
public abstract class AbstractView extends PropertyChangeSupportWrapper{
    protected boolean isUIRegistered = false;

    public AbstractView() {
        super();
    }

    /**
     * Called when a new event is fired by the underlying model
     * 
     * @param evt incoming change
     */
    public void modelPropertyChange(final PropertyChangeEvent evt) {}

    /**
     * 
     * 
     * @param g graphic context
     */
    public void draw(Graphics2D g) {}

    protected List<Component> getUIComponents() { 
        return Arrays.asList(); 
    }

    public void registerUIComponents() {
        for (Component e : getUIComponents()) {
            GamePanelView.getGamePanelView().add(e);
        }

        GamePanelView.getGamePanelView().revalidate();
        isUIRegistered = true;
    }

    public void unregisterUIComponents() {
        for (Component e : getUIComponents()) {
            GamePanelView.getGamePanelView().remove(e);
        }
        
        GamePanelView.getGamePanelView().revalidate();
        isUIRegistered = false;
    }
}

