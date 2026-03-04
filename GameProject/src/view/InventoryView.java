package view;

import static utils.GameConstants.ObjectConstants.OBJECTS_CONFIG;
import static utils.GameConstants.TilesConstants.SCREEN_HEIGHT;
import static utils.GameConstants.TilesConstants.SCREEN_WIDTH;
import static utils.GameConstants.TilesConstants.TILE_SIZE;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.Box;
import javax.swing.Box.Filler;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import model.AtlasSprite;
import model.GameState;
import model.InventoryModel;
import model.PlayerModel;
import model.InventoryModel.InventoryItem;

/**
 * This class will render the specified inventory. It is configured to listen to property changes
 * and to automatically reload upon one.
 */
public class InventoryView extends AbstractView {
    private static final int INVENTORY_VIEW_WIDTH   = (int)(SCREEN_WIDTH*0.5);
    private static final int INVENTORY_VIEW_HEIGHT  = (int)(SCREEN_HEIGHT*0.5);

    protected final String title;
    protected final Map<String, Consumer<Object>> options;
    protected final Object userData;
    
    private JPanel panel;
    private JTabbedPane tabbedPane;

    /**
     * Constructs an {@code InventoryView} with no options.
     * 
     * @param title the title to display
     */
    public InventoryView(String title) {
        this(title, null, null);
    }    
    /**
     * Constructs an {@code InventoryView} with the specified configuration.
     * 
     * @param title the title to display.
     * @param options option buttons to be displayed below the title.
     * @param userData data to be passed when an item is selected.
     */
    public InventoryView(String title, Map<String, Consumer<Object>> options, Object userData) {
        this.title              = title;
        this.options            = Objects.requireNonNullElse(options, Map.of());
        this.userData           = userData;
        this.tabbedPane         = new JTabbedPane();
        this.tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    /**
     * Creates the final inventory view.
     * 
     * @param player the player to interact with.
     * @param inventoryModels the inventories to display.
     */
    @SuppressWarnings("unused")
    public void initUI(PlayerModel player, InventoryModel... inventoryModels) {
        Box uibox = Box.createVerticalBox();
    
        JLabel title = new JLabel(this.title);      
        uibox.add(centerHorizontalComponent(title));
        uibox.add(Box.createVerticalStrut(20));
        
        JComponent optionButtons = makeOptionButtons();
        if (optionButtons != null) {
            uibox.add(optionButtons);
        }

        JComponent header = makeHeader(player);
        if (header != null) {
            uibox.add(header);
        }

        for (InventoryModel e : inventoryModels) {
            addInventoryTab(player, e);
        }
        uibox.add(tabbedPane);
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> GameState.getGameState().popState());
        uibox.add(Box.createVerticalStrut(8));
        uibox.add(centerHorizontalComponent(closeButton));
        
        initPanel(uibox);
    }

    private void initPanel(JComponent comp) {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(comp);
        panel.setPreferredSize(new Dimension(INVENTORY_VIEW_WIDTH, INVENTORY_VIEW_HEIGHT));
        panel.setBackground(new Color(227, 198, 180));
    }

    private void addInventoryTab(PlayerModel player, InventoryModel inventory) {
        JScrollPane comp = new JScrollPane(makeTab(player, inventory));

        comp.getVerticalScrollBar().setUnitIncrement(16);
        comp.getHorizontalScrollBar().setUnitIncrement(16);

        int i = tabbedPane.indexOfTab(inventory.getTitle());
        
        if (i >= 0) {
            int selectedIndex = tabbedPane.getSelectedIndex();
            tabbedPane.removeTabAt(i);
            tabbedPane.insertTab(inventory.getTitle(), null, comp, null, i);
            tabbedPane.setSelectedIndex(selectedIndex);
        } else {
            tabbedPane.addTab(inventory.getTitle(), comp);
        }
    }

    private JComponent centerHorizontalComponent(JComponent comp) {
        Box box = Box.createHorizontalBox();
        
        box.add(Box.createHorizontalGlue());
        box.add(comp);
        box.add(Box.createHorizontalGlue()); 

        return box;
    }

    /**
     * Reloads the UI and updates the specified inventories.
     * 
     * @param player the player to interact with.
     * @param inventoryModels inventories to update.
     */
    public void reloadUI(PlayerModel player, InventoryModel... inventoryModels) {
        unregisterUIComponents();
        initUI(player, inventoryModels);
        registerUIComponents();
    }

    @SuppressWarnings("unused")
    private JComponent makeOptionButtons() {
        Box row = Box.createHorizontalBox();
        
        options.forEach((k, v) -> {
            JButton button = new JButton(k);
            button.addActionListener(e -> v.accept(userData));
            button.setFocusable(false);

            row.add(button);
        });

        row.add(Box.createHorizontalGlue());

        return row.getComponentCount() != 0 ? row : null;
    }

    protected JComponent makeTab(PlayerModel player, InventoryModel inventory) {
        JPanel panel = new JPanel(false);
        Box vbox = Box.createVerticalBox();

        for (InventoryItem e : inventory.getInventory()) {
            vbox.add(makeItem(new InventoryItemInfo(player, inventory, e)));
            vbox.add(Box.createVerticalStrut(8));
        }
        
        panel.setLayout(new GridLayout(1, 1));
        panel.add(vbox);
        return panel;
    }

    /**
     * Creates a horizontal strut just like {@link Box#createHorizontalStrut(int)} but with no height.
     * 
     * @param width the width of the invisible component, in pixels >= 0
     * @return the component.
     */
    protected static JComponent createHorizontalStrut(int width) {
        return new Filler(new Dimension(width,0), new Dimension(width,0), new Dimension(width, 0));
    }

    /**
     * Creates the item view.
     * 
     * @param player the player to interact with.
     * @param inventory the currently active inventory.
     * @param item the currently selected item.
     * @return the item view
     */
    protected JComponent makeItem(InventoryItemInfo info) {
        InventoryItem item = info.item;
        AtlasSprite sprite = OBJECTS_CONFIG.get(item.entity.spriteId).atlasSprite;

        Box row = Box.createHorizontalBox();

        JLabel icon     = getLabelFromBufferedImage(sprite.getImg());
        JLabel itemName = new JLabel(item.entity.name);
        
        if (item.getQuantity() != Integer.MAX_VALUE ) {
            row.add(new JLabel(item.getQuantity().toString()));
        }
        
        row.add(createHorizontalStrut(20));
        row.add(icon);
        row.add(createHorizontalStrut(10));
        row.add(itemName);
        row.add(Box.createHorizontalGlue());

        JComponent selectionButtons = makeSelectionButtons(info);
        if (selectionButtons != null) {
            row.add(selectionButtons);
        }

        return row;
    }

    /**
     * Constructs a {@link JLabel} from a {@link BufferedImage}.
     * 
     * @param img the input image
     * @return the image as a label
     */
    protected JLabel getLabelFromBufferedImage(BufferedImage img) {
        int imgSize = (int)(TILE_SIZE*0.7);
        Image res = new ImageIcon(img).getImage().getScaledInstance(imgSize, imgSize, Image.SCALE_SMOOTH);

        return new JLabel(new ImageIcon(res));
    }

    /**
     * Creates a component to be used as header over the tabbed pane.
     * 
     * @param player the player to interact with.
     * @return the header
     */
    protected JComponent makeHeader(PlayerModel player) {
        return null;
    }

    /**
     * Creates a component to be placed at the right of each item view.
     * 
     * @param player the player to interact with.
     * @param inventory the currently active inventory.
     * @param item the currently selected item.
     * @return a component
     */
    protected JComponent makeSelectionButtons(InventoryItemInfo info) {
        return null;
    }

    @Override
    protected List<Component> getUIComponents() {
        return Arrays.asList(panel);
    }

    public class InventoryItemInfo {
        public final PlayerModel player;
        public final InventoryModel inventory;
        public final InventoryItem item;

        public InventoryItemInfo(PlayerModel player, InventoryModel inventory, InventoryItem item) {
            this.player = player;
            this.inventory = inventory;
            this.item = item;
        }
    }

}
