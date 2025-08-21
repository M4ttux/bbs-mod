package mchorse.bbs_mod.ui.forms.editors.panels.widgets;

import mchorse.bbs_mod.BBSSettings;
import mchorse.bbs_mod.forms.CustomVertexConsumerProvider;
import mchorse.bbs_mod.forms.FormUtilsClient;
import mchorse.bbs_mod.ui.UIKeys;
import mchorse.bbs_mod.ui.framework.UIContext;
import mchorse.bbs_mod.ui.framework.elements.UIElement;
import mchorse.bbs_mod.ui.framework.elements.buttons.UIIcon;
import mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlay;
import mchorse.bbs_mod.ui.utils.UIUtils;
import mchorse.bbs_mod.ui.utils.icons.Icons;
import mchorse.bbs_mod.utils.colors.Colors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

public class UIItemStack extends UIElement
{
    private Consumer<ItemStack> callback;
    private ItemStack stack;
    private boolean opened;
    private UIIcon inventoryButton;

    public UIItemStack(Consumer<ItemStack> callback)
    {
        this.stack = ItemStack.EMPTY;
        this.callback = callback;

        // Create inventory selection button
        this.inventoryButton = new UIIcon(Icons.PLAYER, (b) -> this.openInventorySelector());
        this.inventoryButton.relative(this).x(1.0F, -22).y(0).w(20).h(20);
        this.add(this.inventoryButton);

        this.context((menu) ->
        {
            menu.action(Icons.PASTE, UIKeys.ITEM_STACK_CONTEXT_PASTE, () ->
            {
                ItemStack stack = MinecraftClient.getInstance().player.getMainHandStack().copy();

                if (this.callback != null)
                {
                    this.callback.accept(stack);
                }

                this.setStack(stack);
            });

            menu.action(Icons.CLOSE, UIKeys.ITEM_STACK_CONTEXT_RESET, () ->
            {
                if (this.callback != null)
                {
                    this.callback.accept(ItemStack.EMPTY);
                }

                this.setStack(ItemStack.EMPTY);
            });
        });

        this.h(20);
    }

    private void openInventorySelector()
    {
        UIPlayerInventoryPanel panel = new UIPlayerInventoryPanel((i) ->
        {
            if (this.callback != null)
            {
                this.callback.accept(i);
            }

            this.setStack(i);
        });

        panel.onClose((a) -> this.opened = false);

        UIOverlay.addOverlay(this.getContext(), panel, 220, 160);
        UIUtils.playClick();
        this.opened = true;
    }
    
    public void setStack(ItemStack stack)
    {
        this.stack = stack == null ? ItemStack.EMPTY : stack.copy();
    }

    protected boolean subMouseClicked(UIContext context)
    {
        // Check if inventory button was clicked
        if (this.inventoryButton.area.isInside(context) && context.mouseButton == 0)
        {
            return false; // Let the button handle the click
        }
        
        if (this.area.isInside(context) && context.mouseButton == 0)
        {
            this.opened = true;

            UIItemStackOverlayPanel panel = new UIItemStackOverlayPanel((i) ->
            {
                if (this.callback != null)
                {
                    this.callback.accept(i);
                }

                this.setStack(i);
            }, this.stack);

            panel.onClose((a) -> this.opened = false);

            UIOverlay.addOverlay(this.getContext(), panel, 0.9F, 0.5F);
            UIUtils.playClick();

            return true;
        } else {
            return super.subMouseClicked(context);
        }
    }

    public void render(UIContext context)
    {
        int border = this.opened ? Colors.A100 | BBSSettings.primaryColor.get() : Colors.WHITE;

        // Adjust the item display area to account for the inventory button
        int itemAreaWidth = this.area.w - 22; // Leave space for inventory button

        context.batcher.box((float)this.area.x, (float)this.area.y, (float)(this.area.x + itemAreaWidth), (float)this.area.ey(), border);
        context.batcher.box((float)(this.area.x + 1), (float)(this.area.y + 1), (float)(this.area.x + itemAreaWidth - 1), (float)(this.area.ey() - 1), -3750202);

        if (this.stack != null && !this.stack.isEmpty())
        {
            MatrixStack matrices = context.batcher.getContext().getMatrices();
            CustomVertexConsumerProvider consumers = FormUtilsClient.getProvider();

            matrices.push();
            consumers.setUI(true);
            int centerX = this.area.x + (itemAreaWidth / 2);
            context.batcher.getContext().drawItem(this.stack, centerX - 8, this.area.my() - 8);
            context.batcher.getContext().drawItemInSlot(context.batcher.getFont().getRenderer(), this.stack, centerX - 8, this.area.my() - 8);
            consumers.setUI(false);
            matrices.pop();
        }

        super.render(context);
    }
}