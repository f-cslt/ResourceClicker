package model;


/**
 * Classe qui stocke les EVENTS. Il faut changer la façon dont cette classe est faite évidemment.
 * Je voulais juste avoir une implémentation rapide et efficace. Il faudrait soit en faire une classe Events
 * avec des sous classe PlayerEvents, MapEvents ou bien avoir des enum dans la classe. Ou bien en faire une
 * classe dans utils.
 * 
 */
public class Events {

   public static final String EVENT_MAP_AREA_CLICKED = "MAP_AREA_CLICKED";
   public static final String EVENT_MAP_AREA_HOVERED = "MAP_AREA_HOVERED";
   public static final String EVENT_NEW_ITEM = "NewItem";

}
