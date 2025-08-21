package mchorse.bbs_mod.ui.forms.editors.panels.widgets;

import mchorse.bbs_mod.ui.UIKeys;
import mchorse.bbs_mod.ui.framework.UIContext;
import mchorse.bbs_mod.ui.framework.elements.UIElement;
import mchorse.bbs_mod.ui.framework.elements.buttons.UIButton;
import mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlayPanel;
import mchorse.bbs_mod.utils.colors.Colors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Panel that shows player inventory or creative inventory for item selection
 */
public class UIInventorySelectionPanel extends UIOverlayPanel
{
    private Consumer<ItemStack> callback;
    private UIElement inventoryGrid;
    private UIButton creativeModeToggle;
    private boolean isCreativeMode;

    public UIInventorySelectionPanel(Consumer<ItemStack> callback)
    {
        super(UIKeys.GENERAL_SEARCH); // Using existing key for now
        
        this.callback = callback;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        this.isCreativeMode = player != null && player.getAbilities().creativeMode;
        
        this.setupUI();
        this.updateInventoryDisplay();
    }

    private void setupUI()
    {
        this.creativeModeToggle = new UIButton(
            this.isCreativeMode ? UIKeys.GENERAL_SEARCH : UIKeys.GENERAL_NONE, 
            (b) -> this.toggleInventoryMode()
        );
        this.creativeModeToggle.relative(this.content).x(1.0F, -80).y(6).w(74).h(20);
        
        this.inventoryGrid = new UIElement();
        this.inventoryGrid.relative(this.content).xy(6, 32).w(1.0F, -12).h(1.0F, -38);
        
        this.content.add(this.creativeModeToggle, this.inventoryGrid);
    }

    private void toggleInventoryMode()
    {
        this.isCreativeMode = !this.isCreativeMode;
        this.creativeModeToggle.label = this.isCreativeMode ? UIKeys.GENERAL_SEARCH : UIKeys.GENERAL_NONE;
        this.updateInventoryDisplay();
    }

    private void updateInventoryDisplay()
    {
        this.inventoryGrid.removeAll();
        
        if (this.isCreativeMode)
        {
            this.setupCreativeInventory();
        }
        else
        {
            this.setupPlayerInventory();
        }
    }

    private void setupPlayerInventory()
    {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return;
        
        PlayerInventory inventory = player.getInventory();
        List<ItemStack> items = new ArrayList<>();
        
        // Add main inventory (hotbar + main inventory)
        for (int i = 0; i < inventory.main.size(); i++)
        {
            ItemStack stack = inventory.main.get(i);
            if (!stack.isEmpty())
            {
                items.add(stack);
            }
        }
        
        // Add armor slots
        for (int i = 0; i < inventory.armor.size(); i++)
        {
            ItemStack stack = inventory.armor.get(i);
            if (!stack.isEmpty())
            {
                items.add(stack);
            }
        }
        
        // Add offhand
        for (int i = 0; i < inventory.offHand.size(); i++)
        {
            ItemStack stack = inventory.offHand.get(i);
            if (!stack.isEmpty())
            {
                items.add(stack);
            }
        }
        
        this.createItemGrid(items);
    }

    private void setupCreativeInventory()
    {
        List<ItemStack> items = new ArrayList<>();
        
        // For now, add all items from registry as a simple solution
        Registries.ITEM.forEach(item -> {
            if (item.getDefaultStack() != null && !item.getDefaultStack().isEmpty()) {
                items.add(new ItemStack(item));
            }
        });
        
        this.createItemGrid(items);
    }

    private void createItemGrid(List<ItemStack> items)
    {
        int columns = 9; // Minecraft inventory standard
        int rows = (items.size() + columns - 1) / columns; // Calculate needed rows
        int slotSize = 20;
        int spacing = 2;
        
        int totalWidth = columns * (slotSize + spacing) - spacing;
        int totalHeight = rows * (slotSize + spacing) - spacing;
        
        // Center the grid
        int startX = Math.max(0, (this.inventoryGrid.area.w - totalWidth) / 2);
        int startY = 0;
        
        for (int i = 0; i < items.size(); i++)
        {
            ItemStack stack = items.get(i);
            int row = i / columns;
            int col = i % columns;
            
            int x = startX + col * (slotSize + spacing);
            int y = startY + row * (slotSize + spacing);
            
            UIItemSlot slot = new UIItemSlot(stack, this::selectItem);
            slot.relative(this.inventoryGrid).xy(x, y).wh(slotSize, slotSize);
            
            this.inventoryGrid.add(slot);
        }
        
        // Update scroll height if needed
        this.inventoryGrid.h(Math.max(totalHeight, this.inventoryGrid.area.h));
    }

    private void selectItem(ItemStack stack)
    {
        if (this.callback != null)
        {
            this.callback.accept(stack.copy());
        }
        
        this.close();
    }

    /**
     * Individual item slot widget
     */
    private static class UIItemSlot extends UIElement
    {
        private ItemStack stack;
        private Consumer<ItemStack> callback;
        private boolean hovered;

        public UIItemSlot(ItemStack stack, Consumer<ItemStack> callback)
        {
            this.stack = stack;
            this.callback = callback;
        }

        @Override
        protected boolean subMouseClicked(UIContext context)
        {
            if (this.area.isInside(context))
            {
                if (this.callback != null)
                {
                    this.callback.accept(this.stack);
                }
                return true;
            }
            
            return super.subMouseClicked(context);
        }

        @Override
        public void render(UIContext context)
        {
            this.hovered = this.area.isInside(context);
            
            // Render slot background
            int color = this.hovered ? Colors.A100 | Colors.WHITE : Colors.A50 | Colors.WHITE;
            context.batcher.box(this.area.x, this.area.y, this.area.ex(), this.area.ey(), color);
            context.batcher.box(this.area.x + 1, this.area.y + 1, this.area.ex() - 1, this.area.ey() - 1, 0xFF373737);
            
            // Render item
            if (!this.stack.isEmpty())
            {
                context.batcher.getContext().drawItem(this.stack, this.area.mx() - 8, this.area.my() - 8);
                context.batcher.getContext().drawItemInSlot(context.batcher.getFont().getRenderer(), this.stack, this.area.mx() - 8, this.area.my() - 8);
            }
            
            // Show tooltip on hover (simplified for now)
            if (this.hovered && !this.stack.isEmpty())
            {
                // Tooltip will be handled by parent context
            }

            super.render(context);
        }
    }
}
