package controller;

import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;

import model.AbstractModel;
import view.AbstractView;

/**
 * This class mediates and notifies state changes between views and models
 */
public abstract class AbstractController<T extends AbstractModel, E extends AbstractView> implements PropertyChangeListener {
    protected final ConcurrentLinkedDeque<T> models;
    protected final ConcurrentLinkedDeque<E> views;

    /**
     * Creates a new controller with the specified model and view.
     * It will monitor their events. 
     * 
     * @param model
     * @param view
     */
    public AbstractController(T model, E view) {
        this((T[])null,(E[])null);

        setLateModelView(model, view);
    }

    public AbstractController(T[] models, E[] views) {
        this.models = new ConcurrentLinkedDeque<>();
        this.views  = new ConcurrentLinkedDeque<>();

        if (models != null) this.models.addAll(Arrays.asList(models));
        if (views != null) this.views.addAll(Arrays.asList(views));

        for (T e : this.models) {
            e.addPropertyChangeListener(this);
        }

        for (E e : this.views) {
            e.addPropertyChangeListener(this);
        }
    }

    private void setLateModelView(T model, E view) {
        models.add(model);
        views.add(view);
        model.addPropertyChangeListener(this);
        view.addPropertyChangeListener(this);
    }

    public void close() {
        deactivate();
        
        for (T e : models) {
            e.removePropertyChangeListener(this);
            e.close();
        }

        for (E e : views) {
            e.removePropertyChangeListener(this);
        }

        models.clear();
        views.clear();
    }

    /**
     * Returns the model assigned to this controller
     * 
     * @return a model of type T. It is guarenteed to be non-null
     */
    public T getFirstModel() { 
        return models.getFirst(); 
    }

    /**
     * Returns the view assigned to this controller
     * 
     * @return a view of type E. It is guarenteed to be non-null
     */
    public E getFirstView() { 
        return views.getFirst(); 
    }
    
    //  Use this to observe property changes from registered models
    //  and propagate them on to all the views.
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getSource() instanceof AbstractView) {
            // TODO: set model property
        } else {
            for (E view : views) {
                view.modelPropertyChange(evt);
            }
        }
    }
    
    /**
     * Each controller should implement an update method.
     */
    public abstract void update();
    
    /**
     * J'ai pour l'instant mit cette fonction dans AbstractController mais évidemment on peut la déplacer.
     * 
     * @param controller
     * @param funcName
     * @param evt
     */
    @SuppressWarnings("rawtypes")
    void invokeUserInputHandling(AbstractController controller, String funcName, InputEvent evt) {
	    try {
	        Method method = controller.getClass().getMethod(funcName, new Class[] { evt.getClass() });
	        method.invoke(controller, evt);
	    } catch (Exception ex) {
	        System.err.println("FAIL INVOKE: " + controller.getClass().getName() + funcName);
	        ex.printStackTrace();
	    }
	}

    /**
     * Activates and registers underlying model and view's sub-components.
     */
    public void activate() {
        for (E view : views) {
            view.registerUIComponents();
        }
    }

    /**
     * Deactivates and registers underlying model and view's sub-components.
     */
    public void deactivate() {
        for (E view : views) {
            view.unregisterUIComponents();
        }
    }
}
